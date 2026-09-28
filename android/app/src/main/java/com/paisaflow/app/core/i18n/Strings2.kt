package com.paisaflow.app.core.i18n

import androidx.compose.runtime.Composable

/** Strings for the onboarding, Twin, Memory, Khata, Alerts and Profile screens. */
data class Strings2(
    // Splash / language / onboarding
    val tagline: String, val loop: String, val chooseLanguage: String, val continueBtn: String,
    val namaste: String, val onboardIntro: String, val onboardPrompt: String, val orType: String, val typeHint: String,
    // Twin
    val twinTitle: String, val twinSubtitle: String, val lastUpdated: String, val money: String, val market: String, val ops: String, val risk: String,
    val healthTitle: String, val healthStory: String, val cashflow: String, val payments: String, val inventory: String, val expansion: String, val repayment: String,
    val stable: String, val delayed: (Int) -> String, val healthy: String, val needsReview: String, val stress: String, val viewMemory: String, val runWhatIf: String,
    // Memory
    val memoryTitle: String, val today: String, val yesterday: String, val eventSource: String, val correct: String,
    // Voice Khata
    val khataTitle: String, val khataHint: String, val understood: String, val customer: String, val amount: String, val status: String, val pending: String,
    val save: String, val fix: String, val recentEntries: String,
    // Alerts
    val alertsTitle: String, val view: String, val remindMe: String, val dismiss: String,
    // Profile
    val profileTitle: String, val businessProfile: String, val financialInfo: String, val inventoryMenu: String, val customers: String, val location: String,
    val memoryMenu: String, val alertsMenu: String, val languageMenu: String, val privacy: String, val help: String, val lockApp: String,
)

private val HI = Strings2(
    tagline = "आपके बिज़नेस का साथी", loop = "बोलिए • समझिए • बढ़िए", chooseLanguage = "आप किस भाषा में बात करना चाहेंगे?", continueBtn = "आगे बढ़ें",
    namaste = "नमस्ते! 👋", onboardIntro = "मैं आपके बिज़नेस को समझने में मदद करूँगा।", onboardPrompt = "\"आपका बिज़नेस क्या है?\"", orType = "या टाइप करें", typeHint = "जैसे: डेयरी, किराना, सिलाई…",
    twinTitle = "आपका बिज़नेस डिजिटल ट्विन", twinSubtitle = "पैसा · बाज़ार · काम-काज · जोखिम — एक जगह", lastUpdated = "आख़िरी अपडेट: आज", money = "पैसा", market = "बाज़ार", ops = "काम-काज", risk = "जोखिम",
    healthTitle = "व्यापार की सेहत", healthStory = "सबसे ज़रूरी बात: बकाया वसूली में देरी से कैश बफ़र कम हो रहा है।",
    cashflow = "कैश-फ़्लो", payments = "भुगतान", inventory = "स्टॉक", expansion = "विस्तार", repayment = "किस्त",
    stable = "स्थिर", delayed = { "$it बकाया" }, healthy = "ठीक", needsReview = "जाँच ज़रूरी", stress = "दबाव (सर्दी परिदृश्य)", viewMemory = "मेमोरी देखें", runWhatIf = "अनुमान चलाएँ",
    memoryTitle = "बिज़नेस मेमोरी", today = "आज", yesterday = "कल", eventSource = "स्रोत", correct = "सुधारें",
    khataTitle = "वॉइस खाता", khataHint = "\"रमेश को ₹850 का माल दिया\" — बोलिए", understood = "मैंने समझा:", customer = "ग्राहक", amount = "रक़म", status = "स्थिति", pending = "भुगतान बाक़ी",
    save = "सेव करें", fix = "सुधारें", recentEntries = "हाल की प्रविष्टियाँ",
    alertsTitle = "ज़रूरी सूचनाएँ", view = "देखें", remindMe = "याद दिलाना", dismiss = "हटाएँ",
    profileTitle = "मेरा बिज़नेस", businessProfile = "बिज़नेस प्रोफ़ाइल", financialInfo = "वित्तीय जानकारी", inventoryMenu = "स्टॉक", customers = "ग्राहक", location = "स्थान",
    memoryMenu = "बिज़नेस मेमोरी", alertsMenu = "सूचनाएँ", languageMenu = "भाषा", privacy = "गोपनीयता", help = "मदद", lockApp = "ऐप लॉक (फ़िंगरप्रिंट)",
)

private val HINGLISH = HI.copy(
    tagline = "Aapke Business Ka Saathi", loop = "Boliye • Samjhiye • Badhiye", chooseLanguage = "Aap kis language mein baat karna chahenge?", continueBtn = "Aage badhein",
    namaste = "Namaste! 👋", onboardIntro = "Main aapke business ko samajhne mein help karunga.", onboardPrompt = "\"Aapka business kya hai?\"", orType = "Ya type karein", typeHint = "Jaise: dairy, kirana, silai…",
    twinTitle = "Aapka Business Digital Twin", twinSubtitle = "Paisa · Bazaar · Kaam-kaaj · Jokhim — ek jagah", lastUpdated = "Last update: aaj", money = "Paisa", market = "Bazaar", ops = "Kaam-kaaj", risk = "Jokhim",
    healthTitle = "Business ki sehat", healthStory = "Sabse zaroori: baaki vasooli mein deri se cash buffer kam ho raha hai.",
    cashflow = "Cash-flow", payments = "Payments", inventory = "Stock", expansion = "Expansion", repayment = "EMI",
    stable = "Stable", delayed = { "$it baaki" }, healthy = "Theek", needsReview = "Review zaroori", stress = "Dabaav (sardi scenario)", viewMemory = "Memory dekhein", runWhatIf = "Anumaan chalayein",
    memoryTitle = "Business Memory", today = "Aaj", yesterday = "Kal", eventSource = "Source", correct = "Sudharein",
    khataTitle = "Voice Khata", khataHint = "\"Ramesh ko ₹850 ka maal diya\" — boliye", understood = "Maine samjha:", customer = "Customer", amount = "Amount", status = "Status", pending = "Payment pending",
    save = "Save", fix = "Sudharein", recentEntries = "Haal ki entries",
    alertsTitle = "Zaroori soochnayein", view = "Dekhein", remindMe = "Yaad dilana", dismiss = "Hataein",
    profileTitle = "Mera Business", businessProfile = "Business profile", financialInfo = "Financial jankari", inventoryMenu = "Stock", customers = "Customers", location = "Location",
    memoryMenu = "Business Memory", alertsMenu = "Alerts", languageMenu = "Language", privacy = "Privacy", help = "Help", lockApp = "App lock (fingerprint)",
)

private val EN = HI.copy(
    tagline = "Your business companion", loop = "Talk • Understand • Grow", chooseLanguage = "Which language would you like to talk in?", continueBtn = "Continue",
    namaste = "Namaste! 👋", onboardIntro = "I'll help you understand your business.", onboardPrompt = "\"What is your business?\"", orType = "Or type", typeHint = "e.g. dairy, kirana, tailoring…",
    twinTitle = "Your Business Digital Twin", twinSubtitle = "Money · Market · Operations · Risk — in one place", lastUpdated = "Last updated: today", money = "Money", market = "Market", ops = "Operations", risk = "Risk",
    healthTitle = "Business health", healthStory = "Most important: delayed collections are shrinking your cash buffer.",
    cashflow = "Cash-flow", payments = "Payments", inventory = "Inventory", expansion = "Expansion", repayment = "Repayment",
    stable = "Stable", delayed = { "$it delayed" }, healthy = "Healthy", needsReview = "Needs review", stress = "Stress (winter scenario)", viewMemory = "View memory", runWhatIf = "Run What-If",
    memoryTitle = "Business Memory", today = "Today", yesterday = "Yesterday", eventSource = "Source", correct = "Correct",
    khataTitle = "Voice Khata", khataHint = "Say: \"Ramesh ko ₹850 ka maal diya\"", understood = "I understood:", customer = "Customer", amount = "Amount", status = "Status", pending = "Payment pending",
    save = "Save", fix = "Correct", recentEntries = "Recent entries",
    alertsTitle = "Important alerts", view = "View", remindMe = "Remind me", dismiss = "Dismiss",
    profileTitle = "My Business", businessProfile = "Business profile", financialInfo = "Financial information", inventoryMenu = "Inventory", customers = "Customers", location = "Location",
    memoryMenu = "Business Memory", alertsMenu = "Alerts", languageMenu = "Language", privacy = "Privacy", help = "Help", lockApp = "App lock (fingerprint)",
)

fun strings2For(lang: AppLanguage): Strings2 = when (lang) {
    AppLanguage.HINGLISH -> HINGLISH
    AppLanguage.EN -> EN
    else -> HI   // BHO / MAG / MAI: Hindi until native-speaker copy is supplied
}

val strings2: Strings2
    @Composable get() = strings2For(LocalAppLanguage.current)
