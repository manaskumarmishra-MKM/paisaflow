package com.paisaflow.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import com.paisaflow.app.feature.auth.AuthResult
import com.paisaflow.app.feature.auth.BiometricAuth
import com.paisaflow.app.feature.auth.LockScreen
import com.paisaflow.app.ui.theme.PaisaFlowTheme

/** FragmentActivity (not ComponentActivity) because BiometricPrompt requires it. */
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaisaFlowTheme {
                val auth = remember { BiometricAuth(this) }
                var unlocked by rememberSaveable { mutableStateOf(false) }
                var error by remember { mutableStateOf<String?>(null) }
                val available = remember { auth.canAuthenticate() }

                fun tryUnlock() = auth.prompt { r ->
                    when (r) {
                        AuthResult.Success -> { unlocked = true; error = null }
                        is AuthResult.Failed -> error = r.message
                        AuthResult.Unavailable -> error = null
                    }
                }

                // Prompt automatically on first launch when biometrics exist.
                LaunchedEffect(Unit) { if (!unlocked && available) tryUnlock() }

                if (unlocked) {
                    PaisaFlowApp()
                } else {
                    LockScreen(
                        biometricsAvailable = available,
                        errorMessage = error,
                        onUnlock = ::tryUnlock,
                        onContinueWithoutLock = { unlocked = true },
                    )
                }
            }
        }
    }
}
