package com.paisaflow.app.feature.voice.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/** Reassuring contextual pill: tagline + step counter. */
@Composable
fun ContextPill(step: Int, totalSteps: Int, tagline: String = "Suno, Samjho, Aage Badho · 100% Surakshit") {
    Surface(shape = CircleShape, color = PfColors.SurfaceContainerLow, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(horizontal = PfSpacing.Md, vertical = PfSpacing.Sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.foundation.layout.Box(Modifier.size(10.dp).background(PfColors.SurfaceTint, CircleShape))
            Spacer(Modifier.width(PfSpacing.Xs))
            Text(
                tagline, style = PfType.LabelSm, color = PfColors.OnSurfaceVariant,
                maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f),
            )
            Row(
                Modifier.background(PfColors.PrimaryFixed, CircleShape).padding(horizontal = 10.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Verified, null, tint = PfColors.OnPrimaryFixed, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(4.dp))
                Text("Step $step/$totalSteps", style = PfType.LabelSm, color = PfColors.OnPrimaryFixed)
            }
        }
    }
}
