package com.paisaflow.app.core.mock

import com.paisaflow.app.core.model.BrainUnderstanding
import com.paisaflow.app.core.model.ClarificationOption
import com.paisaflow.app.core.model.ClarificationQuestion
import com.paisaflow.app.core.model.EvidenceLabel
import com.paisaflow.app.core.model.ExtractedFact

/**
 * SAMPLE DATA ONLY — fictional demo business (RULES.md §44).
 * Used when the backend is unreachable. Never present as real statistics.
 */
object DemoData {
    const val OWNER_NAME = "Sunita Devi"
    const val BUSINESS_TYPE = "Dairy"

    val dairyExpansion = BrainUnderstanding(
        transcript = "“Mere paas ₹1 lakh hai, main dairy expand karna chahti hoon”",
        readBack = "“Aapne kaha ₹1 lakh apna paisa hai aur dairy badhani hai. Sahi hai?”",
        onboardingStep = 1,
        onboardingTotal = 3,
        isSampleData = true,
        facts = listOf(
            ExtractedFact("Business Type", "डेयरी · Dairy", "Sheikhpura unit", EvidenceLabel.FACT),
            ExtractedFact("Lakshya (Goal)", "विस्तार · Expand", "+6 Cows target", EvidenceLabel.FACT),
            ExtractedFact("Apna Paisa", "₹1,00,000", "Self-investment", EvidenceLabel.FACT, isHighlight = true),
            ExtractedFact("Sarkari/KCC Sahayata", "Loan Needed", "~₹2.5L to ₹3L", EvidenceLabel.ESTIMATE),
        ),
        nextQuestion = ClarificationQuestion(
            step = 2,
            totalSteps = 3,
            question = "Abhi aapke paas kitni gaay hain?",
            translation = "(How many cows in your shed currently?)",
            unitLabel = "Cows",
            preselected = 4,
            options = listOf(
                ClarificationOption(2, "2 Gaay"),
                ClarificationOption(4, "4 Gaay", "Sunita ji's Shed"),
                ClarificationOption(6, "6 Gaay"),
            ),
        ),
    )
}

/** SAMPLE home dashboard for the fictional demo business (RULES.md §44). */
object DemoHome {
    val data = com.paisaflow.app.core.model.HomeData(
        ownerFirstName = "Sunita",
        businessLabel = "Sunita Dairy (Sheikhpura)",
        todayLabel = "Aaj: 24 Oct",
        voiceExample = "“Kundan ne 500 diye...”",
        overdue = com.paisaflow.app.core.model.OverdueAlert(
            count = 1, daysLate = 12, counterparty = "Ramesh Tea Stall", amount = "₹850",
        ),
        metrics = listOf(
            com.paisaflow.app.core.model.KoshMetric(
                title = "Cash in Hand · SBI Sheikhpura A/c", value = "₹1,00,000", valueSuffix = "Safe",
                footer = "Bina chinta ke surakshit", evidence = EvidenceLabel.FACT,
                tone = com.paisaflow.app.core.model.MetricTone.PRIMARY,
                icon = com.paisaflow.app.core.model.MetricIcon.PAYMENTS,
                footerIcon = com.paisaflow.app.core.model.MetricIcon.CHECK_CIRCLE,
            ),
            com.paisaflow.app.core.model.KoshMetric(
                title = "Monthly Milk Sales (~32 L/din)", value = "₹78,000", valueSuffix = "+8%",
                footer = "Doodh Rate: ₹40/L", evidence = EvidenceLabel.FACT,
                tone = com.paisaflow.app.core.model.MetricTone.NEUTRAL,
                icon = com.paisaflow.app.core.model.MetricIcon.TRENDING_UP,
                footerIcon = com.paisaflow.app.core.model.MetricIcon.STOREFRONT,
            ),
            com.paisaflow.app.core.model.KoshMetric(
                title = "Cattle Feed (Churi/Khal)", value = "14 Din", valueSuffix = "Stock",
                footer = "Reorder Alert Soon", evidence = EvidenceLabel.FACT,
                tone = com.paisaflow.app.core.model.MetricTone.WARNING,
                icon = com.paisaflow.app.core.model.MetricIcon.INVENTORY,
                footerIcon = com.paisaflow.app.core.model.MetricIcon.ALARM,
            ),
            com.paisaflow.app.core.model.KoshMetric(
                title = "Kundan, Ramesh, Shyam baaki", value = "₹18,500", valueSuffix = "3 Jan",
                footer = "Kul 3 Grahak", evidence = EvidenceLabel.FACT,
                tone = com.paisaflow.app.core.model.MetricTone.SECONDARY,
                icon = com.paisaflow.app.core.model.MetricIcon.PENDING,
                footerIcon = com.paisaflow.app.core.model.MetricIcon.GROUP,
            ),
        ),
        topActions = listOf(
            com.paisaflow.app.core.model.TopAction(1, "Ramesh ko WhatsApp reminder bhejein", "₹850 baaki chukane ke liye", com.paisaflow.app.core.model.ActionKind.SEND),
            com.paisaflow.app.core.model.TopAction(2, "Kundan Dairy entry confirm karein", "Subah ka 18 Litre doodh dispatch", com.paisaflow.app.core.model.ActionKind.CHECK),
            com.paisaflow.app.core.model.TopAction(3, "Sheikhpura Feeds se rate confirm karein", "10 bori Sudha Pashu Aahar order", com.paisaflow.app.core.model.ActionKind.CALL),
        ),
        twin = com.paisaflow.app.core.model.TwinStatus(
            name = "Sunita Dairy Twin v1.2",
            status = "Live Synced · Agle hafte ka munafe ka anumaan tayyar",
            isLive = true,
        ),
    )
}

/** SAMPLE evidence-mode content for the fictional demo business (RULES.md §44). */
object DemoEvidence {
    val data = com.paisaflow.app.core.model.EvidenceData(
        ownerInitials = "SD",
        ownerName = "सुनीता डेयरी",
        ownerMeta = "शेखपुरा, बिहार • दुग्ध उत्पादक खाता #4102",
        verifiedScore = "100% सत्यापित स्कोर",
        provenance = listOf(
            com.paisaflow.app.core.model.ProvenanceShare("70% सच", 70, EvidenceLabel.FACT),
            com.paisaflow.app.core.model.ProvenanceShare("20% मंडी", 20, EvidenceLabel.OBSERVATION),
            com.paisaflow.app.core.model.ProvenanceShare("10% सूत्र", 10, EvidenceLabel.ESTIMATE),
        ),
        sources = listOf(
            com.paisaflow.app.core.model.EvidenceSource(
                title = "आपकी खुद की आवाज़", evidence = EvidenceLabel.FACT, percent = 70,
                description = "सुनीता देवी द्वारा बोलकर दर्ज कराई गई पक्की जानकारी (Voice Confirmed Record):",
                rows = listOf(
                    com.paisaflow.app.core.model.KeyValue("दुधारू गाय", "4 गायें"),
                    com.paisaflow.app.core.model.KeyValue("पूँजी लागत", "₹1,00,000"),
                    com.paisaflow.app.core.model.KeyValue("मासिक बिक्री", "₹78,000"),
                ),
                checks = emptyList(),
                footer = "ऑडियो लॉग आईडी: VOX-BH-2024-8831",
                iconKind = com.paisaflow.app.core.model.SourceIcon.MIC,
            ),
            com.paisaflow.app.core.model.EvidenceSource(
                title = "शेखपुरा मंडी पड़ताल", evidence = EvidenceLabel.OBSERVATION, percent = 20,
                description = "शेखपुरा ब्लॉक मंडी रेट (अपडेट: 12 सितंबर 2024) से तुलना:",
                rows = listOf(
                    com.paisaflow.app.core.model.KeyValue("सरकारी फैट बेंचमार्क खरीद दर:", "₹41.50 / ली."),
                    com.paisaflow.app.core.model.KeyValue("आसपास की सक्रिय डेयरियां (PostGIS):", "3 प्रतिस्पर्धी मैप किए गए"),
                ),
                checks = emptyList(),
                footer = null,
                iconKind = com.paisaflow.app.core.model.SourceIcon.STOREFRONT,
            ),
            com.paisaflow.app.core.model.EvidenceSource(
                title = "पक्का गणितीय फॉर्मूला", evidence = EvidenceLabel.ESTIMATE, percent = 10,
                description = "PaisaFlow Deterministic Python Engine — कोई मनगढ़ंत अंदाज़ा या AI झूठ (Zero LLM hallucination) नहीं:",
                rows = emptyList(),
                checks = listOf(
                    "Reducing Balance EMI फॉर्मूला (घटते मूलधन पर ब्याज)",
                    "4-स्ट्रैस शॉक मैट्रिक्स (सूखा, बीमारी, चारे का भाव वृद्धि)",
                ),
                footer = null,
                iconKind = com.paisaflow.app.core.model.SourceIcon.TERMINAL,
            ),
        ),
        explanation = com.paisaflow.app.core.model.SimpleExplanation(
            question = "\"EMI और Cash Buffer का असल मतलब क्या है?\"",
            metaphorTitle = "दूध की बाल्टी का नियम (रोज़ाना 8 बाल्टी उत्पादन)",
            metaphorSubtitle = "देखें हर बाल्टी का दूध कहाँ जाता है:",
            shares = listOf(
                com.paisaflow.app.core.model.BucketShare(3, "चारा-दवा", com.paisaflow.app.core.model.MetricTone.WARNING),
                com.paisaflow.app.core.model.BucketShare(3, "बैंक EMI", com.paisaflow.app.core.model.MetricTone.PRIMARY),
                com.paisaflow.app.core.model.BucketShare(2, "बचत बफ़र", com.paisaflow.app.core.model.MetricTone.NEUTRAL),
            ),
            plainTalk = listOf(
                "EMI का मतलब है हर महीने दूध बेचकर बैंक को लौटाई जाने वाली पक्की क़िस्त।",
                "अगर जाड़े या बीमारी में दूध 8 बाल्टी से घटकर 5 बाल्टी हो जाए, तो बची हुई 2 बाल्टी से घर का खर्च नहीं चलेगा। इसीलिए 'बफ़र' जरूरी है ताकि आपकी जेब खाली न हो!",
            ),
        ),
        story = com.paisaflow.app.core.model.FieldStory(
            person = "कमला ताई, बरबीघा",
            quote = "\"जब मेरी एक गाय बीमार हुई, तो इसी 2 बाल्टी बफ़र ने मेरी क़िस्त भरी और मुझे महाजन के पास नहीं जाना पड़ा।\"",
        ),
    )
}

/** SAMPLE Hindi Home (v2) content (RULES.md §44). */
object DemoHomeHi {
    val data = com.paisaflow.app.core.model.HiHomeData(
        businessName = "सुनीता डेयरी",
        blockChip = "शेखपुरा",
        liveLine = "कोष Live · सुरक्षित खाता",
        greeting = "नमस्ते सुनीता जी! 👋",
        dateLine = "आज: 24 अक्टूबर 2026 • शेखपुरा ब्लॉक",
        question = "आज व्यापार में क्या हिसाब देखना है?",
        heroTitle = "बोलिए और हिसाब दर्ज कीजिए",
        heroSubtitle = "\"कुंदन ने 500 रुपये दिए\" या \"आज का दूध लिखो\" — बस माइक दबाकर बोलें।",
        readyLine = "सुनने के लिए तैयार: \"रमेश का बकाया कितना है?\"",
        quickPrompts = listOf("💬 \"दूध का हिसाब लिखो\"", "⚡ \"लोन का रिस्क बताओ\"", "🥛 \"रमेश का बाकी\""),
        alertDays = "12 दिन बकाया",
        alertName = "रमेश टी स्टॉल",
        alertLine = "₹850 बाकी है · तगादा संदेश तैयार है",
        tiles = listOf(
            com.paisaflow.app.core.model.HiTile("हाथ में रोकड़ (Safe)", "₹1,00,000", "SBI शेखपुरा • सुरक्षित बचत", EvidenceLabel.FACT, com.paisaflow.app.core.model.MetricTone.PRIMARY, com.paisaflow.app.core.model.MetricIcon.PAYMENTS),
            com.paisaflow.app.core.model.HiTile("मासिक बिक्री (दूध)", "₹78,000", "↑ +8% पिछले महीने से", EvidenceLabel.FACT, com.paisaflow.app.core.model.MetricTone.NEUTRAL, com.paisaflow.app.core.model.MetricIcon.TRENDING_UP),
            com.paisaflow.app.core.model.HiTile("सुधा दाना चूरी स्टॉक", "14 दिन का", "⚠️ 3 दिन में ऑर्डर दें", EvidenceLabel.ESTIMATE, com.paisaflow.app.core.model.MetricTone.WARNING, com.paisaflow.app.core.model.MetricIcon.INVENTORY),
            com.paisaflow.app.core.model.HiTile("मार्केट का बाकी उधार", "₹18,500", "3 ग्राहक (रमेश, कुंदन, श्याम)", EvidenceLabel.FACT, com.paisaflow.app.core.model.MetricTone.SECONDARY, com.paisaflow.app.core.model.MetricIcon.PENDING),
        ),
        twinName = "सुनीता डेयरी डिजिटल ट्विन v1.2",
        healthLine = "व्यापार की सेहत: सुरक्षित",
        runwayText = "84 दिन (लगभग 3 महीने)",
        seasonTitle = "सर्दियों का मौसम अनुमान",
        seasonBody = "दिसंबर में हरे चारे के दाम 12% बढ़ सकते हैं। अभी अग्रिम स्टॉक का विकल्प देखें।",
        modelAccuracy = "99.2% मॉडल एक्यूरेसी",
        tasks = listOf(
            com.paisaflow.app.core.model.HiTask("रमेश टी स्टॉल को याद दिलाएं", "₹850 बकाया (12 दिन) • ड्राफ्ट तैयार", "भेजें", com.paisaflow.app.core.model.ActionKind.SEND, true),
            com.paisaflow.app.core.model.HiTask("कुंदन डेयरी का 18L दूध दर्ज करें", "सुबह का कोटा • ₹720 का हिसाब", "पक्का करें", com.paisaflow.app.core.model.ActionKind.CHECK, false),
            com.paisaflow.app.core.model.HiTask("शेखपुरा मंडी रेट चेक करें", "आज का भाव ₹41.50/L (+₹1.50 ज़्यादा)", "देखें", com.paisaflow.app.core.model.ActionKind.CALL, false),
        ),
        lastVoiceEntry = "\"श्याम ग्वाला से 2 बोरी खली ली\"",
    )
}

/** SAMPLE simulation result — same numbers the finance engine produces for the demo dairy (PRD §13). */
object DemoSimulation {
    val nineLakh = com.paisaflow.app.core.model.LoanSimResponse(
        question = "“अगर मैं ₹9,00,000 का लोन लूँ तो क्या होगा?”",
        emi = 19_568, totalInterest = 274_080, safeLimit = 450_000,
        baseSurplusAfterEmi = 12_432, winterSurplusAfterEmi = -9_408, winterDeficit3Months = 28_224,
        scenarios = listOf(
            com.paisaflow.app.core.model.ScenarioOut("base", "सामान्य स्थिति (Base Case)", null, "TIGHT", 78_000, 46_000, 32_000, 19_568, 12_432, 61),
            com.paisaflow.app.core.model.ScenarioOut("sales", "दूध भाव में -20% गिरावट", "Sales & Rate Shock", "DEFICIT", 62_400, 46_000, 16_400, 19_568, -3_168, 119, "डेयरी संकलन केंद्र द्वारा भाव घटाने पर"),
            com.paisaflow.app.core.model.ScenarioOut("cost", "चारा व दाना +15% महंगा", "Input Cost Shock", "TIGHT", 78_000, 52_900, 25_100, 19_568, 5_532, 78, "कमज़ोर सुरक्षा बफ़र"),
            com.paisaflow.app.core.model.ScenarioOut("winter", "सर्दियों की मंदी -28%", "Winter Lean Period (Dec-Feb)", "DEFICIT", 56_160, 46_000, 10_160, 19_568, -9_408, 193, "सर्दियों के 3 महीनों में कुल ₹28,224 की उधारी चढ़ने का जोखिम"),
        ),
        safePlan = com.paisaflow.app.core.model.SafePlanOut(450_000, 6, 9_784, 376, 29_352,
            "₹9,00,000 के बदले ₹4,50,000 का लोन लें (+6 गायें) और ₹29,352 (3 महीने की किस्त) का सुरक्षा बफ़र अलग खाते में रखें। इससे सर्दियों में भी ₹376 की शुद्ध बचत बनी रहेगी।"),
        note = "Yeh anumaan hai — aapke bataye aankdon aur assumptions par. Loan approval, munafa ya bhavishya ki guarantee nahi.",
    )
}

/** SAMPLE Action Layer content (RULES.md §44). Finance numbers come from DemoSimulation. */
object DemoActions {
    val data = com.paisaflow.app.core.model.ActionsData(
        pendingCount = 3,
        collection = com.paisaflow.app.core.model.CollectionAction(
            customer = "रमेश टी स्टॉल को तगादा",
            customerMeta = "12 सितम्बर की शाम दूध सप्लाई (42 लीटर)",
            address = "रमेश टी स्टॉल (स्टेशन रोड)",
            relationship = "दुकानदार से 8 महीने का भरोसेमंद सम्बंध",
            phone = "+919800000000",
            amount = 850, daysOverdue = 12,
            why = "पिछले 12 दिनों से भुगतान नहीं हुआ। आज याद दिलाने से कल सुबह का हरा चारा व खली ख़रीदने में नकदी की तंगी नहीं होगी।",
            draft = "नमस्ते रमेश जी, सुनीता डेयरी का ₹850 का पिछला बकाया चल रहा है। आज शाम तक UPI या नकद कराने की कृपा करें ताकि चारे का भुगतान हो सके। धन्यवाद!",
        ),
        reorder = com.paisaflow.app.core.model.ReorderAction(
            title = "सुधा पशु आहार स्टॉक री-ऑर्डर",
            supplier = "शेखपुरा एग्रो फीड्स प्राइवेट लिमिटेड",
            supplierPhone = "+919835000000",
            amount = 8_400, quantityLabel = "10 बोरी चूरी-खली",
            stockPercent = 22, daysLeft = 3.5, availableBags = 2, minSafeBags = 8,
            product = "सुधा स्पेशल गोल्ड (50kg x 10)",
            priceNote = "28 अक्टूबर से ₹120/बोरी बढ़ने का अनुमान",
            insight = "मंडी में खली की आवक घटने से दाम ₹120/बोरी बढ़ने वाले हैं। आज 10 बोरी बुक करने पर सीधे ₹1,200 की पक्की बचत होगी।",
            savings = 1_200,
        ),
        finance = com.paisaflow.app.core.model.FinanceAction(
            title = "₹9 लाख के बदले ₹4.5 लाख वाला सुरक्षित लोन चुनें",
            scheme = "बैंक डेयरी विस्तार योजना आवेदन",
            unsafeLoan = 900_000, unsafeEmi = DemoSimulation.nineLakh.emi,
            safeLoan = DemoSimulation.nineLakh.safePlan!!.loan, safeEmi = DemoSimulation.nineLakh.safePlan!!.emi,
            winterBuffer = DemoSimulation.nineLakh.safePlan!!.winterSurplusAfterEmi,
            insight = "छोटे लोन पर सर्दियों में दूध उत्पादन घटने पर भी बचत बनी रहेगी और 3 महीने की किस्त का आपातकालीन बफ़र पूरी तरह सुरक्षित रहेगा।",
        ),
    )
}
