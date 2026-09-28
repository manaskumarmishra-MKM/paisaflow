"""POST /api/v1/conversation/message — one utterance in, structured understanding out."""

from fastapi import APIRouter

from app.schemas.conversation import BrainUnderstanding, MessageRequest
from app.services import brain

router = APIRouter(prefix="/conversation", tags=["conversation"])


@router.post("/message", response_model=BrainUnderstanding)
def post_message(req: MessageRequest) -> BrainUnderstanding:
    return brain.understand(req)
