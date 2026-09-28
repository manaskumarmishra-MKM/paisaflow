package com.paisaflow.app.core.i18n

import androidx.compose.runtime.Composable

/**
 * Localised strings. Each block is one language.
 * HI / HINGLISH / EN are complete. BHO / MAG / MAI are dialect-adjusted from
 * Hindi and MUST be reviewed by native speakers before release — anything
 * not overridden there falls back to Hindi.
 */
data class Strings(
    // Shared chrome
    val navHome: String, val navTwin: String, val navTalk: String, val navWhatIf: String, val navActions: String,
    val listenAudio: String, val sampleData: String, val speakNewAction: String,
    // Actions screen
    val actionLayerChip: String, val pendingChip: (Int) -> String, val actionsTitle: String, val actionsIntro: String,
    val listenAudioSub: String, val listenBtn: String, val pauseBtn: String,
    val prioMostUrgent: String, val prioOps: String, val prioFinance: String,
    val daysOverdue: (Int) -> String, val endsInDays: (Int) -> String, val safeOption: String,
    val factChip: String, val estimateChip: String, val aiChip: String,
    val recoverable: String, val callLabel: String, val whyLabel: String,
    val draftLabel: String, val edit: String, val approveSend: String, val changeMsg: String, val snooze: String,
    val logOutcome: String, val paidYes: String, val paidTomorrow: String,
    val stockLevel: String, val remaining: (Int, Double) -> String, val available: (Int) -> String, val minSafe: (Int) -> String,
    val callDealer: String, val checkRates: String,
    val unsafeLoan: String, val recommended: String, val monthlyEmi: String, val winterRisk: String, val bufferSafe: (String) -> String,
    val viewSafePlan: String,
    val helpTitle: String, val helpSub: String, val explainStepByStep: String,
)

private val HI = Strings(
    navHome = "होम", navTwin = "व्यापार", navTalk = "बोलिए", navWhatIf = "अनुमान", navActions = "कार्य",
    listenAudio = "आवाज़ में सुनें (Listen in Audio)", sampleData = "नमूना डेटा · डेमो व्यापार (असली आँकड़े नहीं)",
    speakNewAction = "बोलकर नया काम जोड़ें (Speak New Action)",
    actionLayerChip = "प्राथमिक कार्य · ACTION LAYER", pendingChip = { "$it कार्य लंबित (Pending)" },
    actionsTitle = "आज के ३ ज़रूरी काम",
    actionsIntro = "मुनीम जी द्वारा प्राथमिक क्रम में व्यवस्थित — केवल आपकी मंज़ूरी के बाद ही आगे बढ़ेंगे (100% सुरक्षित मानव नियंत्रण).",
    listenAudioSub = "हिंदी एवं भोजपुरी में पूरा विवरण", listenBtn = "सुने", pauseBtn = "रुकें",
    prioMostUrgent = "१. सबसे ज़रूरी", prioOps = "२. संचालन व स्टॉक", prioFinance = "३. सुरक्षित वित्तीय निर्णय",
    daysOverdue = { "$it दिन बकाया" }, endsInDays = { "$it दिन में ख़त्म" }, safeOption = "सुरक्षित विकल्प",
    factChip = "FACT (पक्का)", estimateChip = "ESTIMATE (अनुमान)", aiChip = "AI INFERENCE",
    recoverable = "वसूली योग्य", callLabel = "कॉल करें", whyLabel = "क्यों?",
    draftLabel = "मुनीम जी द्वारा तैयार संदेश (WhatsApp ड्राफ्ट):", edit = "बदलें",
    approveSend = "✓ संदेश भेजें (Approve & Send WhatsApp)", changeMsg = "✎ संदेश बदलें", snooze = "शाम 6 बजे (Snooze)",
    logOutcome = "क्या रमेश जी ने पैसे दे दिए? (Log Outcome)", paidYes = "हाँ, नकद मिल गए", paidTomorrow = "कल देंगे बोला",
    stockLevel = "वर्तमान चारा स्टॉक स्तर", remaining = { p, d -> "$p% शेष ($d दिन)" }, available = { "उपलब्ध: $it बोरी" }, minSafe = { "न्यूनतम सुरक्षित: $it बोरी" },
    callDealer = "📞 डीलर को कॉल मिलाएं (Call Sheikhpura Feeds)", checkRates = "ताज़ा मंडी भाव व दरें चेक करें",
    unsafeLoan = "₹9 लाख लोन (असुरक्षित)", recommended = "(मुनीम अनुशंसित)", monthlyEmi = "मासिक किस्त (EMI)",
    winterRisk = "सर्दियों में सूखा जोखिम", bufferSafe = { "$it मासिक बफ़र सुरक्षित" },
    viewSafePlan = "📊 सुरक्षित योजना देखें (View Safe Plan in What-If)",
    helpTitle = "कोई काम समझ नहीं आ रहा?", helpSub = "मुनीम जी से बोलकर सरल भाषा में समझें।",
    explainStepByStep = "🎙️ एक-एक करके समझाएं (Explain Step-by-Step)",
)

private val HINGLISH = HI.copy(
    navHome = "Home", navTwin = "Vyapar", navTalk = "Boliye", navWhatIf = "Anumaan", navActions = "Kaam",
    listenAudio = "Awaaz mein sunein (Listen in Audio)", sampleData = "Sample data · demo business (asli aankde nahi)",
    speakNewAction = "Bolkar naya kaam jodein (Speak New Action)",
    actionLayerChip = "PRIORITY KAAM · ACTION LAYER", pendingChip = { "$it kaam pending" },
    actionsTitle = "Aaj ke 3 zaroori kaam",
    actionsIntro = "Munim ji ne priority ke hisaab se lagaye hain — sirf aapki manzoori ke baad hi aage badhenge (100% aapka control).",
    listenAudioSub = "Hindi aur Bhojpuri mein poora vivran", listenBtn = "Sunein", pauseBtn = "Rokein",
    prioMostUrgent = "1. Sabse zaroori", prioOps = "2. Sanchalan & stock", prioFinance = "3. Surakshit paisa faisla",
    daysOverdue = { "$it din baaki" }, endsInDays = { "$it din mein khatam" }, safeOption = "Surakshit vikalp",
    factChip = "FACT (pakka)", estimateChip = "ESTIMATE (anumaan)", aiChip = "AI INFERENCE",
    recoverable = "vasooli yogya", callLabel = "Call karein", whyLabel = "Kyun?",
    draftLabel = "Munim ji ka taiyar sandesh (WhatsApp draft):", edit = "Badlein",
    approveSend = "✓ Sandesh bhejein (Approve & Send WhatsApp)", changeMsg = "✎ Sandesh badlein", snooze = "Shaam 6 baje (Snooze)",
    logOutcome = "Kya Ramesh ji ne paise de diye? (Log Outcome)", paidYes = "Haan, nakad mil gaye", paidTomorrow = "Kal denge bola",
    stockLevel = "Abhi ka chara stock", remaining = { p, d -> "$p% baaki ($d din)" }, available = { "Uplabdh: $it bori" }, minSafe = { "Minimum safe: $it bori" },
    callDealer = "📞 Dealer ko call milayein (Call Sheikhpura Feeds)", checkRates = "Taaza mandi bhaav check karein",
    unsafeLoan = "₹9 lakh loan (asurakshit)", recommended = "(Munim ki salah)", monthlyEmi = "Maasik kist (EMI)",
    winterRisk = "Sardi mein sukha risk", bufferSafe = { "$it maasik buffer surakshit" },
    viewSafePlan = "📊 Surakshit plan dekhein (View Safe Plan in What-If)",
    helpTitle = "Koi kaam samajh nahi aa raha?", helpSub = "Munim ji se bolkar saral bhasha mein samjhein.",
    explainStepByStep = "🎙️ Ek-ek karke samjhao (Explain Step-by-Step)",
)

private val EN = HI.copy(
    navHome = "Home", navTwin = "Business", navTalk = "Talk", navWhatIf = "What-If", navActions = "Actions",
    listenAudio = "Listen in Audio", sampleData = "Sample data · demo business (not real statistics)",
    speakNewAction = "Speak New Action",
    actionLayerChip = "PRIORITY WORK · ACTION LAYER", pendingChip = { "$it actions pending" },
    actionsTitle = "Today's 3 important tasks",
    actionsIntro = "Arranged by Munim ji in priority order — nothing proceeds without your approval (100% human control).",
    listenAudioSub = "Full details in Hindi and Bhojpuri", listenBtn = "Play", pauseBtn = "Pause",
    prioMostUrgent = "1. Most urgent", prioOps = "2. Operations & stock", prioFinance = "3. Safe financial decision",
    daysOverdue = { "$it days overdue" }, endsInDays = { "Runs out in $it days" }, safeOption = "Safe option",
    factChip = "FACT", estimateChip = "ESTIMATE", aiChip = "AI INFERENCE",
    recoverable = "recoverable", callLabel = "Call", whyLabel = "Why?",
    draftLabel = "Message prepared by Munim ji (WhatsApp draft):", edit = "Edit",
    approveSend = "✓ Approve & Send WhatsApp", changeMsg = "✎ Change message", snooze = "6 PM (Snooze)",
    logOutcome = "Did Ramesh ji pay? (Log Outcome)", paidYes = "Yes, cash received", paidTomorrow = "Said tomorrow",
    stockLevel = "Current fodder stock level", remaining = { p, d -> "$p% left ($d days)" }, available = { "Available: $it bags" }, minSafe = { "Minimum safe: $it bags" },
    callDealer = "📞 Call the dealer (Sheikhpura Feeds)", checkRates = "Check today's mandi rates",
    unsafeLoan = "₹9 lakh loan (unsafe)", recommended = "(Munim recommended)", monthlyEmi = "Monthly instalment (EMI)",
    winterRisk = "Winter shortfall risk", bufferSafe = { "$it monthly buffer safe" },
    viewSafePlan = "📊 View Safe Plan in What-If",
    helpTitle = "Don't understand a task?", helpSub = "Ask Munim ji by voice, in simple words.",
    explainStepByStep = "🎙️ Explain step by step",
)

// ---- Regional variants: dialect-adjusted from Hindi. REVIEW WITH NATIVE SPEAKERS. ----
private val BHO = HI.copy(
    navHome = "घर", navTwin = "बेपार", navTalk = "बोलीं", navWhatIf = "अनुमान", navActions = "काम",
    listenAudio = "आवाज़ में सुनीं (Listen in Audio)", speakNewAction = "बोल के नया काम जोड़ीं (Speak New Action)",
    actionsTitle = "आज के ३ जरूरी काम",
    actionsIntro = "मुनीम जी प्राथमिकता के हिसाब से लगवले बाड़न — खाली रउआ मंजूरी के बाद आगे बढ़ी (100% रउआ नियंत्रण में).",
    listenAudioSub = "हिंदी आ भोजपुरी में पूरा बिबरन", listenBtn = "सुनीं", pauseBtn = "रोकीं",
    prioMostUrgent = "१. सबसे जरूरी", prioOps = "२. काम-काज आ स्टॉक", prioFinance = "३. सुरक्षित पइसा के फैसला",
    daysOverdue = { "$it दिन बाकी" }, endsInDays = { "$it दिन में खतम" },
    draftLabel = "मुनीम जी के तइयार संदेश (WhatsApp ड्राफ्ट):", edit = "बदलीं",
    approveSend = "✓ संदेश भेजीं (Approve & Send WhatsApp)", changeMsg = "✎ संदेश बदलीं", snooze = "साँझ 6 बजे (Snooze)",
    logOutcome = "का रमेश जी पइसा दे दिहलन? (Log Outcome)", paidYes = "हँ, नगद मिल गइल", paidTomorrow = "काल्ह देब कहलन",
    stockLevel = "अबहीं के चारा स्टॉक", available = { "उपलब्ध: $it बोरी" },
    callDealer = "📞 डीलर के फोन लगाईं (Call Sheikhpura Feeds)", checkRates = "ताजा मंडी भाव देखीं",
    helpTitle = "कवनो काम समझ में ना आवत बा?", helpSub = "मुनीम जी से बोल के सरल भाषा में समझीं।",
    explainStepByStep = "🎙️ एक-एक करके समझाईं (Explain Step-by-Step)",
)

private val MAG = HI.copy(
    navHome = "घर", navTwin = "बेपार", navTalk = "बोलऽ", navWhatIf = "अनुमान", navActions = "काम",
    listenAudio = "आवाज़ में सुनऽ (Listen in Audio)", speakNewAction = "बोल के नया काम जोड़ऽ (Speak New Action)",
    actionsTitle = "आज के ३ जरूरी काम",
    actionsIntro = "मुनीम जी प्राथमिकता के हिसाब से लगइलन हे — खाली अपने के मंजूरी के बाद आगे बढ़त (100% अपने के नियंत्रण में).",
    listenAudioSub = "हिंदी आउ मगही में पूरा बिबरन", listenBtn = "सुनऽ", pauseBtn = "रोकऽ",
    prioMostUrgent = "१. सबसे जरूरी", prioOps = "२. काम-काज आउ स्टॉक", prioFinance = "३. सुरक्षित पइसा के फैसला",
    daysOverdue = { "$it दिन बाकी" }, endsInDays = { "$it दिन में खतम" },
    draftLabel = "मुनीम जी के तइयार संदेश (WhatsApp ड्राफ्ट):", edit = "बदलऽ",
    approveSend = "✓ संदेश भेजऽ (Approve & Send WhatsApp)", changeMsg = "✎ संदेश बदलऽ", snooze = "साँझ 6 बजे (Snooze)",
    logOutcome = "की रमेश जी पइसा दे देलन? (Log Outcome)", paidYes = "हाँ, नगद मिल गेल", paidTomorrow = "काल्ह देबो कहलन",
    stockLevel = "अभी के चारा स्टॉक", available = { "उपलब्ध: $it बोरी" },
    callDealer = "📞 डीलर के फोन लगावऽ (Call Sheikhpura Feeds)", checkRates = "ताजा मंडी भाव देखऽ",
    helpTitle = "कोई काम समझ में न आवइत हे?", helpSub = "मुनीम जी से बोल के सरल भाषा में समझऽ।",
    explainStepByStep = "🎙️ एक-एक करके समझावऽ (Explain Step-by-Step)",
)

private val MAI = HI.copy(
    navHome = "घर", navTwin = "व्यापार", navTalk = "बाजू", navWhatIf = "अनुमान", navActions = "काज",
    listenAudio = "आवाज़ मे सुनू (Listen in Audio)", speakNewAction = "बाजि कऽ नव काज जोड़ू (Speak New Action)",
    actionsTitle = "आइ केर ३ जरूरी काज",
    actionsIntro = "मुनीम जी प्राथमिकता सँ लगौने छथि — मात्र अहाँक मंजूरीक बाद आगू बढ़त (100% अहाँक नियंत्रण मे).",
    listenAudioSub = "हिंदी आ मैथिली मे पूरा विवरण", listenBtn = "सुनू", pauseBtn = "रोकू",
    prioMostUrgent = "१. सभसँ जरूरी", prioOps = "२. संचालन आ स्टॉक", prioFinance = "३. सुरक्षित पाइक निर्णय",
    daysOverdue = { "$it दिन बाँकी" }, endsInDays = { "$it दिन मे खतम" },
    draftLabel = "मुनीम जीक तैयार संदेश (WhatsApp ड्राफ्ट):", edit = "बदलू",
    approveSend = "✓ संदेश पठाउ (Approve & Send WhatsApp)", changeMsg = "✎ संदेश बदलू", snooze = "साँझ 6 बजे (Snooze)",
    logOutcome = "की रमेश जी पाइ दऽ देलनि? (Log Outcome)", paidYes = "हँ, नगद भेटल", paidTomorrow = "काल्हि देब कहलनि",
    stockLevel = "एखनुक चारा स्टॉक", available = { "उपलब्ध: $it बोरी" },
    callDealer = "📞 डीलर केँ फोन लगाउ (Call Sheikhpura Feeds)", checkRates = "ताजा मंडी भाव देखू",
    helpTitle = "कोनो काज बुझि मे नहि आबि रहल?", helpSub = "मुनीम जी सँ बाजि कऽ सरल भाषा मे बुझू।",
    explainStepByStep = "🎙️ एक-एक कऽ बुझाउ (Explain Step-by-Step)",
)

fun stringsFor(lang: AppLanguage): Strings = when (lang) {
    AppLanguage.HI -> HI
    AppLanguage.HINGLISH -> HINGLISH
    AppLanguage.EN -> EN
    AppLanguage.BHO -> BHO
    AppLanguage.MAG -> MAG
    AppLanguage.MAI -> MAI
}

/** Strings for the current [LocalAppLanguage]. */
val strings: Strings
    @Composable get() = stringsFor(LocalAppLanguage.current)
