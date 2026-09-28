package com.paisaflow.app.core.voice

import android.util.Base64
import com.paisaflow.app.core.network.ApiClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

@Serializable data class TranscribeRequest(@SerialName("audio_base64") val audioBase64: String, val language: String = "hi", @SerialName("sampling_rate") val samplingRate: Int = 16000)
@Serializable data class TranscribeResponse(val text: String, @SerialName("language_used") val languageUsed: String, @SerialName("is_sample_data") val isSampleData: Boolean = false)
@Serializable data class SynthesiseRequest(val text: String, val language: String = "hi", val gender: String = "female")
@Serializable data class SynthesiseResponse(@SerialName("audio_base64") val audioBase64: String? = null, @SerialName("language_used") val languageUsed: String, @SerialName("is_sample_data") val isSampleData: Boolean = false, val note: String? = null)
@Serializable data class TranslateRequest(val text: String, val source: String = "hi", val target: String = "en")
@Serializable data class TranslateResponse(val text: String, @SerialName("language_used") val languageUsed: String, @SerialName("is_sample_data") val isSampleData: Boolean = false)

interface SpeechApi {
    @POST("api/v1/speech/transcribe") suspend fun transcribe(@Body b: TranscribeRequest): TranscribeResponse
    @POST("api/v1/speech/synthesise") suspend fun synthesise(@Body b: SynthesiseRequest): SynthesiseResponse
    @POST("api/v1/speech/translate") suspend fun translate(@Body b: TranslateRequest): TranslateResponse
}

/** Speech + language via the PaisaFlow backend (which talks to Bhashini). */
object SpeechRepository {
    private val api: SpeechApi by lazy { ApiClient.retrofit.create(SpeechApi::class.java) }

    /** Supported UI language codes (Bhashini codes). */
    val languages = listOf("hi" to "हिंदी", "bho" to "भोजपुरी", "mag" to "मगही", "mai" to "मैथिली", "en" to "English")

    suspend fun transcribe(wav: ByteArray, language: String): Result<TranscribeResponse> = runCatching {
        api.transcribe(TranscribeRequest(Base64.encodeToString(wav, Base64.NO_WRAP), language))
    }

    suspend fun synthesise(text: String, language: String): Result<SynthesiseResponse> = runCatching {
        api.synthesise(SynthesiseRequest(text, language))
    }

    suspend fun translate(text: String, source: String, target: String): Result<TranslateResponse> = runCatching {
        api.translate(TranslateRequest(text, source, target))
    }
}
