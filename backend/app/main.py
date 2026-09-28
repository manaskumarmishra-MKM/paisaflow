"""PaisaFlow API — FastAPI entry point.

Run:  uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
Docs: http://localhost:8000/docs
"""

from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse

from app.api.v1 import conversation, health, ml, simulation, speech
from app.core import settings

app = FastAPI(
    title="PaisaFlow API",
    version="0.1.0",
    description="Voice-first AI business companion. Simple outside. Sophisticated inside.",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=[o.strip() for o in settings.cors_origins.split(",")],
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(health.router, prefix="/api/v1")
app.include_router(conversation.router, prefix="/api/v1")
app.include_router(ml.router, prefix="/api/v1")
app.include_router(speech.router, prefix="/api/v1")
app.include_router(simulation.router, prefix="/api/v1")


@app.exception_handler(Exception)
async def unhandled(_: Request, exc: Exception) -> JSONResponse:
    # RULES.md §26 — never leak raw technical errors to the user.
    return JSONResponse(
        status_code=500,
        content={"error": "server_error", "message": "Kuch problem aa gayi. Dobara try karein."},
    )
