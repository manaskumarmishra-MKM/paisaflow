package com.paisaflow.app.core.model

/** Detailed Top-3 action cards for the कार्य (Action Layer) screen. Content is language-specific → lives in i18n. */
data class CollectionAction(
    val title: String, val place: String, val amount: String, val context: String, val why: String,
    val customer: String, val relationship: String, val phone: String, val draft: String,
)

data class ReorderAction(
    val title: String, val supplier: String, val amount: String, val saving: String,
    val stockPct: Int, val stockDays: Double, val availableBags: Int, val minBags: Int,
    val product: String, val priceNote: String, val insight: String, val phone: String,
)

data class FinanceAction(
    val title: String, val scheme: String,
    val unsafeLabel: String, val unsafeEmi: String, val unsafeNote: String,
    val safeLabel: String, val safeEmi: String, val safeNote: String,
)

data class ActionsData(
    val pendingCount: Int,
    val audioDuration: String,
    val collection: CollectionAction,
    val reorder: ReorderAction,
    val finance: FinanceAction,
)
