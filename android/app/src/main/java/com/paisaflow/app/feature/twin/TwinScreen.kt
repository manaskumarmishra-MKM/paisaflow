package com.paisaflow.app.feature.twin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.i18n.strings
import com.paisaflow.app.core.i18n.strings2
import com.paisaflow.app.core.model.EvidenceLabel
import com.paisaflow.app.shared.EvidenceChip
import com.paisaflow.app.shared.PillButton
import com.paisaflow.app.shared.PillVariant
import com.paisaflow.app.shared.PulseDot
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

private val Green = Color(0xFF16A34A); private val Amber = Color(0xFFF59E0B); private val Red = Color(0xFFDC2626)

/** Screens 07 + 08 — Business Digital Twin + Business Health story (DESIGN.md §15–16, §39). */
@Composable
fun TwinScreen(onOpenMemory: () -> Unit, onRunWhatIf: () -> Unit) {
    val s2 = strings2
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Md)) {
        item { TwinCard(onOpenMemory) }
        item { HealthCard(onRunWhatIf) }
        item { Text(strings.sampleData, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.Outline, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
    }
}

@Composable
private fun TwinCard(onOpenMemory: () -> Unit) {
    val s2 = strings2
    Surface(shape = RoundedCornerShape(24.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).background(PfColors.Primary, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Hub, null, tint = PfColors.OnPrimary) }
                Spacer(Modifier.width(PfSpacing.Sm))
                Column(Modifier.weight(1f)) {
                    Text(s2.twinTitle, style = PfType.HeadlineSm, color = PfColors.Primary)
                    Text(s2.twinSubtitle, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                }
                PulseDot(Green, 10.dp)
            }
            Column(verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                    Quadrant(s2.money, "₹1,00,000", "₹78,000/माह", Icons.Default.Payments, EvidenceLabel.FACT, Modifier.weight(1f))
                    Quadrant(s2.market, "₹41.50/L", "3 डेयरी 5 km", Icons.Default.Storefront, EvidenceLabel.OBSERVATION, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                    Quadrant(s2.ops, "4 गाय · 32 L", "14 दिन चारा", Icons.Default.Inventory2, EvidenceLabel.FACT, Modifier.weight(1f))
                    Quadrant(s2.risk, "3.3 माह", "cash buffer", Icons.Default.Warning, EvidenceLabel.ESTIMATE, Modifier.weight(1f))
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(s2.lastUpdated, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant, modifier = Modifier.weight(1f))
                Text("Twin v1.2", style = PfType.LabelSm, color = PfColors.Primary)
            }
            PillButton(s2.viewMemory, onOpenMemory, Modifier.fillMaxWidth(), PillVariant.NEUTRAL)
        }
    }
}

@Composable
private fun Quadrant(title: String, value: String, sub: String, icon: ImageVector, ev: EvidenceLabel, modifier: Modifier) {
    Column(modifier.background(PfColors.SurfaceContainerLow, RoundedCornerShape(16.dp)).padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = PfColors.Primary, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(4.dp))
            Text(title, style = PfType.LabelSm, color = PfColors.OnSurfaceVariant, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(6.dp))
        Text(value, style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold), color = PfColors.OnSurface)
        Text(sub, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
        Spacer(Modifier.height(6.dp))
        EvidenceChip(ev)
    }
}

@Composable
private fun HealthCard(onRunWhatIf: () -> Unit) {
    val s2 = strings2
    val rows = listOf(
        Triple(s2.cashflow, s2.stable, Green), Triple(s2.payments, s2.delayed(3), Amber), Triple(s2.inventory, s2.healthy, Green),
        Triple(s2.expansion, s2.needsReview, Amber), Triple(s2.repayment, s2.stress, Red),
    )
    Surface(shape = RoundedCornerShape(24.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            Text(s2.healthTitle, style = PfType.HeadlineSm, color = PfColors.OnSurface)
            rows.forEach { (k, v, c) ->
                Row(Modifier.fillMaxWidth().background(PfColors.SurfaceContainerLow, RoundedCornerShape(12.dp)).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(12.dp).background(c, CircleShape)); Spacer(Modifier.width(10.dp))
                    Text(k, style = PfType.LabelMd, color = PfColors.OnSurface, modifier = Modifier.weight(1f))
                    Text(v, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                }
            }
            Text(s2.healthStory, style = PfType.BodyMd.copy(fontWeight = FontWeight.SemiBold), color = PfColors.Primary,
                modifier = Modifier.fillMaxWidth().background(PfColors.PrimaryFixed, RoundedCornerShape(16.dp)).padding(12.dp))
            PillButton(s2.runWhatIf, onRunWhatIf, Modifier.fillMaxWidth(), elevated = true)
        }
    }
}
