"""GA loan/expansion optimiser — genetic algorithm in pure NumPy (Phase 24/26).

Question it answers: "Kitna loan, kitne mahine, kitni nayi gaay — jo sab
stress scenarios mein survive kare aur 12 mahine ka munafa sabse zyada ho?"

Fitness = cumulative surplus-after-EMI across the horizon in the BASE case,
minus a heavy penalty for every stress scenario the plan fails
(deterministic finance engine decides survival — the GA never does arithmetic
of its own). Fixed seed → reproducible (RULES.md §6.1).
"""

from __future__ import annotations

from dataclasses import dataclass

import numpy as np

from app.finance.engine import BusinessState, LoanTerms, STANDARD_SCENARIOS, project, survives


@dataclass(frozen=True)
class SearchSpace:
    loan_min: int = 0
    loan_max: int = 1_000_000
    loan_step: int = 50_000
    tenure_options: tuple[int, ...] = (24, 36, 48, 60)
    units_min: int = 0
    units_max: int = 8               # e.g. new cows
    annual_rate_pct: float = 11.0


@dataclass(frozen=True)
class UnitEconomics:
    """Per added unit (e.g. one cow): capital needed, monthly revenue, monthly cost."""
    capex_per_unit: int
    revenue_per_unit: int
    cost_per_unit: int
    ramp_months: int = 1             # months before a new unit earns


@dataclass
class Plan:
    loan: int
    tenure_months: int
    units: int
    fitness: float
    survives_all: bool
    failed_scenarios: list[str]
    min_buffer_months: float
    max_repayment_burden: float
    cumulative_surplus: int
    emi: int


@dataclass
class OptimisationResult:
    best: Plan
    top: list[Plan]
    generations: int
    evidence: str = "ESTIMATE"
    note: str = "Yeh sujhaav hai, guarantee nahi. Sab numbers aapke bataye aankdon aur assumptions par aadharit hain."


def _evaluate(genes: np.ndarray, state: BusinessState, space: SearchSpace, unit: UnitEconomics, horizon: int) -> Plan:
    loan = int(genes[0]) * space.loan_step
    tenure = space.tenure_options[int(genes[1])]
    units = int(genes[2])

    # Capital check: own cash + loan must cover capex; otherwise plan is infeasible.
    capex = units * unit.capex_per_unit
    funding_gap = capex - (state.cash_on_hand + loan)

    loan_terms = LoanTerms(loan, space.annual_rate_pct, tenure) if loan > 0 else None
    extra_rev = units * unit.revenue_per_unit
    extra_cost = units * unit.cost_per_unit
    # Cash after capex; disbursement goes to cash then capex is spent.
    post_capex_state = BusinessState(
        cash_on_hand=state.cash_on_hand + loan - capex,
        monthly_revenue=state.monthly_revenue,
        monthly_costs=state.monthly_costs,
        fixed_costs=state.fixed_costs,
        seasonal_multipliers=state.seasonal_multipliers,
    )

    failed: list[str] = []
    base = None
    min_buffer, max_burden = 99.0, 0.0
    for sc in STANDARD_SCENARIOS:
        p = project(post_capex_state, loan_terms, sc, horizon, extra_rev, extra_cost)
        if sc.name == "base":
            base = p
        min_buffer = min(min_buffer, p.min_buffer_months)
        max_burden = max(max_burden, p.annual_repayment_burden)
        if not survives(p):
            failed.append(sc.name)

    assert base is not None
    fitness = float(base.cumulative_surplus_after_emi)
    fitness -= 150_000 * len(failed)                 # each failed stress case
    if funding_gap > 0:
        fitness -= 10 * funding_gap                  # cannot afford the expansion
    from app.finance.engine import emi as _emi
    return Plan(
        loan=loan, tenure_months=tenure, units=units, fitness=fitness,
        survives_all=not failed and funding_gap <= 0, failed_scenarios=failed,
        min_buffer_months=round(min_buffer, 2), max_repayment_burden=round(max_burden, 2),
        cumulative_surplus=base.cumulative_surplus_after_emi,
        emi=_emi(loan, space.annual_rate_pct, tenure) if loan else 0,
    )


def optimise(
    state: BusinessState,
    unit: UnitEconomics,
    space: SearchSpace = SearchSpace(),
    horizon: int = 12,
    population: int = 40,
    generations: int = 60,
    seed: int = 42,
) -> OptimisationResult:
    rng = np.random.default_rng(seed)
    n_loan = (space.loan_max - space.loan_min) // space.loan_step + 1
    n_tenure = len(space.tenure_options)
    n_units = space.units_max - space.units_min + 1

    def random_gene() -> np.ndarray:
        return np.array([rng.integers(n_loan), rng.integers(n_tenure), rng.integers(space.units_min, space.units_min + n_units)])

    pop = np.array([random_gene() for _ in range(population)])

    def score(p: np.ndarray) -> list[Plan]:
        return [_evaluate(g, state, space, unit, horizon) for g in p]

    plans = score(pop)
    for _ in range(generations):
        fit = np.array([p.fitness for p in plans])
        # tournament selection
        def pick() -> np.ndarray:
            i, j = rng.integers(population, size=2)
            return pop[i] if fit[i] >= fit[j] else pop[j]

        elite_idx = np.argsort(fit)[::-1][:2]
        children = [pop[i].copy() for i in elite_idx]
        while len(children) < population:
            a, b = pick(), pick()
            mask = rng.random(3) < 0.5
            child = np.where(mask, a, b)
            # mutation
            if rng.random() < 0.3:
                child[0] = np.clip(child[0] + rng.integers(-2, 3), 0, n_loan - 1)
            if rng.random() < 0.2:
                child[1] = rng.integers(n_tenure)
            if rng.random() < 0.3:
                child[2] = np.clip(child[2] + rng.integers(-1, 2), space.units_min, space.units_max)
            children.append(child)
        pop = np.array(children)
        plans = score(pop)

    plans_sorted = sorted(plans, key=lambda p: p.fitness, reverse=True)
    # de-duplicate by (loan, tenure, units)
    seen, top = set(), []
    for p in plans_sorted:
        key = (p.loan, p.tenure_months, p.units)
        if key not in seen:
            seen.add(key)
            top.append(p)
        if len(top) == 5:
            break
    return OptimisationResult(best=top[0], top=top, generations=generations)
