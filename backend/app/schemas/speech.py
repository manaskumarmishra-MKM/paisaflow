from __future__ import annotations

from typing import Literal

from pydantic import BaseModel, Field

Lang = Literal["hi", "en", "bho", "mag", "mai"]


class TranscribeRequest(BaseModel):
    audio_base64: str = Field(..., description="16 kHz mono 16-bit PCM WAV, base64")
    language: Lang = "hi"
    sampling_rate: int = 16000


class TranscribeResponse(BaseModel):
    text: str
    language_used: str
    is_sample_data: bool = False


class SynthesiseRequest(BaseModel):
    text: str = Field(..., min_length=1, max_length=1000)
    language: Lang = "hi"
    gender: Literal["female", "male"] = "female"


class SynthesiseResponse(BaseModel):
    audio_base64: str | None
    language_used: str
    is_sample_data: bool = False
    note: str | None = None


class TranslateRequest(BaseModel):
    text: str = Field(..., min_length=1, max_length=2000)
    source: Lang = "hi"
    target: Lang = "en"


class TranslateResponse(BaseModel):
    text: str
    language_used: str
    is_sample_data: bool = False
