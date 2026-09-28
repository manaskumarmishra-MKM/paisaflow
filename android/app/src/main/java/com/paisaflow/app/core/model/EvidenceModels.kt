package com.paisaflow.app.core.model

/** One provenance segment of the "Data Provenance" bar. */
data class ProvenanceShare(val label: String, val percent: Int, val evidence: EvidenceLabel)

data class KeyValue(val key: String, val value: String)

/** A source card on the Evidence Mode screen (FACT / OBSERVATION / ESTIMATE). */
data class EvidenceSource(
    val title: String,            // "आपकी खुद की आवाज़"
    val evidence: EvidenceLabel,
    val percent: Int,
    val description: String,
    val rows: List<KeyValue>,     // shown as value tiles or key/value lines
    val checks: List<String>,     // bullet checks (ESTIMATE card)
    val footer: String?,          // "ऑडियो लॉग आईडी: VOX-BH-2024-8831"
    val iconKind: SourceIcon,
)

enum class SourceIcon { MIC, STOREFRONT, TERMINAL }

/** The "bucket" metaphor used by the simple-explanation card. */
data class BucketShare(val buckets: Int, val label: String, val tone: MetricTone)

data class SimpleExplanation(
    val question: String,             // "EMI और Cash Buffer का असल मतलब क्या है?"
    val metaphorTitle: String,        // "दूध की बाल्टी का नियम (रोज़ाना 8 बाल्टी उत्पादन)"
    val metaphorSubtitle: String,     // "देखें हर बाल्टी का दूध कहाँ जाता है:"
    val shares: List<BucketShare>,
    val plainTalk: List<String>,      // paragraphs of "सीधी बात"
)

data class FieldStory(val person: String, val quote: String)

data class EvidenceData(
    val ownerInitials: String,
    val ownerName: String,
    val ownerMeta: String,            // "शेखपुरा, बिहार • दुग्ध उत्पादक खाता #4102"
    val verifiedScore: String,        // "100% सत्यापित स्कोर"
    val provenance: List<ProvenanceShare>,
    val sources: List<EvidenceSource>,
    val explanation: SimpleExplanation,
    val story: FieldStory,
)
