"""What-If loan/expansion simulation (Phase 24). Deterministic; explains 4 stress cases + a safe plan."""

from __future__ import annotations

from app.finance.engine import BusinessState, LoanTerms, Scenario, emi, project, survives, total_interest
from app.schemas.simulation import LoanSimRequest, LoanSimResponse, SafePlanOut, ScenarioOut


def _status(after: int, burden: float) -> str:
    if after < 0:
        return "DEFICIT"
    if burden > 0.5:
        return "TIGHT"
    return "SAFE"


def _fmt(n: int) -> str:
    s = f"{abs(n):,}"
    # Indian grouping
    head, tail = s.replace(",", "")[:-3], s.replace(",", "")[-3:]
    parts = []
    while len(head) > 2:
        parts.insert(0, head[-2:]); head = head[:-2]
    if head:
        parts.insert(0, head)
    body = ",".join(parts) + ("," if parts else "") + tail
    return ("-" if n < 0 else "") + "₹" + body


def _run(req: LoanSimRequest, loan_amt: int, units: int):
    capex = units * req.capex_per_unit
    state = BusinessState(
        cash_on_hand=req.cash_on_hand + loan_amt - capex,
        monthly_revenue=req.monthly_revenue,
        monthly_costs=req.monthly_costs,
        fixed_costs=req.fixed_costs,
        seasonal_multipliers=(1,) * 11 + (1 - req.winter_drop_pct / 100,),
    )
    loan = LoanTerms(loan_amt, req.annual_rate_pct, req.tenure_months) if loan_amt > 0 else None
    extra_rev, extra_cost = units * req.revenue_per_unit, units * req.cost_per_unit
    base = project(state, loan, Scenario("base", apply_seasonality=False), 12, extra_rev, extra_cost)
    sales = project(state, loan, Scenario("sales", sales_change_pct=-req.sales_drop_pct, apply_seasonality=False), 12, extra_rev, extra_cost)
    cost = project(state, loan, Scenario("cost", cost_change_pct=req.cost_rise_pct, apply_seasonality=False), 12, extra_rev, extra_cost)
    winter = project(state, loan, Scenario("winter", sales_change_pct=-req.winter_drop_pct, apply_seasonality=False), 12, extra_rev, extra_cost)
    seasonal = project(state, loan, Scenario("year"), 12, extra_rev, extra_cost)   # full-year with lean Dec for survivability
    return base, sales, cost, winter, seasonal


def simulate(req: LoanSimRequest) -> LoanSimResponse:
    base, sales, cost, winter, seasonal = _run(req, req.loan, req.new_units)
    e = emi(req.loan, req.annual_rate_pct, req.tenure_months)
    r0 = lambda p: p.rows[0]  # noqa: E731 — steady-state month

    def out(key, title, subtitle, p, note=None) -> ScenarioOut:
        r = r0(p)
        return ScenarioOut(
            key=key, title=title, subtitle=subtitle, status=_status(r.surplus_after_emi, r.repayment_burden),
            revenue=r.revenue, costs=r.costs, surplus=r.surplus, emi=r.emi, surplus_after_emi=r.surplus_after_emi,
            repayment_burden_pct=int(round(min(r.repayment_burden, 9.99) * 100)), note=note,
        )

    winter_deficit = r0(winter).surplus_after_emi
    scenarios = [
        out("base", "सामान्य स्थिति (Base Case)", None, base),
        out("sales", f"दूध भाव में -{int(req.sales_drop_pct)}% गिरावट", "Sales & Rate Shock", sales,
            "डेयरी संकलन केंद्र द्वारा भाव घटाने पर"),
        out("cost", f"चारा व दाना +{int(req.cost_rise_pct)}% महंगा", "Input Cost Shock", cost, "कमज़ोर सुरक्षा बफ़र"),
        out("winter", f"सर्दियों की मंदी -{int(req.winter_drop_pct)}%", "Winter Lean Period (Dec-Feb)", winter,
            f"सर्दियों के 3 महीनों में कुल {_fmt(abs(winter_deficit) * 3)} की उधारी चढ़ने का जोखिम" if winter_deficit < 0 else None),
    ]

    # Safe limit: scan loan amounts downward in ₹50k steps until all four + seasonal year survive.
    step = 50_000
    safe_limit, safe_units = 0, 0
    for amt in range(req.loan, -1, -step):
        # units the loan can fund (own cash kept as buffer): floor((amt)/capex)
        units = min(req.new_units, amt // req.capex_per_unit if req.capex_per_unit else req.new_units)
        b, s, c, w, y = _run(req, amt, units)
        ok = all(r0(p).surplus_after_emi >= 0 for p in (b, s, c, w)) and survives(y)
        if ok:
            safe_limit, safe_units = amt, units
            break

    safe_plan = None
    if safe_limit > 0 and safe_limit < req.loan:
        se = emi(safe_limit, req.annual_rate_pct, req.tenure_months)
        _, _, _, w, _ = _run(req, safe_limit, safe_units)
        wa = r0(w).surplus_after_emi
        safe_plan = SafePlanOut(
            loan=safe_limit, units=safe_units, emi=se, winter_surplus_after_emi=wa, buffer_to_keep=se * 3,
            advice=(f"{_fmt(req.loan)} के बदले {_fmt(safe_limit)} का लोन लें (+{safe_units} गायें) और "
                    f"{_fmt(se * 3)} (3 महीने की किस्त) का सुरक्षा बफ़र अलग खाते में रखें। "
                    f"इससे सर्दियों में भी {_fmt(wa)} की शुद्ध बचत बनी रहेगी।"),
        )

    return LoanSimResponse(
        question=f"“अगर मैं {_fmt(req.loan)} का लोन लूँ तो क्या होगा?”",
        emi=e,
        total_interest=total_interest(LoanTerms(req.loan, req.annual_rate_pct, req.tenure_months)) if req.loan else 0,
        safe_limit=safe_limit,
        base_surplus_after_emi=r0(base).surplus_after_emi,
        winter_surplus_after_emi=winter_deficit,
        winter_deficit_3_months=abs(min(winter_deficit, 0)) * 3,
        scenarios=scenarios,
        safe_plan=safe_plan,
    )
