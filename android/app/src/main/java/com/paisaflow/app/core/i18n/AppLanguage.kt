package com.paisaflow.app.core.i18n

import androidx.compose.runtime.compositionLocalOf

/**
 * UI languages. [bhashini] is the code sent to Bhashini for ASR/TTS
 * (Hinglish is spoken Hindi, so it maps to "hi").
 */
enum class AppLanguage(val code: String, val native: String, val short: String, val bhashini: String) {
    HI("hi", "हिंदी", "हिं", "hi"),
    HINGLISH("hinglish", "Hinglish", "Hin", "hi"),
    EN("en", "English", "EN", "en"),
    BHO("bho", "भोजपुरी", "भो", "bho"),
    MAG("mag", "मगही", "मग", "mag"),
    MAI("mai", "मैथिली", "मै", "mai");

    fun next(): AppLanguage = entries[(ordinal + 1) % entries.size]

    companion object {
        fun fromCode(code: String): AppLanguage = entries.firstOrNull { it.code == code || it.bhashini == code } ?: HI
    }
}

/** Current UI language, provided at the app root. */
val LocalAppLanguage = compositionLocalOf { AppLanguage.HI }
