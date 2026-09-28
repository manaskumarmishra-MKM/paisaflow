package com.paisaflow.app.feature.voice.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.paisaflow.app.shared.PillButton
import com.paisaflow.app.shared.PillVariant
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/** Thumb-reach actions: confirm, "Badalna hai" (RULES.md §4.7), "Samajh nahi aaya?" (DESIGN.md §28). */
@Composable
fun ActionZone(onConfirm: () -> Unit, onChange: () -> Unit, onExplain: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PillButton(
            label = "Aage badhein (Confirm & Next)",
            onClick = onConfirm,
            trailingIcon = Icons.AutoMirrored.Filled.ArrowForward,
            elevated = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(PfSpacing.Sm))
        Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            PillButton(
                label = "Badalna hai", onClick = onChange, variant = PillVariant.NEUTRAL,
                leadingIcon = Icons.AutoMirrored.Filled.Undo, modifier = Modifier.weight(1f),
            )
            PillButton(
                label = "Samajh nahi aaya?", onClick = onExplain, variant = PillVariant.SECONDARY,
                leadingIcon = Icons.AutoMirrored.Filled.HelpOutline, modifier = Modifier.weight(1f),
            )
        }
        Spacer(Modifier.height(PfSpacing.Sm))
        Text(
            "💡 Koi bhi button samajh na aaye toh microphone par boliye.",
            style = PfType.LabelSm.copy(fontWeight = FontWeight.Medium),
            color = PfColors.OnSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
