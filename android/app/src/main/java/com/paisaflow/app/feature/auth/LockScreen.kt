package com.paisaflow.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.paisaflow.app.shared.PillButton
import com.paisaflow.app.shared.PillVariant
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/**
 * Shown until the owner unlocks with fingerprint / PIN.
 * [biometricsAvailable] = false → offers a plain "Continue" (device has no lock set),
 * which is stated honestly on screen rather than pretending to be secure.
 */
@Composable
fun LockScreen(
    biometricsAvailable: Boolean,
    errorMessage: String?,
    onUnlock: () -> Unit,
    onContinueWithoutLock: () -> Unit,
) {
    Box(Modifier.fillMaxSize().background(PfColors.Surface), contentAlignment = Alignment.Center) {
        Column(Modifier.padding(PfSpacing.Lg), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(72.dp).background(PfColors.Primary, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Payments, null, tint = PfColors.OnPrimary, modifier = Modifier.size(36.dp))
            }
            Spacer(Modifier.height(PfSpacing.Md))
            Text("PaisaFlow", style = PfType.HeadlineMd, color = PfColors.Primary)
            Text("Aapka business. Sirf aapke liye.", style = PfType.BodyMd, color = PfColors.OnSurfaceVariant)
            Spacer(Modifier.height(PfSpacing.Xl))

            Box(Modifier.size(120.dp).background(PfColors.PrimaryFixed, CircleShape), contentAlignment = Alignment.Center) {
                Icon(
                    if (biometricsAvailable) Icons.Default.Fingerprint else Icons.Default.Lock,
                    null, tint = PfColors.Primary, modifier = Modifier.size(64.dp),
                )
            }
            Spacer(Modifier.height(PfSpacing.Md))
            Text(
                if (biometricsAvailable) "Ungli lagayein ya PIN daalein" else "Phone par lock set nahi hai",
                style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold), color = PfColors.OnSurface, textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(PfSpacing.Xs))
            Text(
                if (biometricsAvailable) "Aapka khata sirf aap khol sakte hain."
                else "Suraksha ke liye phone settings mein fingerprint ya PIN set karein.",
                style = PfType.BodyMd, color = PfColors.OnSurfaceVariant, textAlign = TextAlign.Center,
            )
            if (errorMessage != null) {
                Spacer(Modifier.height(PfSpacing.Sm))
                Text(errorMessage, style = PfType.LabelMd, color = PfColors.Error, textAlign = TextAlign.Center)
            }
            Spacer(Modifier.height(PfSpacing.Lg))
            if (biometricsAvailable) {
                PillButton(
                    label = "Fingerprint se kholein", onClick = onUnlock,
                    leadingIcon = Icons.Default.Fingerprint, elevated = true, modifier = Modifier.fillMaxWidth(),
                )
            } else {
                PillButton(
                    label = "Bina lock ke aage badhein", onClick = onContinueWithoutLock,
                    variant = PillVariant.NEUTRAL, modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
