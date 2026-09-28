"""Bhashini (ULCA / Dhruva) client — ASR, TTS and translation.

Flow (two calls):
  1. getModelsPipeline  → returns the inference endpoint + per-task serviceId
  2. inference/pipeline → runs ASR / TTS / translation with that serviceId

Keys come from .env (BHASHINI_USER_ID, BHASHINI_API_KEY, BHASHINI_PIPELINE_ID)
and never reach the app or logs (RULES.md §15, §33). Without keys the
service runs in SAMPLE mode so the UI still works; responses say so.

Language codes: hi (Hindi), en (English), bho (Bhojpuri), mag (Magahi),
mai (Maithili). Regional support varies by task — when a pipeline is not
available for a language we fall back to Hindi and report `language_used`.
"""

from __future__ import annotations

import base64
import os
from dataclasses import dataclass

import httpx

AUTH_URL = "https://meity-auth.ulcacontrib.org/ulca/apis/v0/model/getModelsPipeline"
DEFAULT_PIPELINE_ID = "64392f96daac500b55c543cd"  # MeitY public pipeline
FALLBACK_LANG = "hi"


@dataclass
class BhashiniConfig:
    user_id: str = os.getenv("BHASHINI_USER_ID", "")
    api_key: str = os.getenv("BHASHINI_API_KEY", "")
    pipeline_id: str = os.getenv("BHASHINI_PIPELINE_ID", DEFAULT_PIPELINE_ID)

    @property
    def enabled(self) -> bool:
        return bool(self.user_id and self.api_key)


@dataclass
class TaskResult:
    text: str | None = None            # transcript / translation
    audio_base64: str | None = None    # TTS output (wav)
    language_used: str = FALLBACK_LANG
    is_sample_data: bool = False


class BhashiniClient:
    def __init__(self, cfg: BhashiniConfig | None = None, timeout: float = 30.0):
        self.cfg = cfg or BhashiniConfig()
        self.timeout = timeout
        self._pipeline_cache: dict[str, dict] = {}

    # ---------- step 1: pipeline discovery ----------
    async def _pipeline(self, task: str, source: str, target: str | None = None) -> dict:
        key = f"{task}:{source}:{target}"
        if key in self._pipeline_cache:
            return self._pipeline_cache[key]
        lang: dict = {"sourceLanguage": source}
        if target:
            lang["targetLanguage"] = target
        body = {
            "pipelineTasks": [{"taskType": task, "config": {"language": lang}}],
            "pipelineRequestConfig": {"pipelineId": self.cfg.pipeline_id},
        }
        headers = {"userID": self.cfg.user_id, "ulcaApiKey": self.cfg.api_key}
        async with httpx.AsyncClient(timeout=self.timeout) as c:
            r = await c.post(AUTH_URL, json=body, headers=headers)
            r.raise_for_status()
            data = r.json()
        cfg = data["pipelineResponseConfig"][0]["config"][0]
        ep = data["pipelineInferenceAPIEndPoint"]
        out = {
            "callback": ep["callbackUrl"],
            "auth_name": ep["inferenceApiKey"]["name"],
            "auth_value": ep["inferenceApiKey"]["value"],
            "service_id": cfg["serviceId"],
        }
        self._pipeline_cache[key] = out
        return out

    async def _pipeline_with_fallback(self, task: str, source: str, target: str | None = None) -> tuple[dict, str]:
        try:
            return await self._pipeline(task, source, target), source
        except (httpx.HTTPError, KeyError, IndexError):
            if source == FALLBACK_LANG:
                raise
            return await self._pipeline(task, FALLBACK_LANG, target), FALLBACK_LANG

    # ---------- step 2: inference ----------
    async def _infer(self, p: dict, tasks: list[dict], input_data: dict) -> dict:
        headers = {p["auth_name"]: p["auth_value"], "Content-Type": "application/json"}
        async with httpx.AsyncClient(timeout=self.timeout) as c:
            r = await c.post(p["callback"], json={"pipelineTasks": tasks, "inputData": input_data}, headers=headers)
            r.raise_for_status()
            return r.json()

    async def transcribe(self, wav_bytes: bytes, language: str = "hi", sampling_rate: int = 16000) -> TaskResult:
        if not self.cfg.enabled:
            return TaskResult(text="मेरे पास ₹1 लाख है, मैं डेयरी बढ़ाना चाहती हूँ", language_used=language, is_sample_data=True)
        p, used = await self._pipeline_with_fallback("asr", language)
        tasks = [{"taskType": "asr", "config": {
            "language": {"sourceLanguage": used}, "serviceId": p["service_id"],
            "audioFormat": "wav", "samplingRate": sampling_rate}}]
        data = await self._infer(p, tasks, {"audio": [{"audioContent": base64.b64encode(wav_bytes).decode()}]})
        text = data["pipelineResponse"][0]["output"][0]["source"]
        return TaskResult(text=text, language_used=used)

    async def synthesise(self, text: str, language: str = "hi", gender: str = "female") -> TaskResult:
        if not self.cfg.enabled:
            return TaskResult(audio_base64=None, language_used=language, is_sample_data=True)
        p, used = await self._pipeline_with_fallback("tts", language)
        tasks = [{"taskType": "tts", "config": {
            "language": {"sourceLanguage": used}, "serviceId": p["service_id"], "gender": gender, "samplingRate": 8000}}]
        data = await self._infer(p, tasks, {"input": [{"source": text}]})
        audio = data["pipelineResponse"][0]["audio"][0]["audioContent"]
        return TaskResult(audio_base64=audio, language_used=used)

    async def translate(self, text: str, source: str, target: str) -> TaskResult:
        if source == target:
            return TaskResult(text=text, language_used=source)
        if not self.cfg.enabled:
            return TaskResult(text=text, language_used=source, is_sample_data=True)
        p, used = await self._pipeline_with_fallback("translation", source, target)
        tasks = [{"taskType": "translation", "config": {
            "language": {"sourceLanguage": used, "targetLanguage": target}, "serviceId": p["service_id"]}}]
        data = await self._infer(p, tasks, {"input": [{"source": text}]})
        return TaskResult(text=data["pipelineResponse"][0]["output"][0]["target"], language_used=used)


client = BhashiniClient()
