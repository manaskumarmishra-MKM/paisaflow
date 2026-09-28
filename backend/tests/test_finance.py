from app.finance.engine import BusinessState, LoanTerms, BASE, SALES_DOWN, emi, project, survives

DAIRY = BusinessState(cash_on_hand=100_000, monthly_revenue=78_000, monthly_costs=46_000, fixed_costs=30_000,
                      seasonal_multipliers=(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.7))


def test_emi_known_answer():
    # ₹9,00,000 at 11 % for 60 months → ₹19,568 (reducing-balance formula)
    assert emi(900_000, 11.0, 60) == 19_568
    assert emi(0, 11.0, 60) == 0
    assert emi(120_000, 0.0, 12) == 10_000


def test_projection_is_deterministic_and_matches_hand_calc():
    loan = LoanTerms(900_000, 11.0, 60)
    p1 = project(DAIRY, loan, BASE)
    p2 = project(DAIRY, loan, BASE)
    assert [r.closing_cash for r in p1.rows] == [r.closing_cash for r in p2.rows]
    first = p1.rows[0]
    assert first.surplus == 32_000 and first.emi == 19_568 and first.surplus_after_emi == 12_432
    assert first.repayment_burden == 0.61


def test_nine_lakh_fails_stress_but_smaller_loan_survives():
    big = project(DAIRY, LoanTerms(900_000, 11.0, 60), SALES_DOWN)
    assert not survives(big)
    small = project(DAIRY, LoanTerms(300_000, 11.0, 60), SALES_DOWN)
    assert small.rows[0].emi == 6_523
    assert survives(small)
