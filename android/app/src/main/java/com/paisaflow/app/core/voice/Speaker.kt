package com.paisaflow.app.core.voice

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.util.Base64
import java.io.File
import java.util.Locale

/**
 * Plays Bhashini TTS audio when the backend returns it; otherwise falls back
 * to the device's own TextToSpeech in Hindi so explanations are always audible.
 */
class Speaker(context: Context) {
    private val appContext = context.applicationContext
    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private var player: MediaPlayer? = null

    init {
        tts = TextToSpeech(appContext) { status ->
            ttsReady = status == TextToSpeech.SUCCESS
            if (ttsReady) tts?.language = Locale("hi", "IN")
        }
    }

    fun playBase64Wav(audioBase64: String) {
        stop()
        val bytes = Base64.decode(audioBase64, Base64.DEFAULT)
        val f = File.createTempFile("pf_tts_", ".wav", appContext.cacheDir).apply { writeBytes(bytes); deleteOnExit() }
        player = MediaPlayer().apply {
            setDataSource(f.absolutePath)
            setOnCompletionListener { it.release(); f.delete() }
            prepare(); start()
        }
    }

    fun speakFallback(text: String) {
        stop()
        if (ttsReady) tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "pf-${text.hashCode()}")
    }

    fun stop() {
        player?.runCatching { if (isPlaying) stop(); release() }
        player = null
        tts?.stop()
    }

    fun release() { stop(); tts?.shutdown() }
}
