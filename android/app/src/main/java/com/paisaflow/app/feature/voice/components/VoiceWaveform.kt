package com.paisaflow.app.feature.voice.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.paisaflow.app.ui.theme.PfColors
import kotlin.math.abs

/** Five bouncing bars — the "live audio" cue inside the listening banner. */
@Composable
fun VoiceWaveform(color: Color = PfColors.Primary) {
    val heights = listOf(12f, 16f, 8f, 16f, 12f)
    val delays = listOf(0.1f, 0.25f, 0.05f, 0.3f, 0.15f)
    val t = rememberInfiniteTransition(label = "wave")
    val phase by t.animateFloat(
        0f, 1f, infiniteRepeatable(tween(900, easing = LinearEasing)), label = "phase",
    )
    Row(Modifier.height(16.dp), verticalAlignment = Alignment.CenterVertically) {
        heights.forEachIndexed { i, h ->
            val local = (phase + delays[i]) % 1f
            val bounce = 1f - abs(local * 2f - 1f) // triangle wave 0→1→0
            Box(
                Modifier
                    .padding(horizontal = 1.dp)
                    .width(4.dp)
                    .height((h * (0.55f + 0.45f * bounce)).dp)
                    .background(color, RoundedCornerShape(2.dp)),
            )
        }
    }
}
