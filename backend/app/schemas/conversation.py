"""Pydantic schemas for the conversation endpoint.

The shapes here are the contract the Flutter models deserialise
(frontend/lib/core/models/*). Keep both sides in sync.
"""

from __future__ import annotations

from enum import Enum
from typing import Literal

from pydantic import BaseModel, Field


class EvidenceLabel(str, Enum):
    """RULES.md §6.2 / §10.1 — every important value carries one label."""

    FACT = "FACT"
    OBSERVATION = "OBSERVATION"
    ESTIMATE = "ESTIMATE"
    ASSUMPTION = "ASSUMPTION"
    AI_INFERENCE = "AI_INFERENCE"


class MessageRequest(BaseModel):
    text: str = Field(..., min_length=1, max_length=2000)
    language: Literal["hi", "en", "bho", "mag", "mai", "hindi", "hinglish", "english"] = "hi"
    conversation_id: str | None = None


class ExtractedFact(BaseModel):
    label: str
    value: str
    subtitle: str | None = None
    evidence: EvidenceLabel
    is_highlight: bool = False


class ClarificationOption(BaseModel):
    value: int
    label: str
    hint: str | None = None


class ClarificationQuestion(BaseModel):
    step: int
    total_steps: int
    question: str
    translation: str
    unit_label: str
    preselected: int | None = None
    options: list[ClarificationOption]


class BrainUnderstanding(BaseModel):
    transcript: str
    facts: list[ExtractedFact]
    read_back: str
    next_question: ClarificationQuestion
    onboarding_step: int
    onboarding_total: int
    is_sample_data: bool = False
