package com.paisaflow.app.feature.memory

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.i18n.strings2
import com.paisaflow.app.core.model.EvidenceLabel
import com.paisaflow.app.shared.EvidenceChip
import com.paisaflow.app.shared.SimpleTopBar
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

data class MemoryEvent(val text: String, val source: String, val evidence: EvidenceLabel)

/** Screen 15 — Business Memory timeline (DESIGN.md §23). Append-only events with source + correction. */
@Composable
fun MemoryScreen(onBack: () -> Unit, onCorrect: (MemoryEvent) -> Unit) {
    val s2 = strings2
    val groups = listOf(
        s2.today to listOf(
            MemoryEvent("₹850 बिक्री दर्ज (रमेश टी स्टॉल)", "Voice · VOX-BH-2024-8831", EvidenceLabel.FACT),
            MemoryEvent("₹2,500 भुगतान मिला (कुंदन डेयरी)", "Voice", EvidenceLabel.FACT),
        ),
        s2.yesterday to listOf(MemoryEvent("चारा स्टॉक अपडेट: 2 बोरी", "Voice", EvidenceLabel.FACT)),
        "12 SEP" to listOf(MemoryEvent("विस्तार लक्ष्य बनाया: +6 गाय", "Voice", EvidenceLabel.FACT)),
        "05 SEP" to listOf(MemoryEvent("₹9 लाख लोन परिदृश्य जाँचा", "What-If", EvidenceLabel.ESTIMATE)),
    )
    Column(Modifier.fillMaxSize().background(PfColors.Surface)) {
        SimpleTopBar(s2.memoryTitle, onBack)
        LazyColumn(contentPadding = PaddingValues(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            groups.forEach { (day, events) ->
                item { Text(day.uppercase(), style = PfType.LabelSm, color = PfColors.OnSurfaceVariant, modifier = Modifier.padding(top = PfSpacing.Sm)) }
                items(events.size) { i ->
                    val e = events[i]
                    Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(32.dp).background(PfColors.PrimaryFixed, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.CheckCircle, null, tint = PfColors.Primary, modifier = Modifier.size(18.dp)) }
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(e.text, style = PfType.BodyMd.copy(fontWeight = FontWeight.SemiBold), color = PfColors.OnSurface)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${s2.eventSource}: ${e.source}", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                                    Spacer(Modifier.width(6.dp)); EvidenceChip(e.evidence)
                                }
                            }
                            Surface(onClick = { onCorrect(e) }, shape = CircleShape, color = PfColors.SurfaceContainerHigh) {
                                Text(s2.correct, style = PfType.LabelSm, color = PfColors.Primary, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(PfSpacing.Lg)) }
        }
    }
}
