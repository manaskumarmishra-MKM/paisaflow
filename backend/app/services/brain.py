"""PaisaFlow Brain — intent/entity understanding (Phase 16/17 stub).

Guardrail (RULES.md §5.1, §39): the Brain understands, extracts and routes.
It never performs financial arithmetic; that belongs to the deterministic
finance engine (app/finance/, built in Phase 21).

This first version is rule-based on the hero utterance so the app runs end
to end without an LLM key. Swap `understand()` for an LLM + schema
validation pipeline in Phase 17 without changing the API contract.
"""

from __future__ import annotations

import re

from app.schemas.conversation import (
    BrainUnderstanding,
    ClarificationOption,
    ClarificationQuestion,
    EvidenceLabel,
    ExtractedFact,
    MessageRequest,
)

_LAKH = 100_000
_AMOUNT_RE = re.compile(r"₹?\s*(\d+(?:\.\d+)?)\s*(lakh|lac|l|k|hazaar|hazar)?", re.IGNORECASE)


def _parse_amount(text: str) -> int | None:
    """Extract the first rupee amount as an integer (no LLM involved)."""
    m = _AMOUNT_RE.search(text)
    if not m:
        return None
    n = float(m.group(1))
    unit = (m.group(2) or "").lower()
    if unit in {"lakh", "lac", "l"}:
        n *= _LAKH
    elif unit in {"k", "hazaar", "hazar"}:
        n *= 1_000
    return int(n)


def _format_inr(n: int) -> str:
    """Indian grouping: 1,00,000."""
    s = str(n)
    if len(s) <= 3:
        return f"₹{s}"
    head, tail = s[:-3], s[-3:]
    parts = []
    while len(head) > 2:
        parts.insert(0, head[-2:])
        head = head[:-2]
    if head:
        parts.insert(0, head)
    return "₹" + ",".join(parts) + "," + tail


def _detect_business(text: str) -> tuple[str, str] | None:
    t = text.lower()
    table = {
        "dairy": ("डेयरी · Dairy", "dairy"),
        "kirana": ("किराना · Kirana", "kirana"),
        "tailor": ("सिलाई · Tailoring", "tailoring"),
    }
    for key, val in table.items():
        if key in t:
            return val
    return None


def understand(req: MessageRequest) -> BrainUnderstanding:
    text = req.text.strip()
    capital = _parse_amount(text)
    biz = _detect_business(text)
    wants_expansion = any(w in text.lower() for w in ("expand", "badha", "bada", "grow"))

    facts: list[ExtractedFact] = []
    if biz:
        facts.append(
            ExtractedFact(
                label="Business Type",
                value=biz[0],
                subtitle="Sheikhpura unit",
                evidence=EvidenceLabel.FACT,
            )
        )
    if wants_expansion:
        facts.append(
            ExtractedFact(
                label="Lakshya (Goal)",
                value="विस्तार · Expand",
                subtitle="+6 Cows target" if biz and biz[1] == "dairy" else None,
                evidence=EvidenceLabel.FACT,
            )
        )
    if capital:
        facts.append(
            ExtractedFact(
                label="Apna Paisa",
                value=_format_inr(capital),
                subtitle="Self-investment",
                evidence=EvidenceLabel.FACT,
                is_highlight=True,
            )
        )
    # Illustrative range only — an ESTIMATE, never presented as an offer.
    facts.append(
        ExtractedFact(
            label="Sarkari/KCC Sahayata",
            value="Loan Needed",
            subtitle="~₹2.5L to ₹3L",
            evidence=EvidenceLabel.ESTIMATE,
        )
    )

    read_back_capital = _format_inr(capital) if capital else "kuch paisa"
    read_back = (
        f"“Aapne kaha {read_back_capital} apna paisa hai aur "
        f"{'dairy badhani' if biz and biz[1] == 'dairy' else 'business badhana'} hai. Sahi hai?”"
    )

    next_q = ClarificationQuestion(
        step=2,
        total_steps=3,
        question="Abhi aapke paas kitni gaay hain?",
        translation="(How many cows in your shed currently?)",
        unit_label="Cows",
        preselected=4,
        options=[
            ClarificationOption(value=2, label="2 Gaay"),
            ClarificationOption(value=4, label="4 Gaay", hint="Sunita ji's Shed"),
            ClarificationOption(value=6, label="6 Gaay"),
        ],
    )

    return BrainUnderstanding(
        transcript=f"“{text}”",
        facts=facts,
        read_back=read_back,
        next_question=next_q,
        onboarding_step=1,
        onboarding_total=3,
        is_sample_data=True,
    )
