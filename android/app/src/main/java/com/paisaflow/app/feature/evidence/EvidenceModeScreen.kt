package com.paisaflow.app.feature.evidence

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Troubleshoot
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.HelpCenter
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.mock.DemoEvidence
import com.paisaflow.app.core.model.BucketShare
import com.paisaflow.app.core.model.EvidenceData
import com.paisaflow.app.core.model.EvidenceLabel
import com.paisaflow.app.core.model.EvidenceSource
import com.paisaflow.app.core.model.FieldStory
import com.paisaflow.app.core.model.MetricTone
import com.paisaflow.app.core.model.ProvenanceShare
import com.paisaflow.app.core.model.SimpleExplanation
import com.paisaflow.app.core.model.SourceIcon
import com.paisaflow.app.shared.PillButton
import com.paisaflow.app.shared.PillVariant
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

private val Green = Color(0xFF16A34A)
private val Orange = Color(0xFFF59E0B)
private val Purple = Color(0xFF7C3AED)

private fun EvidenceLabel.tint(): Color = when (this) {
    EvidenceLabel.FACT -> Green
    EvidenceLabel.OBSERVATION -> Orange
    EvidenceLabel.ESTIMATE -> Purple
    else -> PfColors.Outline
}

private fun EvidenceLabel.dot(): String = when (this) {
    EvidenceLabel.FACT -> "🟢"
    EvidenceLabel.OBSERVATION -> "🟠"
    EvidenceLabel.ESTIMATE -> "🟣"
    else -> "⚪"
}

/** Screen 12 — Evidence Mode + "Samajh nahi aa raha?" (DESIGN.md §20, §28). Full-screen route. */
@Composable
fun EvidenceModeScreen(
    data: EvidenceData = DemoEvidence.data,
    onBack: () -> Unit,
    onSpeak: () -> Unit,
    onToast: (String) -> Unit,
) {
    Scaffold(
        containerColor = PfColors.Surface,
        topBar = { EvidenceTopBar(onBack) { onToast("Screen padh kar sunaa raha hoon…") } },
        bottomBar = { BoliyeBar(onSpeak) },
    ) { pad ->
        LazyColumn(
            Modifier.fillMaxSize().padding(pad),
            contentPadding = PaddingValues(horizontal = PfSpacing.Md, vertical = PfSpacing.Md),
            verticalArrangement = Arrangement.spacedBy(PfSpacing.Md),
        ) {
            item { OwnerRow(data) }
            item { EvidenceBanner() }
            item { ProvenanceCard(data.provenance, data.verifiedScore) }
            items(data.sources.size) { i -> SourceCard(data.sources[i]) }
            item { ExplanationCard(data.explanation) { onToast("Hindi/Bhojpuri mein sunaa raha hoon…") } }
            item { StoryCard(data.story) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                    PillButton("छोटा उदाहरण देखें", { onToast("Chhota udaharan…") }, Modifier.weight(1f), PillVariant.NEUTRAL, Icons.Default.Lightbulb)
                    PillButton("मित्र से बात करें", { onToast("Facilitator se jod raha hoon…") }, Modifier.weight(1f), PillVariant.NEUTRAL, Icons.Default.SupportAgent)
                }
            }
            item {
                Text(
                    "Sample data · demo business (not real statistics)",
                    style = PfType.LabelSm, color = PfColors.Outline, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun EvidenceTopBar(onBack: () -> Unit, onSpeak: () -> Unit) {
    Surface(color = PfColors.Surface, shadowElevation = 2.dp) {
        Row(
            Modifier.fillMaxWidth().statusBarsPadding().height(64.dp).padding(horizontal = PfSpacing.Sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(onClick = onBack, shape = CircleShape, color = Color.Transparent) {
                Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = PfColors.OnSurface)
                }
            }
            Text("Audio Playback & Audit", style = PfType.HeadlineSm, color = PfColors.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
            Surface(onClick = onSpeak, shape = CircleShape, color = PfColors.SurfaceContainerHigh) {
                Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.VolumeUp, "Sunen", tint = PfColors.Primary, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(Modifier.width(PfSpacing.Xs))
            Box(Modifier.size(36.dp).background(PfColors.PrimaryFixed, CircleShape), contentAlignment = Alignment.Center) {
                Text("₹", style = PfType.LabelLg, color = PfColors.Primary)
            }
        }
    }
}

@Composable
private fun OwnerRow(d: EvidenceData) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(44.dp).background(PfColors.PrimaryFixed, CircleShape), contentAlignment = Alignment.Center) {
            Text(d.ownerInitials, style = PfType.LabelLg, color = PfColors.Primary)
        }
        Spacer(Modifier.width(PfSpacing.Sm))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(d.ownerName, style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = PfColors.OnSurface)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Default.Verified, null, tint = Green, modifier = Modifier.size(16.dp))
            }
            Text(d.ownerMeta, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun EvidenceBanner() {
    Surface(shape = RoundedCornerShape(20.dp), color = PfColors.Primary, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Troubleshoot, null, tint = PfColors.PrimaryFixed, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("पारदर्शिता और प्रमाण (EVIDENCE MODE)", style = PfType.LabelSm, color = PfColors.PrimaryFixed)
            }
            Spacer(Modifier.height(6.dp))
            Text("🔎 ये रिज़ल्ट कहाँ से आया?", style = PfType.HeadlineMd, color = Color.White)
            Spacer(Modifier.height(4.dp))
            Text("हर एक आंकड़ा 100% जाँचा-परखा है। इसमें कोई मनगढ़ंत या बनावटी हिसाब नहीं है।", style = PfType.BodyMd, color = PfColors.PrimaryFixedDim)
        }
    }
}

@Composable
private fun ProvenanceCard(shares: List<ProvenanceShare>, score: String) {
    Surface(shape = RoundedCornerShape(20.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("डेटा स्रोत विभाजन (Data Provenance)", style = PfType.LabelMd, color = PfColors.OnSurface, modifier = Modifier.weight(1f))
                Text(score, style = PfType.LabelSm, color = Green)
            }
            Spacer(Modifier.height(PfSpacing.Sm))
            Row(Modifier.fillMaxWidth().height(12.dp).clip(CircleShape)) {
                shares.forEach { s -> Box(Modifier.weight(s.percent.toFloat()).fillMaxSize().background(s.evidence.tint())) }
            }
            Spacer(Modifier.height(PfSpacing.Sm))
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                shares.forEach { s ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(8.dp).background(s.evidence.tint(), CircleShape))
                        Spacer(Modifier.width(4.dp))
                        Text(s.label, style = PfType.LabelSm, color = PfColors.OnSurfaceVariant)
                    }
                }
            }
        }
    }
}

private fun SourceIcon.vector(): ImageVector = when (this) {
    SourceIcon.MIC -> Icons.Default.Mic
    SourceIcon.STOREFRONT -> Icons.Default.Storefront
    SourceIcon.TERMINAL -> Icons.Default.Terminal
}

@Composable
private fun SourceCard(s: EvidenceSource) {
    val tint = s.evidence.tint()
    Surface(shape = RoundedCornerShape(20.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(s.iconKind.vector(), null, tint = PfColors.Primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text(s.title, style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = PfColors.OnSurface, modifier = Modifier.weight(1f))
                Text(
                    "${s.evidence.dot()} ${s.evidence.display} (${s.percent}%)", style = PfType.LabelSm, color = tint,
                    modifier = Modifier.background(tint.copy(alpha = 0.12f), CircleShape).padding(horizontal = 8.dp, vertical = 3.dp),
                )
            }
            Text(s.description, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant)
            if (s.rows.isNotEmpty() && s.evidence == EvidenceLabel.FACT) {
                Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                    s.rows.forEach { kv ->
                        Column(
                            Modifier.weight(1f).background(PfColors.SurfaceContainerLow, RoundedCornerShape(12.dp)).padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(kv.key, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(kv.value, style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = PfColors.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            } else if (s.rows.isNotEmpty()) {
                s.rows.forEach { kv ->
                    Row(Modifier.fillMaxWidth().background(PfColors.SurfaceContainerLow, RoundedCornerShape(12.dp)).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(kv.key, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant, modifier = Modifier.weight(1f))
                        Text(kv.value, style = PfType.LabelMd, color = if (s.evidence == EvidenceLabel.OBSERVATION) PfColors.Secondary else PfColors.OnSurface)
                    }
                }
            }
            s.checks.forEach { c ->
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.CheckCircle, null, tint = PfColors.Primary, modifier = Modifier.size(18.dp).padding(top = 2.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(c, style = PfType.BodyMd, color = PfColors.OnSurface)
                }
            }
            s.footer?.let { f ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.GraphicEq, null, tint = PfColors.Outline, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(f, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.Outline)
                }
            }
        }
    }
}

@Composable
private fun ExplanationCard(e: SimpleExplanation, onListen: () -> Unit) {
    Surface(shape = RoundedCornerShape(24.dp), color = PfColors.SecondaryFixed, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.HelpCenter, null, tint = PfColors.Secondary, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(6.dp))
                Column(Modifier.weight(1f)) {
                    Text("❓ समझ नहीं आ रहा?", style = PfType.HeadlineSm, color = PfColors.OnSecondaryFixed)
                    Text("सरल उदाहरण से समझें (Simple Real-Life Metaphor)", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSecondaryFixedVariant)
                }
                Surface(onClick = onListen, shape = CircleShape, color = PfColors.Secondary) {
                    Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.VolumeUp, "Sunen", tint = PfColors.OnSecondary, modifier = Modifier.size(20.dp))
                    }
                }
            }
            Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLowest, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(PfSpacing.Md)) {
                    Text("आज का सवाल", style = PfType.LabelSm, color = PfColors.Secondary)
                    Text(e.question, style = PfType.HeadlineSm, color = PfColors.OnSurface)
                }
            }
            Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLowest, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(PfSpacing.Md), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(e.metaphorTitle, style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = PfColors.OnSurface, textAlign = TextAlign.Center)
                    Text(e.metaphorSubtitle, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                    Spacer(Modifier.height(PfSpacing.Sm))
                    BucketFunnel(e.shares)
                    Spacer(Modifier.height(PfSpacing.Sm))
                    Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                        e.shares.forEach { b ->
                            val (bg, fg) = b.tone.chip()
                            Column(Modifier.weight(1f).background(bg, RoundedCornerShape(12.dp)).padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${b.buckets} बाल्टी", style = PfType.LabelMd, color = fg)
                                Text(b.label, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = fg)
                            }
                        }
                    }
                }
            }
            Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLowest, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("सीधी बात:", style = PfType.LabelMd, color = PfColors.Secondary)
                    e.plainTalk.forEach { Text(it, style = PfType.BodyMd, color = PfColors.OnSurface) }
                }
            }
            Surface(onClick = onListen, shape = CircleShape, color = PfColors.Primary, modifier = Modifier.fillMaxWidth().height(PfSpacing.TouchTarget)) {
                Row(Modifier.padding(horizontal = PfSpacing.Md), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VolumeUp, null, tint = PfColors.OnPrimary, modifier = Modifier.size(20.dp))
                    Text("🔊 आवाज़ में सुनें (Listen in Hindi/Bhojpuri)", style = PfType.LabelMd, color = PfColors.OnPrimary, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.PlayCircle, null, tint = PfColors.OnPrimary, modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}

private fun MetricTone.chip(): Pair<Color, Color> = when (this) {
    MetricTone.WARNING -> PfColors.SecondaryFixed to PfColors.OnSecondaryFixed
    MetricTone.PRIMARY -> PfColors.PrimaryFixed to PfColors.OnPrimaryFixed
    else -> PfColors.SurfaceContainerHigh to PfColors.OnSurface
}

/** Three stacked, narrowing bands — the "bucket" funnel from the mockup. */
@Composable
private fun BucketFunnel(shares: List<BucketShare>) {
    val widths = listOf(1f, 0.85f, 0.7f)
    val colors = listOf(PfColors.SecondaryFixedDim, PfColors.PrimaryFixedDim, PfColors.PrimaryFixed)
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        shares.forEachIndexed { i, b ->
            Box(
                Modifier.fillMaxWidth(widths.getOrElse(i) { 0.6f }).height(30.dp)
                    .background(colors.getOrElse(i) { PfColors.SurfaceContainerHigh }, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center,
            ) { Text("${b.buckets} बाल्टी: ${b.label}", style = PfType.LabelSm, color = PfColors.OnSurface) }
        }
    }
}

@Composable
private fun StoryCard(s: FieldStory) {
    Surface(shape = RoundedCornerShape(20.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Psychology, null, tint = PfColors.Primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("पड़ोस की मिसाल (Real Field Story)", style = PfType.LabelMd, color = PfColors.OnSurface)
            }
            Spacer(Modifier.height(PfSpacing.Sm))
            Row(verticalAlignment = Alignment.Top) {
                Box(Modifier.size(44.dp).background(PfColors.SecondaryFixed, CircleShape), contentAlignment = Alignment.Center) {
                    Text(s.person.take(1), style = PfType.LabelLg, color = PfColors.OnSecondaryFixed)
                }
                Spacer(Modifier.width(PfSpacing.Sm))
                Column(Modifier.weight(1f)) {
                    Text(s.person, style = PfType.LabelMd, color = PfColors.OnSurface)
                    Text(s.quote, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun BoliyeBar(onSpeak: () -> Unit) {
    Surface(color = PfColors.Surface, shadowElevation = 8.dp) {
        Box(Modifier.fillMaxWidth().navigationBarsPadding().padding(PfSpacing.Md)) {
            Surface(onClick = onSpeak, shape = CircleShape, color = PfColors.Primary, shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth().height(64.dp)) {
                Row(Modifier.padding(horizontal = PfSpacing.Md), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Box(Modifier.size(40.dp).background(PfColors.SecondaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Mic, null, tint = PfColors.OnSecondaryContainer, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(PfSpacing.Sm))
                    Column {
                        Text("बोलिए (Boliye)", style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = PfColors.OnPrimary)
                        Text("कोई भी सवाल बेझिझक पूछें", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.PrimaryFixedDim)
                    }
                }
            }
        }
    }
}
