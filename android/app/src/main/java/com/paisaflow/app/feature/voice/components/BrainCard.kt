package com.paisaflow.app.feature.voice.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.model.EvidenceLabel
import com.paisaflow.app.core.model.ExtractedFact
import com.paisaflow.app.shared.EvidenceChip
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfRadius
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/** "PaisaFlow Brain" card: 2×2 fact bento with evidence chips + read-back strip (FR-V1). */
@Composable
fun BrainCard(facts: List<ExtractedFact>, readBack: String, onPlayReadBack: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(PfRadius.Lg),
        color = PfColors.SurfaceContainerLowest,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(PfSpacing.Lg)) {
            Header()
            Spacer(Modifier.height(PfSpacing.Md))
            FactGrid(facts)
            Spacer(Modifier.height(PfSpacing.Md))
            ReadBackStrip(readBack, onPlayReadBack)
        }
    }
}

@Composable
private fun Header() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(32.dp).background(PfColors.PrimaryFixed, CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Psychology, null, tint = PfColors.OnPrimaryFixed, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text("PaisaFlow Brain", style = PfType.HeadlineSm, color = PfColors.Primary)
            Text("Samjha gaya · Structured Inferences", style = PfType.LabelSm.copy(fontWeight = FontWeight.Medium), color = PfColors.OnSurfaceVariant)
        }
        Row(
            Modifier.background(PfColors.PrimaryFixedDim, CircleShape).padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.CheckCircle, null, tint = PfColors.OnPrimaryFixed, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("Pukka Record", style = PfType.LabelSm, color = PfColors.OnPrimaryFixed)
        }
    }
}

@Composable
private fun FactGrid(facts: List<ExtractedFact>) {
    Column(verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
        facts.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                row.forEach { FactTile(it, Modifier.weight(1f)) }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun FactTile(fact: ExtractedFact, modifier: Modifier = Modifier) {
    val isEstimate = fact.evidence == EvidenceLabel.ESTIMATE
    val bg = if (isEstimate) PfColors.SecondaryFixed else PfColors.SurfaceContainerLow
    val labelColor = if (isEstimate) PfColors.OnSecondaryFixedVariant else PfColors.OnSurfaceVariant
    val valueColor = when {
        isEstimate -> PfColors.OnSecondaryFixed
        fact.isHighlight -> PfColors.Primary
        else -> PfColors.OnSurface
    }
    val subColor = if (isEstimate) PfColors.OnSecondaryFixedVariant else PfColors.Outline

    Column(modifier.background(bg, RoundedCornerShape(PfRadius.Base)).padding(PfSpacing.Md)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                fact.label, style = PfType.LabelSm.copy(fontWeight = FontWeight.SemiBold), color = labelColor,
                maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(4.dp))
            EvidenceChip(fact.evidence)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            fact.value,
            style = (if (fact.isHighlight) PfType.HeadlineSm else PfType.BodyLg).copy(fontWeight = FontWeight.Bold),
            color = valueColor,
        )
        fact.subtitle?.let {
            Text(it, style = PfType.LabelSm.copy(fontWeight = FontWeight.Medium), color = subColor)
        }
    }
}

@Composable
private fun ReadBackStrip(text: String, onPlay: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(PfRadius.Base),
        color = PfColors.SurfaceContainerHigh,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(PfSpacing.Md), verticalAlignment = Alignment.CenterVertically) {
            Surface(onClick = onPlay, shape = CircleShape, color = PfColors.Primary, shadowElevation = 4.dp) {
                Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.VolumeUp, "Play voice confirmation", tint = PfColors.OnPrimary, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Text(text, style = PfType.BodyMd, color = PfColors.OnSurface, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(PfSpacing.Sm))
            Box(Modifier.size(10.dp).background(PfColors.SurfaceTint, CircleShape))
        }
    }
}
