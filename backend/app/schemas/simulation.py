from __future__ import annotations

from pydantic import BaseModel, Field


class LoanSimRequest(BaseModel):
    cash_on_hand: int = Field(100_000, ge=0)
    monthly_revenue: int = Field(78_000, ge=0)
    monthly_costs: int = Field(46_000, ge=0)
    fixed_costs: int = Field(30_000, ge=0)
    loan: int = Field(900_000, ge=0)
    annual_rate_pct: float = Field(11.0, ge=0, le=40)
    tenure_months: int = Field(60, ge=6, le=120)
    new_units: int = Field(6, ge=0, le=50)              # e.g. cows
    capex_per_unit: int = Field(60_000, ge=0)
    revenue_per_unit: int = Field(0, ge=0)   # conservative: new units earn after ramp-up (PRD §13)
    cost_per_unit: int = Field(0, ge=0)
    winter_drop_pct: float = Field(28.0, ge=0, le=90)   # Dec–Feb lean
    sales_drop_pct: float = Field(20.0, ge=0, le=90)
    cost_rise_pct: float = Field(15.0, ge=0, le=200)
    language: str = "hi"


class ScenarioOut(BaseModel):
    key: str
    title: str
    subtitle: str | None
    status: str                     # SAFE / TIGHT / DEFICIT
    revenue: int
    costs: int
    surplus: int
    emi: int
    surplus_after_emi: int
    repayment_burden_pct: int
    note: str | None = None


class SafePlanOut(BaseModel):
    loan: int
    units: int
    emi: int
    winter_surplus_after_emi: int
    buffer_to_keep: int             # 3 months of EMI
    advice: str


class LoanSimResponse(BaseModel):
    question: str
    emi: int
    total_interest: int
    safe_limit: int                 # largest loan (at this step) that survives all scenarios
    base_surplus_after_emi: int
    winter_surplus_after_emi: int
    winter_deficit_3_months: int
    scenarios: list[ScenarioOut]
    safe_plan: SafePlanOut | None
    evidence: str = "ESTIMATE"
    note: str = "Yeh anumaan hai — aapke bataye aankdon aur assumptions par. Loan approval, munafa ya bhavishya ki guarantee nahi."
