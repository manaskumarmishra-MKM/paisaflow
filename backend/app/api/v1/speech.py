"""Speech + language endpoints backed by Bhashini (ASR · TTS · translation)."""

import base64
import binascii

from fastapi import APIRouter, HTTPException

from app.schemas.speech import (
    SynthesiseRequest, SynthesiseResponse, TranscribeRequest, TranscribeResponse, TranslateRequest, TranslateResponse,
)
from app.services import bhashini

router = APIRouter(prefix="/speech", tags=["speech"])


@router.post("/transcribe", response_model=TranscribeResponse)
async def transcribe(req: TranscribeRequest) -> TranscribeResponse:
    try:
        wav = base64.b64decode(req.audio_base64, validate=True)
    except (binascii.Error, ValueError) as e:
        raise HTTPException(status_code=422, detail="Audio samajh nahi aaya. Dobara try karein.") from e
    try:
        r = await bhashini.client.transcribe(wav, req.language, req.sampling_rate)
    except Exception as e:  # noqa: BLE001 — never leak provider errors to the user
        raise HTTPException(status_code=502, detail="Awaaz service abhi uplabdh nahi. Type karke batayein.") from e
    return TranscribeResponse(text=r.text or "", language_used=r.language_used, is_sample_data=r.is_sample_data)


@router.post("/synthesise", response_model=SynthesiseResponse)
async def synthesise(req: SynthesiseRequest) -> SynthesiseResponse:
    try:
        r = await bhashini.client.synthesise(req.text, req.language, req.gender)
    except Exception as e:  # noqa: BLE001
        raise HTTPException(status_code=502, detail="Awaaz service abhi uplabdh nahi.") from e
    return SynthesiseResponse(
        audio_base64=r.audio_base64, language_used=r.language_used, is_sample_data=r.is_sample_data,
        note=None if r.audio_base64 else "Bhashini keys set nahi — app device TTS use karega.",
    )


@router.post("/translate", response_model=TranslateResponse)
async def translate(req: TranslateRequest) -> TranslateResponse:
    try:
        r = await bhashini.client.translate(req.text, req.source, req.target)
    except Exception as e:  # noqa: BLE001
        raise HTTPException(status_code=502, detail="Anuvaad service abhi uplabdh nahi.") from e
    return TranslateResponse(text=r.text or req.text, language_used=r.language_used, is_sample_data=r.is_sample_data)
