"""ML endpoints: ANN forecast and GA loan/expansion optimisation (Phase 26)."""

from fastapi import APIRouter, HTTPException

from app.finance.engine import BusinessState
from app.ml import ann, ga
from app.schemas.ml import ForecastRequest, ForecastResponse, OptimiseRequest, OptimiseResponse, PlanOut

router = APIRouter(prefix="/ml", tags=["ml"])


@router.post("/forecast", response_model=ForecastResponse)
def forecast(req: ForecastRequest) -> ForecastResponse:
    try:
        f = ann.forecast_next_month(req.monthly_revenue_history, req.start_month)
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e)) from e
    return ForecastResponse(**f.__dict__)


@router.post("/optimise-plan", response_model=OptimiseResponse)
def optimise_plan(req: OptimiseRequest) -> OptimiseResponse:
    state = BusinessState(
        cash_on_hand=req.cash_on_hand,
        monthly_revenue=req.monthly_revenue,
        monthly_costs=req.monthly_costs,
        fixed_costs=req.fixed_costs,
        seasonal_multipliers=tuple(req.seasonal_multipliers),
    )
    unit = ga.UnitEconomics(req.capex_per_unit, req.revenue_per_unit, req.cost_per_unit)
    space = ga.SearchSpace(
        loan_max=req.loan_max, loan_step=req.loan_step, units_max=req.units_max, annual_rate_pct=req.annual_rate_pct,
    )
    res = ga.optimise(state, unit, space, horizon=req.horizon_months)

    def out(p: ga.Plan) -> PlanOut:
        return PlanOut(
            loan=p.loan, tenure_months=p.tenure_months, units=p.units, emi=p.emi,
            cumulative_surplus=p.cumulative_surplus, min_buffer_months=p.min_buffer_months,
            max_repayment_burden=p.max_repayment_burden, survives_all=p.survives_all,
            failed_scenarios=p.failed_scenarios,
        )

    return OptimiseResponse(
        best=out(res.best), alternatives=[out(p) for p in res.top[1:]],
        generations=res.generations, evidence=res.evidence, note=res.note,
    )
