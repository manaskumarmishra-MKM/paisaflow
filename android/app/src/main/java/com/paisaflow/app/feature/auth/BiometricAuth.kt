package com.paisaflow.app.feature.auth

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/** Result of an unlock attempt, in words the UI can show. */
sealed interface AuthResult {
    data object Success : AuthResult
    data class Failed(val message: String) : AuthResult
    data object Unavailable : AuthResult
}

/**
 * Fingerprint / face / device-PIN unlock via AndroidX BiometricPrompt.
 * Allows BIOMETRIC_WEAK | DEVICE_CREDENTIAL so users without a registered
 * fingerprint can still unlock with their phone PIN (no-shame fallback).
 */
class BiometricAuth(private val activity: FragmentActivity) {

    private val allowed = BIOMETRIC_STRONG or BIOMETRIC_WEAK or DEVICE_CREDENTIAL

    fun canAuthenticate(): Boolean =
        BiometricManager.from(activity).canAuthenticate(allowed) == BiometricManager.BIOMETRIC_SUCCESS

    fun prompt(onResult: (AuthResult) -> Unit) {
        if (!canAuthenticate()) {
            onResult(AuthResult.Unavailable)
            return
        }
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) =
                onResult(AuthResult.Success)

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                // User cancelled or hardware error — never show raw codes (RULES.md §26)
                val msg = when (errorCode) {
                    BiometricPrompt.ERROR_USER_CANCELED,
                    BiometricPrompt.ERROR_NEGATIVE_BUTTON,
                    BiometricPrompt.ERROR_CANCELED -> "Unlock cancel ho gaya."
                    BiometricPrompt.ERROR_LOCKOUT,
                    BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> "Bahut baar galat. Thodi der baad try karein."
                    else -> "Kuch problem aa gayi. Dobara try karein."
                }
                onResult(AuthResult.Failed(msg))
            }

            override fun onAuthenticationFailed() {
                // A single non-matching fingerprint; the prompt stays open, no action needed.
            }
        }
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("PaisaFlow kholein")
            .setSubtitle("Ungli lagayein ya phone ka PIN daalein")
            .setAllowedAuthenticators(allowed)
            .setConfirmationRequired(false)
            .build()
        BiometricPrompt(activity, executor, callback).authenticate(info)
    }
}
