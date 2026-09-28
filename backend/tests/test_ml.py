from fastapi.testclient import TestClient

from app.finance.engine import BusinessState
from app.main import app
from app.ml import ann, ga

HISTORY = [70_000, 72_000, 75_000, 78_000, 76_000, 74_000, 60_000, 58_000, 62_000, 70_000, 74_000, 77_000, 79_000, 80_000]


def test_ann_forecast_is_reproducible_and_labelled_estimate():
    a = ann.forecast_next_month(HISTORY, start_month=6)
    b = ann.forecast_next_month(HISTORY, start_month=6)
    assert a == b
    assert a.evidence == "ESTIMATE"
    assert a.low < a.point < a.high
    assert 40_000 < a.point < 120_000


def test_ga_prefers_surviving_plan_and_is_reproducible():
    state = BusinessState(100_000, 78_000, 46_000, 30_000, (1,) * 11 + (0.7,))
    unit = ga.UnitEconomics(capex_per_unit=60_000, revenue_per_unit=9_600, cost_per_unit=4_500)
    r1 = ga.optimise(state, unit, generations=25, population=30)
    r2 = ga.optimise(state, unit, generations=25, population=30)
    assert (r1.best.loan, r1.best.tenure_months, r1.best.units) == (r2.best.loan, r2.best.tenure_months, r2.best.units)
    assert r1.best.survives_all
    assert r1.evidence == "ESTIMATE"


def test_api_endpoints():
    c = TestClient(app)
    f = c.post("/api/v1/ml/forecast", json={"monthly_revenue_history": HISTORY, "start_month": 6})
    assert f.status_code == 200 and f.json()["evidence"] == "ESTIMATE"
    o = c.post("/api/v1/ml/optimise-plan", json={
        "cash_on_hand": 100_000, "monthly_revenue": 78_000, "monthly_costs": 46_000, "fixed_costs": 30_000,
        "seasonal_multipliers": [1] * 11 + [0.7], "capex_per_unit": 60_000, "revenue_per_unit": 9_600, "cost_per_unit": 4_500,
    })
    assert o.status_code == 200, o.text
    body = o.json()
    assert body["best"]["survives_all"] is True
    assert "guarantee nahi" in body["note"]
