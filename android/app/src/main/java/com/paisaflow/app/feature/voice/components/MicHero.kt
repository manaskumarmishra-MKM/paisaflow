package com.paisaflow.app.feature.voice.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisaflow.app.core.model.VoiceState
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfRadius
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/** Hero voice zone: status banner, 88dp pulsating mic, "बोलिए" label, utterance bubble. */
@Composable
fun MicHero(
    voiceState: VoiceState,
    transcript: String?,
    onMicClick: () -> Unit,
    onReplayClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(PfRadius.Lg),
        color = PfColors.SurfaceContainerLowest,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(PfSpacing.Lg), horizontalAlignment = Alignment.CenterHorizontally) {
            StatusBanner(voiceState)
            Spacer(Modifier.height(PfSpacing.Md))
            PulsingMic(active = voiceState == VoiceState.LISTENING, onClick = onMicClick)
            Spacer(Modifier.height(PfSpacing.Sm))
            Text("बोलिए (Boliye)", style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold), color = PfColors.Primary)
            Spacer(Modifier.height(2.dp))
            Text("Tap karke khul ke baat karein", style = PfType.BodyMd, color = PfColors.OnSurfaceVariant)
            if (transcript != null) {
                Spacer(Modifier.height(PfSpacing.Md))
                UtteranceBubble(transcript, onReplayClick)
            }
        }
    }
}

@Composable
private fun StatusBanner(state: VoiceState) {
    val isError = state == VoiceState.ERROR
    Surface(
        shape = CircleShape,
        color = if (isError) PfColors.ErrorContainer else PfColors.PrimaryFixed,
        shadowElevation = 1.dp,
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            if (!isError) {
                PingDot()
                Spacer(Modifier.width(PfSpacing.Sm))
            }
            Text(
                state.bannerText,
                style = PfType.LabelMd.copy(letterSpacing = (-0.2).sp),
                color = if (isError) PfColors.OnErrorContainer else PfColors.OnPrimaryFixed,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
            if (state.showsWaveform) {
                Spacer(Modifier.width(8.dp))
                VoiceWaveform()
            }
        }
    }
}

@Composable
private fun PingDot() {
    val t = rememberInfiniteTransition(label = "ping")
    val p by t.animateFloat(0f, 1f, infiniteRepeatable(tween(1000, easing = LinearEasing)), label = "p")
    Box(Modifier.size(14.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.size(10.dp).scale(1f + 0.6f * p).alpha(1f - p).background(PfColors.Primary, CircleShape))
        Box(Modifier.size(10.dp).background(PfColors.Primary, CircleShape))
    }
}

@Composable
private fun PulsingMic(active: Boolean, onClick: () -> Unit) {
    val t = rememberInfiniteTransition(label = "mic")
    val p by t.animateFloat(0f, 1f, infiniteRepeatable(tween(2400, easing = LinearEasing)), label = "p")
    Box(Modifier.size(140.dp), contentAlignment = Alignment.Center) {
        if (active) {
            Box(
                Modifier.size(128.dp).scale(0.8f + 0.3f * p).alpha(0.7f * (1f - p))
                    .background(PfColors.PrimaryFixed, CircleShape),
            )
        }
        Box(Modifier.size(112.dp).background(PfColors.PrimaryFixedDim.copy(alpha = 0.5f), CircleShape))
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = PfColors.Primary,
            shadowElevation = 10.dp,
            modifier = Modifier.size(88.dp).semantics { contentDescription = "Boliye - Tap to talk" },
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Mic, null, tint = PfColors.OnPrimary, modifier = Modifier.size(42.dp))
            }
        }
    }
}

@Composable
private fun UtteranceBubble(text: String, onReplay: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(PfRadius.Base),
        color = PfColors.SurfaceContainerLow,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(PfSpacing.Md), verticalAlignment = Alignment.Top) {
            Box(
                Modifier.padding(top = 2.dp).size(32.dp).background(PfColors.SecondaryFixed, CircleShape),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.Default.RecordVoiceOver, null, tint = PfColors.OnSecondaryFixed, modifier = Modifier.size(20.dp)) }
            Spacer(Modifier.width(PfSpacing.Sm))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Aapne abhi kaha:", style = PfType.LabelSm.copy(fontWeight = FontWeight.Medium), color = PfColors.OnSurfaceVariant, modifier = Modifier.weight(1f))
                    Surface(onClick = onReplay, shape = CircleShape, color = PfColors.SurfaceContainerHigh) {
                        Box(Modifier.size(28.dp), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.VolumeUp, "Sunen dobara", tint = PfColors.Primary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(text, style = PfType.BodyLg.copy(fontWeight = FontWeight.SemiBold, lineHeight = 24.sp), color = PfColors.OnSurface)
            }
        }
    }
}
