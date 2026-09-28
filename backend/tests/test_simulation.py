from fastapi.testclient import TestClient
from app.main import app

def test_nine_lakh_loan_fails_winter_and_safe_plan_is_smaller():
    r = TestClient(app).post("/api/v1/simulation/loan", json={})
    assert r.status_code == 200, r.text
    b = r.json()
    assert b["evidence"] == "ESTIMATE"
    assert b["emi"] == 19568
    keys = {s["key"]: s for s in b["scenarios"]}
    assert keys["winter"]["status"] == "DEFICIT"
    assert keys["base"]["surplus_after_emi"] > 0
    assert b["safe_plan"] is not None and b["safe_plan"]["loan"] < 900_000
    assert b["safe_plan"]["winter_surplus_after_emi"] >= 0
