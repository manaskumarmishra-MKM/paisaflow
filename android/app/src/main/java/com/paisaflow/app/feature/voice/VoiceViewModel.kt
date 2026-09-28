package com.paisaflow.app.feature.voice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.paisaflow.app.core.mock.DemoData
import com.paisaflow.app.core.model.BrainUnderstanding
import com.paisaflow.app.core.model.VoiceState
import com.paisaflow.app.core.network.ApiClient
import com.paisaflow.app.core.voice.AudioRecorder
import com.paisaflow.app.core.voice.Speaker
import com.paisaflow.app.core.voice.SpeechRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class VoiceUiState(
    val voiceState: VoiceState = VoiceState.IDLE,
    val understanding: BrainUnderstanding? = DemoData.dairyExpansion,
    val selectedOption: Int? = DemoData.dairyExpansion.nextQuestion.preselected,
    val usingSampleData: Boolean = true,
    val language: String = "hi",          // Bhashini code: hi · bho · mag · mai · en
    val message: String? = null,          // one-off toast
)

/**
 * Voice conversation state. Mic → AudioRecorder → /speech/transcribe (Bhashini ASR)
 * → /conversation/message (Brain) → read-back via /speech/synthesise (Bhashini TTS,
 * device TTS fallback). Starts on the demo utterance so the screen matches the mockup.
 */
class VoiceViewModel(app: Application) : AndroidViewModel(app) {
    private val _state = MutableStateFlow(VoiceUiState())
    val state: StateFlow<VoiceUiState> = _state

    private val recorder = AudioRecorder()
    private val speaker = Speaker(app)

    /** Tap once to start listening, tap again to stop and transcribe. */
    fun onMicTap() {
        if (recorder.isRecording) stopAndTranscribe() else startListening()
    }

    fun startListening() {
        runCatching { recorder.start() }
            .onSuccess { _state.update { it.copy(voiceState = VoiceState.LISTENING) } }
            .onFailure { _state.update { it.copy(voiceState = VoiceState.ERROR, message = "Mic chalu nahi hua. Type karke batayein.") } }
    }

    private fun stopAndTranscribe() {
        val wav = recorder.stop()
        _state.update { it.copy(voiceState = VoiceState.PROCESSING) }
        viewModelScope.launch {
            val lang = _state.value.language
            val res = withContext(Dispatchers.IO) { SpeechRepository.transcribe(wav, lang) }
            res.onSuccess { t ->
                if (t.text.isBlank()) {
                    _state.update { it.copy(voiceState = VoiceState.ERROR, message = "Kuch sunai nahi diya. Dobara boliye.") }
                } else submit(t.text)
            }.onFailure {
                _state.update { it.copy(voiceState = VoiceState.ERROR, message = "Awaaz service abhi uplabdh nahi. Type karke batayein.") }
            }
        }
    }

    fun submit(transcript: String) {
        _state.update { it.copy(voiceState = VoiceState.UNDERSTANDING) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { ApiClient.sendUtterance(transcript, _state.value.language) }
            _state.update {
                it.copy(
                    voiceState = VoiceState.RESPONDING,
                    understanding = result,
                    selectedOption = result.nextQuestion.preselected,
                    usingSampleData = result.isSampleData,
                )
            }
            speak(result.readBack)   // FR-V1: read back what was captured
            _state.update { it.copy(voiceState = VoiceState.IDLE) }
        }
    }

    /** Bhashini TTS via backend; device TTS if audio is unavailable. */
    fun speak(text: String) {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) { SpeechRepository.synthesise(text, _state.value.language) }
            val audio = res.getOrNull()?.audioBase64
            if (audio != null) runCatching { speaker.playBase64Wav(audio) }.onFailure { speaker.speakFallback(text) }
            else speaker.speakFallback(text)
        }
    }

    fun setLanguage(code: String) = _state.update { it.copy(language = code) }
    fun selectOption(value: Int) = _state.update { it.copy(selectedOption = value) }
    fun setVoiceState(s: VoiceState) = _state.update { it.copy(voiceState = s) }
    fun messageShown() = _state.update { it.copy(message = null) }

    override fun onCleared() {
        if (recorder.isRecording) recorder.stop()
        speaker.release()
    }
}
