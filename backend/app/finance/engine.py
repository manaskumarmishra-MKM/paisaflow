"""Deterministic finance engine (Phase 21/23).

Pure functions only — no LLM, no randomness. Identical inputs → identical
outputs (RULES.md §6.1). Amounts are INR integers; periods are months.
"""

from __future__ import annotations

from dataclasses import dataclass, field


@dataclass(frozen=True)
class BusinessState:
    cash_on_hand: int
    monthly_revenue: int
    monthly_costs: int                 # fixed + variable, base case
    fixed_costs: int                   # portion of monthly_costs that does not scale
    seasonal_multipliers: tuple[float, ...] = (1.0,) * 12  # revenue quantity multiplier per month


@dataclass(frozen=True)
class LoanTerms:
    principal: int
    annual_rate_pct: float
    tenure_months: int
    moratorium_months: int = 0


@dataclass(frozen=True)
class Scenario:
    name: str
    sales_change_pct: float = 0.0      # −20 → revenue ×0.8
    cost_change_pct: float = 0.0       # +15 → costs ×1.15
    apply_seasonality: bool = True


BASE = Scenario("base")
SALES_DOWN = Scenario("sales_-20", sales_change_pct=-20)
COST_UP = Scenario("cost_+15", cost_change_pct=15)
COMBINED = Scenario("combined_stress", sales_change_pct=-20, cost_change_pct=15)
STANDARD_SCENARIOS = (BASE, SALES_DOWN, COST_UP, COMBINED)


@dataclass
class MonthRow:
    month: int
    revenue: int
    costs: int
    surplus: int
    emi: int
    surplus_after_emi: int
    closing_cash: int
    repayment_burden: float           # emi / surplus (∞ when surplus ≤ 0 → capped 9.99)
    buffer_months: float              # closing_cash / fixed_costs


@dataclass
class Projection:
    scenario: str
    rows: list[MonthRow] = field(default_factory=list)

    @property
    def worst_month(self) -> MonthRow:
        return min(self.rows, key=lambda r: r.surplus_after_emi)

    @property
    def min_buffer_months(self) -> float:
        return min(r.buffer_months for r in self.rows)

    @property
    def max_repayment_burden(self) -> float:
        return max(r.repayment_burden for r in self.rows)

    @property
    def annual_repayment_burden(self) -> float:
        """Total EMI ÷ total operating surplus over the horizon (9.99 when surplus ≤ 0)."""
        total_emi = sum(r.emi for r in self.rows)
        total_surplus = sum(r.surplus for r in self.rows)
        if total_emi == 0:
            return 0.0
        return round(min(9.99, total_emi / total_surplus), 2) if total_surplus > 0 else 9.99

    @property
    def cumulative_surplus_after_emi(self) -> int:
        return sum(r.surplus_after_emi for r in self.rows)

    @property
    def negative_cash_months(self) -> int:
        return sum(1 for r in self.rows if r.closing_cash < 0)


def emi(principal: int, annual_rate_pct: float, tenure_months: int) -> int:
    """Standard reducing-balance EMI, rounded to the rupee."""
    if principal <= 0 or tenure_months <= 0:
        return 0
    r = annual_rate_pct / 12 / 100
    if r == 0:
        return round(principal / tenure_months)
    f = (1 + r) ** tenure_months
    return round(principal * r * f / (f - 1))


def total_interest(loan: LoanTerms) -> int:
    e = emi(loan.principal, loan.annual_rate_pct, loan.tenure_months)
    interest_only = round(loan.principal * loan.annual_rate_pct / 12 / 100) * loan.moratorium_months
    return e * loan.tenure_months + interest_only - loan.principal


def project(
    state: BusinessState,
    loan: LoanTerms | None,
    scenario: Scenario = BASE,
    horizon: int = 12,
    extra_monthly_revenue: int = 0,
    extra_monthly_costs: int = 0,
    disbursement_to_cash: bool = False,
) -> Projection:
    """Month-by-month projection under one scenario.

    extra_* let callers model an expansion (e.g. +6 cows) whose own revenue
    and costs are added before shocks are applied (PRD §13 rule 3).
    """
    proj = Projection(scenario.name)
    cash = state.cash_on_hand + (loan.principal if (loan and disbursement_to_cash) else 0)
    sales_factor = 1 + scenario.sales_change_pct / 100
    cost_factor = 1 + scenario.cost_change_pct / 100
    monthly_emi = emi(loan.principal, loan.annual_rate_pct, loan.tenure_months) if loan else 0
    interest_only = round(loan.principal * loan.annual_rate_pct / 12 / 100) if loan else 0

    for m in range(1, horizon + 1):
        season = state.seasonal_multipliers[(m - 1) % 12] if scenario.apply_seasonality else 1.0
        revenue = round((state.monthly_revenue + extra_monthly_revenue) * season * sales_factor)
        costs = round((state.monthly_costs + extra_monthly_costs) * cost_factor)
        surplus = revenue - costs
        pay = interest_only if (loan and m <= loan.moratorium_months) else monthly_emi
        after = surplus - pay
        cash += after
        burden = min(9.99, pay / surplus) if surplus > 0 else (9.99 if pay > 0 else 0.0)
        buffer = cash / state.fixed_costs if state.fixed_costs > 0 else 0.0
        proj.rows.append(MonthRow(m, revenue, costs, surplus, pay, after, cash, round(burden, 2), round(buffer, 2)))
    return proj


def survives(proj: Projection, min_buffer_months: float = 1.0, max_burden: float = 0.60) -> bool:
    """Survivability rule (PRD §13 outputs): cash never negative, cash buffer
    never below 1 month of fixed costs, and EMI never above 60 % of the
    year's operating surplus. A single lean month is allowed — that is what
    the buffer is for — as long as cash and buffer hold."""
    return (
        proj.negative_cash_months == 0
        and proj.min_buffer_months >= min_buffer_months
        and proj.annual_repayment_burden <= max_burden
    )
