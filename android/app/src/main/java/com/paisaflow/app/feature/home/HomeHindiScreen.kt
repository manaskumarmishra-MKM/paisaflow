package com.paisaflow.app.feature.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisaflow.app.core.mock.DemoHomeHi
import com.paisaflow.app.core.model.ActionKind
import com.paisaflow.app.core.model.EvidenceLabel
import com.paisaflow.app.core.model.HiHomeData
import com.paisaflow.app.core.model.HiTask
import com.paisaflow.app.core.model.HiTile
import com.paisaflow.app.core.model.MetricIcon
import com.paisaflow.app.core.model.MetricTone
import com.paisaflow.app.shared.PulseDot
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/** Home (Hindi, v2): brand bar, greeting, terracotta-mic hero, alert, 2×2 pillars, Twin card, 3 tasks, last voice entry. */
@Composable
fun HomeHindiScreen(
    data: HiHomeData = DemoHomeHi.data,
    onSpeak: () -> Unit,
    onOpenSimulation: () -> Unit,
    onOpenActions: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToast: (String) -> Unit,
) {
    LazyColumn(
        Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(horizontal = PfSpacing.Md, vertical = PfSpacing.Sm),
        verticalArrangement = Arrangement.spacedBy(PfSpacing.Md),
    ) {
        item { BrandBar(data, onToggleLanguage) { onToast("पूरा पन्ना सुना रहा हूँ…") } }
        item { GreetingHi(data) }
        item { HeroHi(data, onSpeak, onToast) }
        item { AlertHi(data) { onToast("तगादा ड्राफ्ट तैयार — आपकी मंज़ूरी के बाद ही जाएगा") } }
        item { PillarsHi(data.tiles) }
        item { TwinCardHi(data, onOpenSimulation) }
        item { TasksHi(data.tasks, onOpenActions, onToast) }
        item { LastVoiceEntry(data.lastVoiceEntry) { onToast("आवाज़ रीप्ले…") } }
        item {
            Text("नमूना डेटा · डेमो व्यापार (असली आँकड़े नहीं)", style = PfType.LabelSm, color = PfColors.Outline,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun BrandBar(d: HiHomeData, onLang: () -> Unit, onSpeakAll: () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLow, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(PfSpacing.Sm), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).background(PfColors.PrimaryContainer, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Store, null, tint = PfColors.PrimaryFixed, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(d.businessName, style = PfType.HeadlineSm, color = PfColors.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.width(4.dp))
                    Text(d.blockChip, style = PfType.LabelSm, color = PfColors.OnPrimaryFixed,
                        modifier = Modifier.background(PfColors.PrimaryFixed, CircleShape).padding(horizontal = 8.dp, vertical = 2.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PulseDot(PfColors.Primary, 8.dp)
                    Spacer(Modifier.width(6.dp))
                    Text(d.liveLine, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                }
            }
            Surface(onClick = onLang, shape = CircleShape, color = PfColors.SurfaceContainerHighest) {
                Row(Modifier.defaultMinSize(minHeight = 44.dp).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Translate, null, tint = PfColors.OnSurface, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("हिंदी", style = PfType.LabelMd, color = PfColors.OnSurface)
                }
            }
            Spacer(Modifier.width(4.dp))
            Surface(onClick = onSpeakAll, shape = CircleShape, color = PfColors.Primary, shadowElevation = 1.dp, modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Campaign, "पूरा पन्ना सुनें", tint = PfColors.OnPrimary, modifier = Modifier.size(22.dp)) }
            }
        }
    }
}

@Composable
private fun GreetingHi(d: HiHomeData) {
    Column {
        Text(d.greeting, style = PfType.HeadlineLgMobile, color = PfColors.Primary)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarToday, null, tint = PfColors.OnSurfaceVariant, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(d.dateLine, style = PfType.LabelMd, color = PfColors.OnSurfaceVariant)
        }
        Spacer(Modifier.height(4.dp))
        Text(d.question, style = PfType.BodyLg.copy(fontWeight = FontWeight.SemiBold), color = PfColors.OnSurface)
    }
}

@Composable
private fun HeroHi(d: HiHomeData, onSpeak: () -> Unit, onToast: (String) -> Unit) {
    var transcript by remember { mutableStateOf(d.readyLine) }
    Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(PfColors.PrimaryContainer)) {
        Box(Modifier.align(Alignment.TopEnd).size(176.dp).background(PfColors.PrimaryFixed.copy(alpha = 0.10f), CircleShape))
        Column(Modifier.padding(PfSpacing.Md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(Modifier.background(PfColors.PrimaryFixed, CircleShape).padding(horizontal = 12.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Mic, null, tint = PfColors.Secondary, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("VOICE ASSISTANT · आवाज़ से हिसाब", style = PfType.LabelSm, color = PfColors.OnPrimaryFixed, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Spacer(Modifier.weight(1f))
                Text("Instant AI", style = PfType.LabelSm, color = PfColors.OnPrimary,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape).padding(horizontal = 10.dp, vertical = 2.dp))
            }
            Spacer(Modifier.height(PfSpacing.Md))
            Text(d.heroTitle, style = PfType.HeadlineMd, color = Color.White)
            Spacer(Modifier.height(4.dp))
            Text(d.heroSubtitle, style = PfType.BodyMd, color = PfColors.OnPrimaryContainer)
            Spacer(Modifier.height(PfSpacing.Md))
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                TerracottaMic { transcript = "सुनीता जी, बोलिए... आवाज़ रिकॉर्ड हो रही है..."; onSpeak() }
                Spacer(Modifier.height(4.dp))
                Text("दबाकर बोलिए (Boliye)", style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text("भोजपुरी, मगही व हिंदी समझता है", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnPrimaryContainer)
            }
            Spacer(Modifier.height(PfSpacing.Md))
            Row(Modifier.fillMaxWidth().background(PfColors.Primary, RoundedCornerShape(16.dp)).padding(PfSpacing.Sm), verticalAlignment = Alignment.CenterVertically) {
                MiniBars()
                Spacer(Modifier.width(PfSpacing.Sm))
                Text(transcript, style = PfType.BodyMd, color = PfColors.PrimaryFixedDim, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Spacer(Modifier.height(PfSpacing.Sm))
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                d.quickPrompts.forEach { p ->
                    Surface(onClick = { transcript = "पहचाना गया: ${p.substringAfter(' ')}"; onToast("प्रॉम्प्ट सेट") }, shape = CircleShape, color = Color.White.copy(alpha = 0.12f)) {
                        Text(p, style = PfType.LabelSm, color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun TerracottaMic(onClick: () -> Unit) {
    val t = rememberInfiniteTransition(label = "ring")
    val p by t.animateFloat(0f, 1f, infiniteRepeatable(tween(1800, easing = LinearEasing)), label = "p")
    Box(Modifier.size(120.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.size(112.dp).scale(0.8f + 0.35f * p).alpha(0.4f * (1 - p)).background(PfColors.SecondaryContainer, CircleShape))
        Box(Modifier.size(96.dp).background(PfColors.Secondary.copy(alpha = 0.3f), CircleShape))
        Surface(onClick = onClick, shape = CircleShape, color = PfColors.Secondary, shadowElevation = 12.dp, modifier = Modifier.size(80.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Mic, "माइक चालू करें और बोलें", tint = PfColors.OnSecondary, modifier = Modifier.size(36.dp)) }
        }
    }
}

@Composable
private fun MiniBars() {
    val t = rememberInfiniteTransition(label = "bars")
    val p by t.animateFloat(0.5f, 1f, infiniteRepeatable(tween(700), androidx.compose.animation.core.RepeatMode.Reverse), label = "p")
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        listOf(14f, 24f, 16f, 8f).forEach { h ->
            Box(Modifier.width(4.dp).height((h * p).dp).background(PfColors.PrimaryFixed, CircleShape))
        }
    }
}

@Composable
private fun AlertHi(d: HiHomeData, onTaqada: () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SecondaryFixed, shadowElevation = 3.dp, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(PfSpacing.Sm), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).background(PfColors.Secondary, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.NotificationImportant, null, tint = PfColors.OnSecondary, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(d.alertDays, style = PfType.LabelSm, color = PfColors.OnError,
                        modifier = Modifier.background(PfColors.Error, CircleShape).padding(horizontal = 8.dp, vertical = 2.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(d.alertName, style = PfType.LabelMd, color = PfColors.Secondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Text(d.alertLine, style = PfType.BodyMd, color = PfColors.OnSecondaryFixedVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Surface(onClick = onTaqada, shape = RoundedCornerShape(12.dp), color = PfColors.Secondary, shadowElevation = 1.dp) {
                Row(Modifier.defaultMinSize(minHeight = 48.dp).padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Send, null, tint = PfColors.OnSecondary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("तगादा", style = PfType.LabelMd, color = PfColors.OnSecondary)
                }
            }
        }
    }
}

@Composable
private fun PillarsHi(tiles: List<HiTile>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("कारोबार का हाल (4 खंभे)", style = PfType.HeadlineSm, color = PfColors.OnSurface)
                Text("पक्की जानकारी और लाइव अनुमान", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
            }
            Text("आज का बहीखाता", style = PfType.LabelSm, color = PfColors.OnPrimaryFixed,
                modifier = Modifier.background(PfColors.PrimaryFixed, CircleShape).padding(horizontal = 10.dp, vertical = 4.dp))
        }
        Spacer(Modifier.height(PfSpacing.Sm))
        Column(verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            tiles.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                    row.forEach { PillarTile(it, Modifier.weight(1f)) }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun PillarTile(t: HiTile, modifier: Modifier = Modifier) {
    val (iconBg, iconFg) = when (t.tone) {
        MetricTone.WARNING -> PfColors.SecondaryFixed to PfColors.Secondary
        MetricTone.SECONDARY -> PfColors.SurfaceContainerHighest to PfColors.OnSurface
        else -> PfColors.PrimaryFixed to PfColors.Primary
    }
    val valueColor = when (t.tone) { MetricTone.PRIMARY -> PfColors.Primary; MetricTone.WARNING -> PfColors.Secondary; else -> PfColors.OnSurface }
    val (chipBg, chipFg, chipText) = if (t.evidence == EvidenceLabel.ESTIMATE)
        Triple(PfColors.SecondaryFixed, PfColors.OnSecondaryFixed, "अनुमान") else Triple(PfColors.PrimaryFixed, PfColors.OnPrimaryFixed, "FACT")
    val (footBg, footFg) = when (t.tone) {
        MetricTone.NEUTRAL -> PfColors.PrimaryFixed.copy(alpha = 0.4f) to PfColors.Primary
        MetricTone.WARNING -> PfColors.SecondaryFixed.copy(alpha = 0.5f) to PfColors.OnSecondaryContainer
        else -> PfColors.SurfaceContainerLow to PfColors.OnSurfaceVariant
    }
    val icon = when (t.icon) {
        MetricIcon.PAYMENTS -> Icons.Default.AccountBalanceWallet
        MetricIcon.TRENDING_UP -> Icons.Default.TrendingUp
        MetricIcon.INVENTORY -> Icons.Default.Inventory2
        else -> Icons.Default.ReceiptLong
    }
    Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = modifier.defaultMinSize(minHeight = 148.dp)) {
        Column(Modifier.padding(PfSpacing.Sm), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(32.dp).background(iconBg, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        Icon(icon, null, tint = iconFg, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.weight(1f))
                    Text(chipText, style = PfType.LabelSm, color = chipFg, modifier = Modifier.background(chipBg, CircleShape).padding(horizontal = 8.dp, vertical = 2.dp))
                }
                Spacer(Modifier.height(8.dp))
                Text(t.label, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                Text(t.value, style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.3).sp), color = valueColor)
            }
            Spacer(Modifier.height(8.dp))
            Text(t.footer, style = PfType.LabelSm.copy(fontWeight = if (t.tone == MetricTone.NEUTRAL) FontWeight.SemiBold else FontWeight.Normal), color = footFg,
                modifier = Modifier.fillMaxWidth().background(footBg, RoundedCornerShape(8.dp)).padding(6.dp), maxLines = 2)
        }
    }
}

@Composable
private fun TwinCardHi(d: HiHomeData, onOpen: () -> Unit) {
    Box(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(PfColors.Primary, PfColors.PrimaryContainer, PfColors.SurfaceTint))),
    ) {
        Column(Modifier.padding(PfSpacing.Md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(28.dp).background(PfColors.PrimaryFixed, CircleShape), contentAlignment = Alignment.Center) {
                    Text("AI", style = PfType.LabelSm.copy(fontSize = 13.sp), color = PfColors.OnPrimaryFixed)
                }
                Spacer(Modifier.width(8.dp))
                Text(d.twinName.uppercase(), style = PfType.LabelMd.copy(letterSpacing = 1.sp), color = PfColors.PrimaryFixed, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("AI INFERENCE", style = PfType.LabelSm, color = PfColors.OnPrimary,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape).padding(horizontal = 8.dp, vertical = 2.dp))
            }
            Spacer(Modifier.height(PfSpacing.Sm))
            Row(verticalAlignment = Alignment.CenterVertically) {
                PulseDot(PfColors.PrimaryFixed, 14.dp)
                Spacer(Modifier.width(8.dp))
                Text(d.healthLine, style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold), color = Color.White)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                buildAnnotatedString {
                    append("मौजूदा बचत से आपका कारोबार ")
                    withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) { append(d.runwayText) }
                    append(" बिना किसी नए कर्ज के पूरी मजबूती से चल सकता है।")
                },
                style = PfType.BodyMd, color = PfColors.PrimaryFixedDim,
            )
            Spacer(Modifier.height(PfSpacing.Sm))
            Row(Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp)).padding(PfSpacing.Sm), verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.AcUnit, null, tint = PfColors.SecondaryContainer, modifier = Modifier.size(22.dp).padding(top = 2.dp))
                Spacer(Modifier.width(PfSpacing.Sm))
                Column {
                    Text(d.seasonTitle, style = PfType.LabelMd, color = PfColors.SecondaryFixed)
                    Text(d.seasonBody, style = PfType.BodyMd, color = PfColors.OnPrimary.copy(alpha = 0.9f))
                }
            }
            Spacer(Modifier.height(PfSpacing.Md))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(onClick = onOpen, shape = RoundedCornerShape(16.dp), color = PfColors.PrimaryFixed, shadowElevation = 4.dp) {
                    Row(Modifier.defaultMinSize(minHeight = 48.dp).padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("सिमुलेशन खोलें", style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = PfColors.OnPrimaryFixed)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = PfColors.OnPrimaryFixed, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(Modifier.weight(1f))
                Text(d.modelAccuracy, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.PrimaryFixedDim)
            }
        }
    }
}

@Composable
private fun TasksHi(tasks: List<HiTask>, onSeeAll: () -> Unit, onToast: (String) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("🎯 आज के 3 ज़रूरी काम", style = PfType.HeadlineSm, color = PfColors.OnSurface)
                Text("समय पर करने से मुनाफ़ा सुरक्षित रहेगा", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
            }
            Surface(onClick = onSeeAll, color = Color.Transparent) {
                Text("सभी देखें (${tasks.size}) →", style = PfType.LabelMd, color = PfColors.Primary, modifier = Modifier.padding(8.dp))
            }
        }
        Spacer(Modifier.height(PfSpacing.Sm))
        Column(verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            tasks.forEach { t -> TaskRowHi(t, onToast) }
        }
    }
}

@Composable
private fun TaskRowHi(t: HiTask, onToast: (String) -> Unit) {
    var done by remember { mutableStateOf(false) }
    val (iconBg, iconFg, icon) = when (t.kind) {
        ActionKind.SEND -> Triple(PfColors.SecondaryFixed, PfColors.Secondary, Icons.Default.Chat)
        ActionKind.CHECK -> Triple(PfColors.PrimaryFixed, PfColors.Primary, Icons.Default.CheckCircle)
        ActionKind.CALL -> Triple(PfColors.SurfaceContainerHigh, PfColors.Primary, Icons.Default.PriceChange)
    }
    Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(PfSpacing.Sm), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).background(iconBg, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = iconFg, modifier = Modifier.size(26.dp))
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Column(Modifier.weight(1f)) {
                Text(t.title, style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = PfColors.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(t.subtitle, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            val filled = t.primary || done
            Surface(
                onClick = {
                    if (t.kind == ActionKind.CHECK) done = true
                    onToast(if (t.kind == ActionKind.SEND) "संदेश ड्राफ्ट — आपकी मंज़ूरी के बाद भेजा जाएगा" else t.title)
                },
                shape = RoundedCornerShape(12.dp),
                color = if (t.primary) PfColors.Primary else if (done) PfColors.PrimaryFixed else PfColors.SurfaceContainerHigh,
            ) {
                Box(Modifier.defaultMinSize(minWidth = 56.dp, minHeight = 48.dp).padding(horizontal = 14.dp), contentAlignment = Alignment.Center) {
                    Text(
                        if (done) "दर्ज हुआ ✓" else t.buttonLabel, style = PfType.LabelMd,
                        color = if (t.primary) PfColors.OnPrimary else if (filled) PfColors.OnPrimaryFixed else PfColors.OnSurface,
                    )
                }
            }
        }
    }
}

@Composable
private fun LastVoiceEntry(text: String, onReplay: () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(PfSpacing.Sm), verticalAlignment = Alignment.CenterVertically) {
            PulseDot(PfColors.Primary, 12.dp)
            Spacer(Modifier.width(8.dp))
            Text("पिछली आवाज़ प्रविष्टि:", style = PfType.LabelMd.copy(fontWeight = FontWeight.SemiBold), color = PfColors.OnSurface)
            Spacer(Modifier.width(6.dp))
            Text(text, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
            Surface(onClick = onReplay, shape = CircleShape, color = Color.Transparent) {
                Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.VolumeUp, "पिछली प्रविष्टि सुनें", tint = PfColors.Primary, modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}
