package com.paisaflow.app.core.network

import com.paisaflow.app.BuildConfig
import com.paisaflow.app.core.mock.DemoData
import com.paisaflow.app.core.model.BrainUnderstanding
import com.paisaflow.app.core.model.MessageRequest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

/** Versioned API (RULES.md §16). */
interface PaisaFlowApi {
    @POST("api/v1/conversation/message")
    suspend fun sendMessage(@Body body: MessageRequest): BrainUnderstanding

    @POST("api/v1/simulation/loan")
    suspend fun simulateLoan(@Body body: com.paisaflow.app.core.model.LoanSimRequest): com.paisaflow.app.core.model.LoanSimResponse
}

/**
 * Thin Retrofit client. Falls back to sample data when the backend is not
 * reachable so the screen can be reviewed without running the server.
 */
object ApiClient {
    private val json = Json { ignoreUnknownKeys = true }

    private val http = OkHttpClient.Builder()
        .connectTimeout(4, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)  // ASR/TTS round-trips take longer than JSON calls
        // Body-level logging is off by default — never log transcripts/amounts (RULES.md §33)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(http)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val api: PaisaFlowApi = retrofit.create(PaisaFlowApi::class.java)

    /** Returns the Brain's understanding, or sample data on any failure. */
    suspend fun sendUtterance(text: String, language: String = "hinglish"): BrainUnderstanding =
        runCatching { api.sendMessage(MessageRequest(text = text, language = language)) }
            .getOrElse { DemoData.dairyExpansion }
}
