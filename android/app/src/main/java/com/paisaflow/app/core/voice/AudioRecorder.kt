package com.paisaflow.app.core.voice

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Records 16 kHz mono 16-bit PCM and returns a WAV byte array — the format
 * Bhashini ASR expects. Caller must hold RECORD_AUDIO permission.
 */
class AudioRecorder(private val sampleRate: Int = 16_000) {
    private var recorder: AudioRecord? = null
    private var buffer = ByteArrayOutputStream()
    @Volatile private var recording = false
    private var thread: Thread? = null

    val isRecording get() = recording

    @SuppressLint("MissingPermission")
    fun start() {
        if (recording) return
        val minBuf = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        val rec = AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBuf * 2)
        buffer = ByteArrayOutputStream()
        recorder = rec
        recording = true
        rec.startRecording()
        thread = Thread {
            val chunk = ByteArray(minBuf)
            while (recording) {
                val n = rec.read(chunk, 0, chunk.size)
                if (n > 0) buffer.write(chunk, 0, n)
            }
        }.also { it.start() }
    }

    /** Stops and returns a complete WAV file (header + PCM). */
    fun stop(): ByteArray {
        recording = false
        thread?.join()
        recorder?.run { stop(); release() }
        recorder = null
        return wrapWav(buffer.toByteArray(), sampleRate)
    }

    private fun wrapWav(pcm: ByteArray, rate: Int): ByteArray {
        val channels = 1; val bits = 16
        val byteRate = rate * channels * bits / 8
        val header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN)
        header.put("RIFF".toByteArray()).putInt(36 + pcm.size).put("WAVE".toByteArray())
        header.put("fmt ".toByteArray()).putInt(16).putShort(1).putShort(channels.toShort())
        header.putInt(rate).putInt(byteRate).putShort((channels * bits / 8).toShort()).putShort(bits.toShort())
        header.put("data".toByteArray()).putInt(pcm.size)
        return header.array() + pcm
    }
}
