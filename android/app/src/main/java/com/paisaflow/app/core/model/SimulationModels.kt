package com.paisaflow.app.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoanSimRequest(
    @SerialName("cash_on_hand") val cashOnHand: Int = 100_000,
    @SerialName("monthly_revenue") val monthlyRevenue: Int = 78_000,
    @SerialName("monthly_costs") val monthlyCosts: Int = 46_000,
    @SerialName("fixed_costs") val fixedCosts: Int = 30_000,
    val loan: Int = 900_000,
    @SerialName("annual_rate_pct") val annualRatePct: Double = 11.0,
    @SerialName("tenure_months") val tenureMonths: Int = 60,
    @SerialName("new_units") val newUnits: Int = 6,
    @SerialName("capex_per_unit") val capexPerUnit: Int = 60_000,
    @SerialName("winter_drop_pct") val winterDropPct: Double = 28.0,
    @SerialName("sales_drop_pct") val salesDropPct: Double = 20.0,
    @SerialName("cost_rise_pct") val costRisePct: Double = 15.0,
    val language: String = "hi",
)

@Serializable
data class ScenarioOut(
    val key: String,
    val title: String,
    val subtitle: String? = null,
    val status: String,                       // SAFE / TIGHT / DEFICIT
    val revenue: Int,
    val costs: Int,
    val surplus: Int,
    val emi: Int,
    @SerialName("surplus_after_emi") val surplusAfterEmi: Int,
    @SerialName("repayment_burden_pct") val repaymentBurdenPct: Int,
    val note: String? = null,
)

@Serializable
data class SafePlanOut(
    val loan: Int,
    val units: Int,
    val emi: Int,
    @SerialName("winter_surplus_after_emi") val winterSurplusAfterEmi: Int,
    @SerialName("buffer_to_keep") val bufferToKeep: Int,
    val advice: String,
)

@Serializable
data class LoanSimResponse(
    val question: String,
    val emi: Int,
    @SerialName("total_interest") val totalInterest: Int,
    @SerialName("safe_limit") val safeLimit: Int,
    @SerialName("base_surplus_after_emi") val baseSurplusAfterEmi: Int,
    @SerialName("winter_surplus_after_emi") val winterSurplusAfterEmi: Int,
    @SerialName("winter_deficit_3_months") val winterDeficit3Months: Int,
    val scenarios: List<ScenarioOut>,
    @SerialName("safe_plan") val safePlan: SafePlanOut? = null,
    val evidence: String = "ESTIMATE",
    val note: String = "",
)

/** Indian rupee grouping: 900000 → ₹9,00,000 ; negatives keep the sign. */
fun Int.inr(): String {
    val s = kotlin.math.abs(this).toString()
    if (s.length <= 3) return (if (this < 0) "-₹" else "₹") + s
    val head = s.dropLast(3); val tail = s.takeLast(3)
    val parts = mutableListOf<String>()
    var h = head
    while (h.length > 2) { parts.add(0, h.takeLast(2)); h = h.dropLast(2) }
    if (h.isNotEmpty()) parts.add(0, h)
    return (if (this < 0) "-₹" else "₹") + parts.joinToString(",") + "," + tail
}
