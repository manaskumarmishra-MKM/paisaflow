package com.paisaflow.app.core.i18n

import com.paisaflow.app.core.model.ActionsData
import com.paisaflow.app.core.model.CollectionAction
import com.paisaflow.app.core.model.FinanceAction
import com.paisaflow.app.core.model.ReorderAction

/**
 * SAMPLE content for the Action Layer, per language (RULES.md §44 — fictional demo).
 * Finance numbers match the finance engine's demo run (EMI ₹19,568 on ₹9L; safe plan ₹4.5L → EMI ₹9,784).
 * BHO / MAG / MAI reuse Hindi content until native-speaker copy is supplied.
 */
object ActionsContent {
    private val HI = ActionsData(
        pendingCount = 3, audioDuration = "1:40 मिनट",
        collection = CollectionAction(
            title = "रमेश टी स्टॉल को तगादा", place = "शेखपुरा चौक, निकट बस स्टैंड", amount = "₹850",
            context = "12 सितम्बर शाम का 42 लीटर दूध का भुगतान लंबित है।",
            why = "आज शाम तक यह मिलने पर कल सुबह चूरी-खली व हरा चारा नकद ले पाएँगी, साहूकार से नया कर्ज़ नहीं लेना पड़ेगा।",
            customer = "रमेश टी स्टॉल (स्टेशन रोड)", relationship = "दुकानदार से 8 महीने का भरोसेमंद सम्बंध", phone = "+919800000000",
            draft = "नमस्ते रमेश जी, सुनीता डेयरी का ₹850 का पिछला बकाया चल रहा है। कृपया आज शाम तक UPI या नकद कराने की कृपा करें ताकि पशु आहार खरीदा जा सके। धन्यवाद!",
        ),
        reorder = ReorderAction(
            title = "सुधा पशु आहार स्टॉक री-ऑर्डर", supplier = "शेखपुरा फीड्स डिपो • 10 बोरी चूरी-खली", amount = "₹8,400", saving = "₹1,200 की बचत",
            stockPct = 22, stockDays = 3.5, availableBags = 2, minBags = 8,
            product = "सुधा स्पेशल गोल्ड (50kg x 10)", priceNote = "28 अक्टूबर से ₹120/बोरी बढ़ने का अनुमान",
            insight = "मंडी में आवक घटने से 28 अक्टूबर से ₹120/बोरी भाव बढ़ने का अनुमान है। आज 10 बोरी बुक करने पर सीधे ₹1,200 की पक्की बचत होगी।",
            phone = "+919876543210",
        ),
        finance = FinanceAction(
            title = "₹9 लाख के बदले ₹4.5 लाख वाला सुरक्षित लोन चुनें", scheme = "भैंस विस्तार योजना (शेखपुरा ग्रामीण बैंक प्रस्ताव)",
            unsafeLabel = "₹9 लाख लोन (जोखिम भरा)", unsafeEmi = "EMI ₹19,568",
            unsafeNote = "सर्दियों में दूध उत्पादन घटने पर -₹9,408/माह नकद घाटा और बैंक डिफ़ॉल्ट का गंभीर खतरा।",
            safeLabel = "₹4.5 लाख लोन (मुनीम अनुशंसित)", safeEmi = "EMI ₹9,784",
            safeNote = "मंदे मौसम में भी बचत बनी रहे + हाथ में ₹29,352 सुरक्षा बफ़र (3 किस्त) हमेशा सुरक्षित।",
        ),
    )

    private val HINGLISH = HI.copy(
        audioDuration = "1:40 min",
        collection = HI.collection.copy(
            title = "Ramesh Tea Stall ko taqada", place = "Sheikhpura Chowk, bus stand ke paas",
            context = "12 September shaam ka 42 litre doodh ka payment pending hai.",
            why = "Aaj shaam tak mil jaye toh kal subah churi-khali aur hara chara nakad le paayengi, sahukar se naya karz nahi lena padega.",
            customer = "Ramesh Tea Stall (Station Road)", relationship = "8 mahine ka bharosemand sambandh",
            draft = "Namaste Ramesh ji, Sunita Dairy ka ₹850 ka pichhla baaki chal raha hai. Kripya aaj shaam tak UPI ya nakad de dein taaki pashu aahar khareeda ja sake. Dhanyavaad!",
        ),
        reorder = HI.reorder.copy(
            title = "Sudha pashu aahar stock re-order", supplier = "Sheikhpura Feeds Depot • 10 bori churi-khali", saving = "₹1,200 ki bachat",
            product = "Sudha Special Gold (50kg x 10)", priceNote = "28 October se ₹120/bori badhne ka anumaan",
            insight = "Mandi mein aavak ghatne se 28 October se ₹120/bori bhaav badhne ka anumaan hai. Aaj 10 bori book karne par seedhe ₹1,200 ki pakki bachat hogi.",
        ),
        finance = HI.finance.copy(
            title = "₹9 lakh ke badle ₹4.5 lakh wala surakshit loan chunein", scheme = "Bhains vistaar yojana (Sheikhpura Gramin Bank prastav)",
            unsafeLabel = "₹9 lakh loan (jokhim bhara)", unsafeNote = "Sardi mein doodh ghatne par -₹9,408/maah nakad ghata aur bank default ka gambhir khatra.",
            safeLabel = "₹4.5 lakh loan (Munim ki salah)", safeNote = "Mande mausam mein bhi bachat bani rahe + haath mein ₹29,352 suraksha buffer (3 kist) hamesha surakshit.",
        ),
    )

    private val EN = HI.copy(
        audioDuration = "1:40 min",
        collection = HI.collection.copy(
            title = "Collect from Ramesh Tea Stall", place = "Sheikhpura Chowk, near bus stand",
            context = "Payment for 42 litres of milk supplied on the evening of 12 September is pending.",
            why = "If it comes in by tonight, you can buy churi-khali and green fodder for cash tomorrow morning instead of borrowing from the moneylender.",
            customer = "Ramesh Tea Stall (Station Road)", relationship = "8-month trusted relationship",
            draft = "Namaste Ramesh ji, Sunita Dairy's earlier dues of ₹850 are pending. Kindly pay by UPI or cash by this evening so cattle feed can be bought. Thank you!",
        ),
        reorder = HI.reorder.copy(
            title = "Re-order Sudha cattle feed stock", supplier = "Sheikhpura Feeds Depot • 10 bags churi-khali", saving = "Saves ₹1,200",
            product = "Sudha Special Gold (50kg x 10)", priceNote = "Expected to rise ₹120/bag from 28 October",
            insight = "With lower arrivals in the mandi, prices are expected to rise ₹120/bag from 28 October. Booking 10 bags today saves a sure ₹1,200.",
        ),
        finance = HI.finance.copy(
            title = "Choose the safe ₹4.5 lakh loan instead of ₹9 lakh", scheme = "Buffalo expansion plan (Sheikhpura Gramin Bank proposal)",
            unsafeLabel = "₹9 lakh loan (risky)", unsafeNote = "When winter milk output drops: −₹9,408/month cash deficit and a serious risk of bank default.",
            safeLabel = "₹4.5 lakh loan (Munim recommended)", safeNote = "Savings hold even in the lean season + a ₹29,352 safety buffer (3 instalments) always in hand.",
        ),
    )

    fun forLang(lang: AppLanguage): ActionsData = when (lang) {
        AppLanguage.HINGLISH -> HINGLISH
        AppLanguage.EN -> EN
        else -> HI   // BHO / MAG / MAI: Hindi content until reviewed copy is available
    }
}
