package com.paisaflow.app.feature.voice.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.ContactSupport
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisaflow.app.core.model.ClarificationOption
import com.paisaflow.app.core.model.ClarificationQuestion
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfRadius
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/** One question at a time (RULES.md §3.2): presets, "Voice se bolein", quantity read-out. */
@Composable
fun ClarificationCard(
    question: ClarificationQuestion,
    selected: Int?,
    onSelect: (Int) -> Unit,
    onVoiceClick: () -> Unit,
    onPlayQuestion: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(PfRadius.Lg),
        color = PfColors.SurfaceContainerLowest,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(PfSpacing.Lg)) {
            QuestionHeader(question, onPlayQuestion)
            Spacer(Modifier.height(PfSpacing.Md))
            OptionGrid(question.options, selected, onSelect, onVoiceClick)
            Spacer(Modifier.height(PfSpacing.Md))
            QuantityPill(question.unitLabel, selected, onVoiceClick)
        }
    }
}

@Composable
private fun QuestionHeader(q: ClarificationQuestion, onPlay: () -> Unit) {
    Row(verticalAlignment = Alignment.Top) {
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.ContactSupport, null, tint = PfColors.Secondary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("SAWĀL · STEP ${q.step} OF ${q.totalSteps}", style = PfType.LabelSm.copy(letterSpacing = 1.sp), color = PfColors.Secondary)
            }
            Spacer(Modifier.height(4.dp))
            Text(q.question, style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold), color = PfColors.OnSurface)
            Spacer(Modifier.height(2.dp))
            Text(q.translation, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant)
        }
        Spacer(Modifier.width(PfSpacing.Sm))
        Surface(onClick = onPlay, shape = CircleShape, color = PfColors.SurfaceContainerLow) {
            Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.VolumeUp, "Question audio", tint = PfColors.Primary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun OptionGrid(options: List<ClarificationOption>, selected: Int?, onSelect: (Int) -> Unit, onVoice: () -> Unit) {
    // options + trailing voice tile, laid out two per row
    val cells: List<ClarificationOption?> = options + listOf(null)
    Column(verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
        cells.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                row.forEach { o ->
                    if (o == null) VoiceOptionTile(onVoice, Modifier.weight(1f))
                    else OptionTile(o, o.value == selected, { onSelect(o.value) }, Modifier.weight(1f))
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun OptionTile(option: ClarificationOption, active: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(PfRadius.Base),
        color = if (active) PfColors.PrimaryFixed else PfColors.SurfaceContainerLow,
        shadowElevation = if (active) 1.dp else 0.dp,
        modifier = modifier.defaultMinSize(minHeight = PfSpacing.TouchTarget),
    ) {
        Row(Modifier.padding(horizontal = PfSpacing.Md, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Pets, null, tint = PfColors.Primary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    option.label,
                    style = PfType.BodyLg.copy(fontWeight = FontWeight.Bold, lineHeight = 22.sp),
                    color = if (active) PfColors.OnPrimaryFixed else PfColors.OnSurface,
                )
                if (active && option.hint != null) {
                    Text(option.hint, style = PfType.LabelSm.copy(fontWeight = FontWeight.Medium), color = PfColors.OnPrimaryFixedVariant)
                }
            }
            Icon(
                if (active) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                null,
                tint = if (active) PfColors.Primary else PfColors.Outline,
                modifier = Modifier.size(if (active) 20.dp else 18.dp),
            )
        }
    }
}

@Composable
private fun VoiceOptionTile(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(PfRadius.Base),
        color = PfColors.SurfaceContainerLow,
        modifier = modifier.defaultMinSize(minHeight = PfSpacing.TouchTarget),
    ) {
        Row(
            Modifier.padding(horizontal = PfSpacing.Md, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(Icons.Default.Mic, null, tint = PfColors.Secondary, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
            Text("Voice se bolein", style = PfType.LabelMd, color = PfColors.Primary)
        }
    }
}

@Composable
private fun QuantityPill(unitLabel: String, selected: Int?, onMic: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = PfSpacing.TouchTarget)
            .background(PfColors.SurfaceContainerLow, RoundedCornerShape(PfRadius.Base))
            .padding(horizontal = PfSpacing.Md, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Default.EditNote, null, tint = PfColors.Outline)
        Spacer(Modifier.width(8.dp))
        Text(
            buildAnnotatedString {
                append("Sankhya (Quantity): ")
                withStyle(SpanStyle(color = PfColors.OnSurface, fontWeight = FontWeight.Bold)) {
                    append(if (selected == null) "Not selected" else "$selected $unitLabel selected")
                }
            },
            style = PfType.BodyMd.copy(fontWeight = FontWeight.Medium),
            color = PfColors.OnSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Surface(onClick = onMic, shape = CircleShape, color = PfColors.SurfaceContainerHighest) {
            Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Mic, "Mic se likhwayein", tint = PfColors.Primary, modifier = Modifier.size(20.dp))
            }
        }
    }
}
