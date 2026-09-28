"""Schemas for the ML endpoints. Every response is labelled ESTIMATE."""

from __future__ import annotations

from pydantic import BaseModel, Field


class ForecastRequest(BaseModel):
    monthly_revenue_history: list[int] = Field(..., min_length=6, max_length=60, description="Oldest → newest, INR")
    start_month: int = Field(1, ge=1, le=12, description="Calendar month of the first value")


class ForecastResponse(BaseModel):
    point: int
    low: int
    high: int
    confidence: str
    training_rmse: float
    n_train: int
    evidence: str
    note: str


class OptimiseRequest(BaseModel):
    cash_on_hand: int = Field(..., ge=0)
    monthly_revenue: int = Field(..., ge=0)
    monthly_costs: int = Field(..., ge=0)
    fixed_costs: int = Field(..., ge=0)
    seasonal_multipliers: list[float] = Field(default_factory=lambda: [1.0] * 12, min_length=12, max_length=12)
    # expansion unit economics (e.g. one cow)
    capex_per_unit: int = Field(..., ge=0)
    revenue_per_unit: int = Field(..., ge=0)
    cost_per_unit: int = Field(..., ge=0)
    # search space
    loan_max: int = Field(1_000_000, ge=0)
    loan_step: int = Field(50_000, ge=1_000)
    units_max: int = Field(8, ge=0, le=50)
    annual_rate_pct: float = Field(11.0, ge=0, le=40)
    horizon_months: int = Field(12, ge=6, le=36)


class PlanOut(BaseModel):
    loan: int
    tenure_months: int
    units: int
    emi: int
    cumulative_surplus: int
    min_buffer_months: float
    max_repayment_burden: float
    survives_all: bool
    failed_scenarios: list[str]


class OptimiseResponse(BaseModel):
    best: PlanOut
    alternatives: list[PlanOut]
    generations: int
    evidence: str
    note: str
