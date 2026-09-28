"""ANN sales forecaster — a small multilayer perceptron in pure NumPy (Phase 26).

Purpose: predict next-month revenue from recent history + season, so the
What-If simulator can use a data-driven base case instead of a flat average.

Guardrails (RULES.md §5.4, §5.5, PHASE 26):
- Output is an ESTIMATE with a range, never a fact.
- It never overrides user-provided numbers; the caller decides.
- Training is deterministic (fixed seed) so results are reproducible.
"""

from __future__ import annotations

from dataclasses import dataclass

import numpy as np

LAGS = 3  # previous 3 months as inputs


@dataclass
class Forecast:
    point: int
    low: int
    high: int
    confidence: str          # high / medium / low
    training_rmse: float
    n_train: int
    evidence: str = "ESTIMATE"
    note: str = "Anumaan hai — aapke pichhle mahino ke aankdon aur mausam par aadharit. Guarantee nahi."


class MLP:
    """1 hidden layer, tanh, MSE loss, full-batch gradient descent."""

    def __init__(self, n_in: int, n_hidden: int = 8, seed: int = 7):
        rng = np.random.default_rng(seed)
        self.W1 = rng.normal(0, 0.5, (n_in, n_hidden))
        self.b1 = np.zeros(n_hidden)
        self.W2 = rng.normal(0, 0.5, (n_hidden, 1))
        self.b2 = np.zeros(1)

    def forward(self, X: np.ndarray) -> tuple[np.ndarray, np.ndarray]:
        H = np.tanh(X @ self.W1 + self.b1)
        return H, H @ self.W2 + self.b2

    def fit(self, X: np.ndarray, y: np.ndarray, epochs: int = 3000, lr: float = 0.02, l2: float = 1e-4) -> float:
        y = y.reshape(-1, 1)
        n = len(X)
        for _ in range(epochs):
            H, out = self.forward(X)
            err = out - y
            dW2 = H.T @ err / n + l2 * self.W2
            db2 = err.mean(axis=0)
            dH = err @ self.W2.T * (1 - H**2)
            dW1 = X.T @ dH / n + l2 * self.W1
            db1 = dH.mean(axis=0)
            self.W2 -= lr * dW2
            self.b2 -= lr * db2
            self.W1 -= lr * dW1
            self.b1 -= lr * db1
        _, out = self.forward(X)
        return float(np.sqrt(np.mean((out - y) ** 2)))

    def predict(self, X: np.ndarray) -> np.ndarray:
        return self.forward(X)[1].ravel()


def _features(history: np.ndarray, month_index: np.ndarray) -> tuple[np.ndarray, np.ndarray]:
    """Rows of [lag1, lag2, lag3, sin(month), cos(month)] → target next value."""
    X, y = [], []
    for t in range(LAGS, len(history)):
        m = month_index[t]
        X.append([*history[t - LAGS:t][::-1], np.sin(2 * np.pi * m / 12), np.cos(2 * np.pi * m / 12)])
        y.append(history[t])
    return np.array(X), np.array(y)


def forecast_next_month(history: list[int], start_month: int = 1, seed: int = 7) -> Forecast:
    """history: monthly revenues oldest→newest (≥ 6 values); start_month: 1–12 of history[0]."""
    if len(history) < LAGS + 3:
        raise ValueError("Kam se kam 6 mahine ka data chahiye.")
    h = np.array(history, dtype=float)
    months = np.array([((start_month - 1 + i) % 12) + 1 for i in range(len(h) + 1)])
    scale = h.max() or 1.0
    hs = h / scale

    X, y = _features(hs, months)
    net = MLP(X.shape[1], seed=seed)
    rmse = net.fit(X, y)

    m_next = months[len(h)]
    x_next = np.array([[*hs[-LAGS:][::-1], np.sin(2 * np.pi * m_next / 12), np.cos(2 * np.pi * m_next / 12)]])
    point = float(net.predict(x_next)[0]) * scale
    band = max(rmse * scale * 1.5, 0.05 * point)   # never fake precision: ≥ ±5 %
    rel = band / point if point else 1.0
    confidence = "high" if rel < 0.08 else "medium" if rel < 0.15 else "low"

    return Forecast(
        point=int(round(point, -2)),
        low=int(round(point - band, -2)),
        high=int(round(point + band, -2)),
        confidence=confidence,
        training_rmse=round(rmse * scale, 2),
        n_train=len(X),
    )
