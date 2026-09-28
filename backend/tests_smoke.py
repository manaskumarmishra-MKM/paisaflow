"""Quick smoke test: python tests_smoke.py"""
from fastapi.testclient import TestClient

from app.main import app

c = TestClient(app)
assert c.get("/api/v1/health").json()["status"] == "ok"
r = c.post(
    "/api/v1/conversation/message",
    json={"text": "Mere paas ₹1 lakh hai, main dairy expand karna chahti hoon"},
)
assert r.status_code == 200, r.text
body = r.json()
assert body["facts"][2]["value"] == "₹1,00,000", body["facts"]
assert body["next_question"]["preselected"] == 4
print("OK —", len(body["facts"]), "facts;", body["read_back"])
