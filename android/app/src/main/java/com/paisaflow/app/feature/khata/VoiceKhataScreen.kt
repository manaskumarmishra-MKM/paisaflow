package com.paisaflow.app.feature.khata

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
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.i18n.strings2
import com.paisaflow.app.shared.PillButton
import com.paisaflow.app.shared.PillVariant
import com.paisaflow.app.shared.SimpleTopBar
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

data class KhataEntry(val customer: String, val amount: String, val status: String)

/**
 * Screen 14 — Voice Khata (DESIGN.md §22). Grammar (PRD §9 K4) parsed on-device for the demo:
 * "<name> ko ₹<amt> ka maal diya" → receivable · "<name> ne ₹<amt> diye" → payment.
 */
@Composable
fun VoiceKhataScreen(onBack: () -> Unit, onSpeak: () -> Unit, transcript: String?, onToast: (String) -> Unit) {
    val s2 = strings2
    val entries = remember { mutableStateListOf(KhataEntry("रमेश", "₹850", "बाक़ी"), KhataEntry("कुंदन", "₹2,500", "मिला")) }
    var parsed by remember(transcript) { mutableStateOf(transcript?.let(::parseKhata)) }
    Column(Modifier.fillMaxSize().background(PfColors.Surface)) {
        SimpleTopBar(s2.khataTitle, onBack)
        LazyColumn(contentPadding = PaddingValues(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Md)) {
            item {
                Surface(shape = RoundedCornerShape(24.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(PfSpacing.Md), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                        Surface(onClick = onSpeak, shape = CircleShape, color = PfColors.Primary, shadowElevation = 8.dp, modifier = Modifier.size(80.dp)) {
                            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Mic, null, tint = PfColors.OnPrimary, modifier = Modifier.size(36.dp)) }
                        }
                        Text(s2.khataHint, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant)
                        if (transcript != null) Text("“$transcript”", style = PfType.BodyLg.copy(fontWeight = FontWeight.SemiBold), color = PfColors.OnSurface)
                        parsed?.let { p ->
                            Column(Modifier.fillMaxWidth().background(PfColors.SurfaceContainerLow, RoundedCornerShape(16.dp)).padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(s2.understood, style = PfType.LabelSm, color = PfColors.OnSurfaceVariant)
                                KV(s2.customer, p.customer); KV(s2.amount, p.amount); KV(s2.status, p.status)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                                PillButton("✓ ${s2.save}", { entries.add(0, p); parsed = null; onToast("✓ ${s2.save}") }, Modifier.weight(1f), leadingIcon = Icons.Default.Save)
                                PillButton("✎ ${s2.fix}", { onToast(s2.fix) }, Modifier.weight(1f), PillVariant.NEUTRAL, Icons.Default.EditNote)
                            }
                        }
                    }
                }
            }
            item { Text(s2.recentEntries, style = PfType.HeadlineSm, color = PfColors.OnSurface) }
            items(entries.size) { i ->
                val e = entries[i]
                Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Receipt, null, tint = PfColors.Primary); Spacer(Modifier.width(10.dp))
                        Text(e.customer, style = PfType.LabelLg, color = PfColors.OnSurface, modifier = Modifier.weight(1f))
                        Text(e.amount, style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = if (e.status == "मिला") PfColors.Primary else PfColors.Secondary)
                        Spacer(Modifier.width(8.dp))
                        Text(e.status, style = PfType.LabelSm, color = PfColors.OnSurfaceVariant)
                    }
                }
            }
            item { Spacer(Modifier.height(PfSpacing.Lg)) }
        }
    }
}

@Composable
private fun KV(k: String, v: String) {
    Row { Text(k, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant, modifier = Modifier.weight(1f)); Text(v, style = PfType.BodyMd.copy(fontWeight = FontWeight.Bold), color = PfColors.OnSurface) }
}

private val AMOUNT = Regex("""₹?\s*(\d[\d,]*)""")

/** Minimal PRD §9 K4 grammar. Returns null when nothing recognisable. */
fun parseKhata(t: String): KhataEntry? {
    val amt = AMOUNT.find(t)?.groupValues?.get(1)?.replace(",", "")?.toIntOrNull() ?: return null
    val words = t.trim().split(Regex("\\s+"))
    val name = words.firstOrNull() ?: return null
    val received = Regex("\\b(ne|ने)\\b.*\\b(diye|diya|दिए|दिया)\\b").containsMatchIn(t) && !t.contains("maal", true) && !t.contains("माल")
    return KhataEntry(name, "₹" + "%,d".format(amt), if (received) "मिला" else "बाक़ी")
}
