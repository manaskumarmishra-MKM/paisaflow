package com.paisaflow.app.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Evidence classification on every important value (RULES.md §6.2, §10.1). */
@Serializable
enum class EvidenceLabel(val display: String) {
    @SerialName("FACT") FACT("FACT"),
    @SerialName("OBSERVATION") OBSERVATION("OBSERVATION"),
    @SerialName("ESTIMATE") ESTIMATE("ESTIMATE"),
    @SerialName("ASSUMPTION") ASSUMPTION("ASSUMPTION"),
    @SerialName("AI_INFERENCE") AI_INFERENCE("AI INFERENCE"),
}

/** One entity the Brain extracted, shown as a chip on the "Maine samjha" card. */
@Serializable
data class ExtractedFact(
    val label: String,
    val value: String,
    val subtitle: String? = null,
    val evidence: EvidenceLabel,
    @SerialName("is_highlight") val isHighlight: Boolean = false,
)

@Serializable
data class ClarificationOption(
    val value: Int,
    val label: String,
    val hint: String? = null,
)

/** One follow-up question — one at a time (RULES.md §3.2). */
@Serializable
data class ClarificationQuestion(
    val step: Int,
    @SerialName("total_steps") val totalSteps: Int,
    val question: String,
    val translation: String,
    @SerialName("unit_label") val unitLabel: String,
    val preselected: Int? = null,
    val options: List<ClarificationOption>,
)

/** Contract returned by POST /api/v1/conversation/message. */
@Serializable
data class BrainUnderstanding(
    val transcript: String,
    val facts: List<ExtractedFact>,
    @SerialName("read_back") val readBack: String,
    @SerialName("next_question") val nextQuestion: ClarificationQuestion,
    @SerialName("onboarding_step") val onboardingStep: Int,
    @SerialName("onboarding_total") val onboardingTotal: Int,
    @SerialName("is_sample_data") val isSampleData: Boolean = false,
)

@Serializable
data class MessageRequest(
    val text: String,
    val language: String = "hinglish",
    @SerialName("conversation_id") val conversationId: String? = null,
)

/** Voice interaction states (DESIGN.md §12, RULES.md §24). Always visible in the UI. */
enum class VoiceState(val bannerText: String) {
    IDLE("Tap karke boliye"),
    LISTENING("Sun rahe hain... (Listening)"),
    PROCESSING("Aapki baat samajh raha hoon..."),
    UNDERSTANDING("Business data check kar raha hoon..."),
    RESPONDING("Result ko simple bana raha hoon..."),
    ERROR("Kuch problem aa gayi. Dobara try karein.");

    val showsWaveform get() = this == LISTENING
}
