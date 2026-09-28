package com.paisaflow.app.core.model

/** Tone of a metric tile on the Home / Kosh screen. */
enum class MetricTone { PRIMARY, NEUTRAL, WARNING, SECONDARY }

/** One "Kosh Snapshot" tile. */
data class KoshMetric(
    val title: String,          // "Cash in Hand · SBI Sheikhpura A/c"
    val value: String,          // "₹1,00,000"
    val valueSuffix: String?,   // "Safe" / "+8%" / "Stock" / "3 Jan"
    val footer: String,         // "Bina chinta ke surakshit"
    val evidence: EvidenceLabel,
    val tone: MetricTone,
    val icon: MetricIcon,
    val footerIcon: MetricIcon,
)

/** Icon names resolved in the UI layer (keeps the model free of Compose types). */
enum class MetricIcon { PAYMENTS, TRENDING_UP, INVENTORY, PENDING, CHECK_CIRCLE, STOREFRONT, ALARM, GROUP }

enum class ActionKind { SEND, CHECK, CALL }

/** One of "Aaj ke Top 3 Kaam". */
data class TopAction(
    val rank: Int,
    val title: String,
    val subtitle: String,
    val kind: ActionKind,
)

data class OverdueAlert(
    val count: Int,
    val daysLate: Int,
    val counterparty: String,
    val amount: String,
)

data class TwinStatus(
    val name: String,       // "Sunita Dairy Twin v1.2"
    val status: String,     // "Live Synced · Agle hafte ka munafe ka anumaan tayyar"
    val isLive: Boolean,
)

data class HomeData(
    val ownerFirstName: String,
    val businessLabel: String,   // "Sunita Dairy (Sheikhpura)"
    val todayLabel: String,      // "Aaj: 24 Oct"
    val voiceExample: String,    // "“Kundan ne 500 diye...”"
    val overdue: OverdueAlert?,
    val metrics: List<KoshMetric>,
    val topActions: List<TopAction>,
    val twin: TwinStatus,
)

/** Hindi Home (v2) — content model. Sample only. */
data class HiTile(
    val label: String,        // "हाथ में रोकड़ (Safe)"
    val value: String,        // "₹1,00,000"
    val footer: String,       // "SBI शेखपुरा • सुरक्षित बचत"
    val evidence: EvidenceLabel,
    val tone: MetricTone,
    val icon: MetricIcon,
)

data class HiTask(
    val title: String,
    val subtitle: String,
    val buttonLabel: String,   // "भेजें" / "पक्का करें" / "देखें"
    val kind: ActionKind,
    val primary: Boolean,      // filled dark button vs neutral
)

data class HiHomeData(
    val businessName: String,        // "सुनीता डेयरी"
    val blockChip: String,           // "शेखपुरा"
    val liveLine: String,            // "कोष Live · सुरक्षित खाता"
    val greeting: String,            // "नमस्ते सुनीता जी! 👋"
    val dateLine: String,            // "आज: 24 अक्टूबर 2026 • शेखपुरा ब्लॉक"
    val question: String,            // "आज व्यापार में क्या हिसाब देखना है?"
    val heroTitle: String,
    val heroSubtitle: String,
    val readyLine: String,           // "सुनने के लिए तैयार: ..."
    val quickPrompts: List<String>,
    val alertDays: String,           // "12 दिन बकाया"
    val alertName: String,           // "रमेश टी स्टॉल"
    val alertLine: String,           // "₹850 बाकी है · तगादा संदेश तैयार है"
    val tiles: List<HiTile>,
    val twinName: String,            // "सुनीता डेयरी डिजिटल ट्विन v1.2"
    val healthLine: String,          // "व्यापार की सेहत: सुरक्षित"
    val runwayText: String,          // "84 दिन (लगभग 3 महीने)"
    val seasonTitle: String,
    val seasonBody: String,
    val modelAccuracy: String,       // "99.2% मॉडल एक्यूरेसी"
    val tasks: List<HiTask>,
    val lastVoiceEntry: String,
)
