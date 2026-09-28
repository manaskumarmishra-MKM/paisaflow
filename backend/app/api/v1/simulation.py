from fastapi import APIRouter

from app.schemas.simulation import LoanSimRequest, LoanSimResponse
from app.services import simulation

router = APIRouter(prefix="/simulation", tags=["simulation"])


@router.post("/loan", response_model=LoanSimResponse)
def loan(req: LoanSimRequest) -> LoanSimResponse:
    return simulation.simulate(req)
