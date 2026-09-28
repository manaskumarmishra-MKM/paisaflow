package com.paisaflow.app.feature.actions

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.i18n.ActionsContent
import com.paisaflow.app.core.i18n.LocalAppLanguage
import com.paisaflow.app.core.i18n.strings
import com.paisaflow.app.core.model.CollectionAction
import com.paisaflow.app.core.model.FinanceAction
import com.paisaflow.app.core.model.ReorderAction
import com.paisaflow.app.shared.PulseDot
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/**
 * Screen 13 — Action Layer (आज के ३ ज़रूरी काम). Every consequential action is
 * draft → review → approve (RULES.md §11); outcomes are logged back to Memory.
 */
@Composable
fun ActionsScreen(onSpeak: () -> Unit, onOpenWhatIf: () -> Unit, onToast: (String) -> Unit) {
    val s = strings
    val data = ActionsContent.forLang(LocalAppLanguage.current)
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = PfSpacing.Md, vertical = PfSpacing.Md),
        verticalArrangement = Arrangement.spacedBy(PfSpacing.Md),
    ) {
        item { OverviewCard(data.pendingCount, data.audioDuration, onToast) }
        item { CollectionCard(data.collection, onToast) }
        item { ReorderCard(data.reorder, onToast) }
        item { FinanceCard(data.finance, onOpenWhatIf) }
        item { HelpCard(onSpeak) }
        item { SpeakNewActionButton(onSpeak) }
        item { Text(s.sampleData, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.Outline, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
    }
}

// ---------- building blocks ----------

@Composable
private fun Card(stripe: Color? = null, content: @Composable () -> Unit) {
    Surface(shape = RoundedCornerShape(24.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column {
            if (stripe != null) Box(Modifier.fillMaxWidth().height(6.dp).background(stripe))
            Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Md)) { content() }
        }
    }
}

@Composable
private fun Chip(text: String, bg: Color, fg: Color, icon: ImageVector? = null) {
    Row(Modifier.background(bg, CircleShape).padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        if (icon != null) { Icon(icon, null, tint = fg, modifier = Modifier.size(14.dp)); Spacer(Modifier.width(4.dp)) }
        Text(text, style = PfType.LabelSm, color = fg)
    }
}

@Composable
private fun BigButton(text: String, icon: ImageVector, bg: Color, fg: Color, onClick: () -> Unit, minHeight: Int = 56) {
    Surface(onClick = onClick, shape = RoundedCornerShape(16.dp), color = bg, shadowElevation = if (bg == PfColors.PrimaryContainer || bg == PfColors.Primary) 4.dp else 0.dp,
        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = minHeight.dp)) {
        Row(Modifier.padding(horizontal = PfSpacing.Md, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = fg, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
            Text(text, style = if (minHeight >= 56) PfType.LabelLg else PfType.LabelMd, color = fg, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun NoteBox(icon: ImageVector, text: String, bg: Color = PfColors.SurfaceContainer, iconTint: Color = PfColors.PrimaryContainer, textColor: Color = PfColors.OnSurfaceVariant) {
    Row(Modifier.fillMaxWidth().background(bg, RoundedCornerShape(16.dp)).padding(12.dp), verticalAlignment = Alignment.Top) {
        Icon(icon, null, tint = iconTint, modifier = Modifier.size(22.dp).padding(top = 2.dp))
        Spacer(Modifier.width(10.dp))
        Text(text, style = PfType.BodyMd, color = textColor)
    }
}

// ---------- sections ----------

@Composable
private fun OverviewCard(pending: Int, duration: String, onToast: (String) -> Unit) {
    val s = strings
    var playing by rememberSaveable { mutableStateOf(false) }
    Card {
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Chip(s.actionLayerChip, PfColors.PrimaryContainer, PfColors.OnPrimary, Icons.Default.Bolt)
                Spacer(Modifier.height(6.dp))
                Text(s.actionsTitle, style = PfType.HeadlineLgMobile, color = PfColors.Primary)
                Text(s.actionsIntro, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant)
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Row(Modifier.background(PfColors.SecondaryFixed, CircleShape).padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                PulseDot(PfColors.Secondary, 8.dp); Spacer(Modifier.width(4.dp))
                Text(s.pendingChip(pending), style = PfType.LabelSm, color = PfColors.OnSecondaryFixed)
            }
        }
        Row(Modifier.fillMaxWidth().background(PfColors.SurfaceContainerLow, RoundedCornerShape(16.dp)).padding(PfSpacing.Sm), verticalAlignment = Alignment.CenterVertically) {
            Surface(onClick = { playing = !playing; onToast(if (playing) "सुन रहे हैं…" else "रुका") }, shape = CircleShape, color = if (playing) PfColors.Secondary else PfColors.Primary, modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(if (playing) Icons.Default.Pause else Icons.Default.VolumeUp, null, tint = PfColors.OnPrimary, modifier = Modifier.size(24.dp)) }
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Column(Modifier.weight(1f)) {
                Text(s.listenAudio, style = PfType.LabelMd, color = PfColors.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${s.listenAudioSub} ($duration)", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = if (playing) PfColors.Primary else PfColors.Outline, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Chip(if (playing) s.pauseBtn else s.listenBtn, PfColors.PrimaryFixed, PfColors.OnPrimaryFixed)
        }
    }
}

@Composable
private fun CollectionCard(a: CollectionAction, onToast: (String) -> Unit) {
    val s = strings
    val ctx = LocalContext.current
    var sent by rememberSaveable { mutableStateOf(false) }
    var snoozed by rememberSaveable { mutableStateOf(false) }
    var outcome by rememberSaveable { mutableStateOf<String?>(null) }
    Card(stripe = PfColors.Secondary) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Chip(s.prioMostUrgent, PfColors.Secondary, PfColors.OnSecondary)
            Chip(s.daysOverdue(12), PfColors.ErrorContainer, PfColors.OnErrorContainer, Icons.Default.Warning)
            Spacer(Modifier.weight(1f))
            Chip(s.factChip, PfColors.PrimaryFixed, PfColors.OnPrimaryFixed, Icons.Default.Verified)
        }
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(a.title, style = PfType.HeadlineSm, color = PfColors.OnSurface)
                Text(a.place, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(a.amount, style = PfType.HeadlineLgMobile, color = PfColors.Secondary)
                Text(s.recoverable, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
            }
        }
        // Customer snippet (v1)
        Row(Modifier.fillMaxWidth().background(PfColors.SurfaceContainerLow, RoundedCornerShape(12.dp)).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(44.dp).background(PfColors.SecondaryFixed, CircleShape), contentAlignment = Alignment.Center) { Text("र", style = PfType.LabelLg, color = PfColors.OnSecondaryFixed) }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(a.customer, style = PfType.LabelMd, color = PfColors.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(a.relationship, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Surface(onClick = { ctx.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${a.phone}"))) }, shape = CircleShape, color = PfColors.SurfaceContainerHigh) {
                Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) { Icon(Icons.Default.Call, s.callLabel, tint = PfColors.Primary, modifier = Modifier.size(20.dp)) }
            }
        }
        Column(Modifier.fillMaxWidth().background(PfColors.SurfaceContainerLow, RoundedCornerShape(16.dp)).padding(PfSpacing.Sm), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(a.context, style = PfType.BodyMd, color = PfColors.OnSurface)
            NoteBox(Icons.Default.Lightbulb, a.why, bg = PfColors.PrimaryFixed.copy(alpha = 0.4f), iconTint = PfColors.Primary, textColor = PfColors.Primary)
        }
        // Draft (RULES.md §11 — never auto-sent)
        Column(Modifier.fillMaxWidth().background(PfColors.SurfaceContainer, RoundedCornerShape(16.dp)).padding(PfSpacing.Sm), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Chat, null, tint = PfColors.Primary, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp))
                Text(s.draftLabel, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant, modifier = Modifier.weight(1f))
                Text(s.edit, style = PfType.LabelSm, color = PfColors.Primary, modifier = Modifier.clip(CircleShape).padding(4.dp))
            }
            Text("« ${a.draft} »", style = PfType.BodyMd.copy(fontStyle = FontStyle.Italic), color = PfColors.OnSurface,
                modifier = Modifier.fillMaxWidth().background(PfColors.SurfaceContainerLowest, RoundedCornerShape(12.dp)).padding(10.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            if (sent) BigButton("✓ " + s.approveSend.substringAfter("✓ ").substringBefore(" (") , Icons.Default.DoneAll, PfColors.PrimaryFixed, PfColors.OnPrimaryFixed, {})
            else BigButton(s.approveSend, Icons.AutoMirrored.Filled.Send, PfColors.PrimaryContainer, PfColors.OnPrimary, {
                sent = true
                val uri = Uri.parse("https://api.whatsapp.com/send?text=" + Uri.encode(a.draft))
                runCatching { ctx.startActivity(Intent(Intent.ACTION_VIEW, uri)) }.onFailure { onToast("WhatsApp nahi mila — sandesh copy karke bhejein") }
            })
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(Modifier.weight(1f)) { BigButton(s.changeMsg, Icons.Default.EditNote, PfColors.SurfaceContainer, PfColors.OnSurface, { onToast(s.changeMsg) }, 48) }
                Box(Modifier.weight(1f)) {
                    BigButton(if (snoozed) "✓ " + s.snooze.substringBefore(" (") else s.snooze, Icons.Default.Schedule, PfColors.SurfaceContainer, PfColors.OnSurfaceVariant, { snoozed = true }, 48)
                }
            }
        }
        // Outcome logger → Memory (closed loop)
        Column(Modifier.fillMaxWidth().background(PfColors.SurfaceContainerLow, RoundedCornerShape(16.dp)).padding(PfSpacing.Sm), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Psychology, null, tint = PfColors.Primary, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp))
                Text(s.logOutcome, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
            }
            when (outcome) {
                "received" -> Text("✓ ${a.amount} दर्ज हुआ! मुनीम जी ने लेज़र अपडेट कर दिया।", style = PfType.LabelSm, color = PfColors.OnPrimaryFixed, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().background(PfColors.PrimaryFixed, RoundedCornerShape(12.dp)).padding(8.dp))
                "tomorrow" -> Text("कल के लिए नया तगादा रिमाइंडर सेट हुआ।", style = PfType.LabelSm, color = PfColors.OnSurface, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().background(PfColors.SurfaceContainerHighest, RoundedCornerShape(12.dp)).padding(8.dp))
                else -> Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.weight(1f)) { BigButton(s.paidYes, Icons.Default.CheckCircle, PfColors.PrimaryFixed, PfColors.OnPrimaryFixed, { outcome = "received" }, 44) }
                    Box(Modifier.weight(1f)) { BigButton(s.paidTomorrow, Icons.Default.Event, PfColors.SurfaceContainerHighest, PfColors.OnSurface, { outcome = "tomorrow" }, 44) }
                }
            }
        }
    }
}

@Composable
private fun ReorderCard(a: ReorderAction, onToast: (String) -> Unit) {
    val s = strings
    val ctx = LocalContext.current
    Card(stripe = PfColors.PrimaryContainer) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Chip(s.prioOps, PfColors.PrimaryContainer, PfColors.OnPrimary)
            Chip(s.endsInDays(3), PfColors.SecondaryContainer, PfColors.OnSecondaryContainer, Icons.Default.HourglassTop)
            Spacer(Modifier.weight(1f))
            Chip(s.estimateChip, PfColors.SecondaryFixed, PfColors.OnSecondaryFixed, Icons.Default.QueryStats)
        }
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(a.title, style = PfType.HeadlineSm, color = PfColors.OnSurface)
                Text(a.supplier, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(a.amount, style = PfType.HeadlineMd, color = PfColors.Primary)
                Text(a.saving, style = PfType.LabelSm, color = PfColors.Primary)
            }
        }
        Column(Modifier.fillMaxWidth().background(PfColors.SurfaceContainerLow, RoundedCornerShape(16.dp)).padding(PfSpacing.Sm), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row { Text(s.stockLevel, style = PfType.LabelMd.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurface, modifier = Modifier.weight(1f)); Text(s.remaining(a.stockPct, a.stockDays), style = PfType.LabelSm, color = PfColors.Secondary) }
            Box(Modifier.fillMaxWidth().height(12.dp).clip(CircleShape).background(PfColors.SurfaceContainerHighest)) {
                Box(Modifier.fillMaxWidth(a.stockPct / 100f).fillMaxSize().background(PfColors.Secondary, CircleShape))
            }
            Row { Text(s.available(a.availableBags), style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant, modifier = Modifier.weight(1f)); Text(s.minSafe(a.minBags), style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant) }
        }
        Row(Modifier.fillMaxWidth().background(PfColors.SurfaceContainer, RoundedCornerShape(12.dp)).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(56.dp).background(PfColors.SecondaryFixed, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) { Text("🌾", style = PfType.HeadlineMd) }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(a.product, style = PfType.LabelMd, color = PfColors.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(a.priceNote, style = PfType.LabelSm, color = PfColors.Secondary)
            }
        }
        NoteBox(Icons.Default.TrendingUp, a.insight, bg = PfColors.PrimaryFixed, iconTint = PfColors.Primary, textColor = PfColors.OnPrimaryFixed)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            BigButton(s.callDealer, Icons.Default.PhoneInTalk, PfColors.PrimaryContainer, PfColors.OnPrimary, { ctx.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${a.phone}"))) })
            BigButton(s.checkRates, Icons.Default.Analytics, PfColors.SurfaceContainer, PfColors.OnSurface, { onToast("शेखपुरा मंडी: सुधा चूरी-खली ₹840/बोरी · बिनौला खली ₹1,050/बोरी · 28 अक्टूबर से +₹120 (अनुमान)") }, 48)
        }
    }
}

@Composable
private fun FinanceCard(a: FinanceAction, onOpenWhatIf: () -> Unit) {
    val s = strings
    Card(stripe = PfColors.Tertiary) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Chip(s.prioFinance, PfColors.SurfaceContainerHighest, PfColors.OnSurface)
            Chip(s.safeOption, PfColors.PrimaryFixed, PfColors.OnPrimaryFixed, Icons.Default.Verified)
            Spacer(Modifier.weight(1f))
            Chip(s.aiChip, PfColors.TertiaryFixed, PfColors.OnTertiaryFixed, Icons.Default.SmartToy)
        }
        Column {
            Text(a.title, style = PfType.HeadlineSm, color = PfColors.OnSurface)
            Text(a.scheme, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            OptionBox(a.unsafeLabel, a.unsafeEmi, a.unsafeNote, Icons.Default.Warning, PfColors.SecondaryFixed, PfColors.Secondary, PfColors.OnSecondaryContainer)
            OptionBox(a.safeLabel, a.safeEmi, a.safeNote, Icons.Default.TaskAlt, PfColors.PrimaryFixed, PfColors.Primary, PfColors.OnPrimaryFixedVariant)
        }
        BigButton(s.viewSafePlan, Icons.Default.Tune, PfColors.SurfaceContainer, PfColors.OnSurface, onOpenWhatIf)
    }
}

@Composable
private fun OptionBox(label: String, emi: String, note: String, icon: ImageVector, bg: Color, accent: Color, noteColor: Color) {
    Column(Modifier.fillMaxWidth().background(bg, RoundedCornerShape(16.dp)).padding(PfSpacing.Sm), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(4.dp))
            Text(label, style = PfType.LabelMd, color = accent, modifier = Modifier.weight(1f))
            Text(emi, style = PfType.LabelSm, color = accent)
        }
        Text(note, style = PfType.BodyMd, color = noteColor)
    }
}

@Composable
private fun HelpCard(onSpeak: () -> Unit) {
    val s = strings
    Surface(shape = RoundedCornerShape(24.dp), color = PfColors.SurfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            Box(Modifier.size(48.dp).background(PfColors.SurfaceContainerHighest, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.SupportAgent, null, tint = PfColors.Primary, modifier = Modifier.size(28.dp)) }
            Text(s.helpTitle, style = PfType.HeadlineSm, color = PfColors.OnSurface, textAlign = TextAlign.Center)
            Text(s.helpSub, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant, textAlign = TextAlign.Center)
            BigButton(s.explainStepByStep, Icons.Default.RecordVoiceOver, PfColors.SurfaceContainerLowest, PfColors.Primary, onSpeak, 52)
        }
    }
}

@Composable
private fun SpeakNewActionButton(onSpeak: () -> Unit) {
    val s = strings
    Surface(onClick = onSpeak, shape = CircleShape, color = PfColors.PrimaryContainer, shadowElevation = 8.dp, modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 56.dp)) {
        Row(Modifier.padding(horizontal = 20.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Box(Modifier.size(32.dp).background(PfColors.Secondary, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Mic, null, tint = PfColors.OnSecondary, modifier = Modifier.size(20.dp)) }
            Spacer(Modifier.width(12.dp))
            Text(s.speakNewAction, style = PfType.LabelLg, color = PfColors.OnPrimary, textAlign = TextAlign.Center)
        }
    }
}
