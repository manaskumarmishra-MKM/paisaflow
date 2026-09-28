package com.paisaflow.app.feature.home.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisaflow.app.core.model.ActionKind
import com.paisaflow.app.core.model.KoshMetric
import com.paisaflow.app.core.model.MetricIcon
import com.paisaflow.app.core.model.MetricTone
import com.paisaflow.app.core.model.OverdueAlert
import com.paisaflow.app.core.model.TopAction
import com.paisaflow.app.core.model.TwinStatus
import com.paisaflow.app.shared.PulseDot
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

// Green "FACT" chip colours used on the Home tiles (mockup uses Tailwind green-100/800/600)
private val FactBg = Color(0xFFDCFCE7)
private val FactFg = Color(0xFF14532D)
private val FactDot = Color(0xFF16A34A)

/** Brand masthead bar: logo · "PaisaFlow Kosh / Voice-First Khata" · 100% Surakshit. */
@Composable
fun KoshBadgeBar() {
    Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLow, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(horizontal = PfSpacing.Md, vertical = PfSpacing.Sm), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.width(64.dp).height(40.dp).background(PfColors.SurfaceContainerLowest, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.Default.Payments, null, tint = PfColors.Primary, modifier = Modifier.size(26.dp)) }
            Spacer(Modifier.width(PfSpacing.Sm))
            Column(Modifier.weight(1f)) {
                Text("PAISAFLOW KOSH", style = PfType.LabelSm.copy(letterSpacing = 1.sp), color = PfColors.Primary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PulseDot(color = PfColors.Primary, size = 8.dp)
                    Spacer(Modifier.width(4.dp))
                    Text("Voice-First Khata", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                }
            }
            Row(
                Modifier.background(PfColors.PrimaryFixed, CircleShape).padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Verified, null, tint = PfColors.OnPrimaryFixed, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("100% Surakshit", style = PfType.LabelSm, color = PfColors.OnPrimaryFixed)
            }
        }
    }
}

/** "Namaste Sunita ji! 👋" + date pill + question line. */
@Composable
fun Greeting(firstName: String, todayLabel: String) {
    val t = rememberInfiniteTransition(label = "wave")
    val dy by t.animateFloat(0f, -4f, infiniteRepeatable(tween(600), RepeatMode.Reverse), label = "dy")
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Namaste $firstName ji! ", style = PfType.HeadlineMd, color = PfColors.Primary, modifier = Modifier.weight(1f, fill = false))
            Text("👋", style = PfType.HeadlineMd, modifier = Modifier.offset(y = dy.dp))
            Spacer(Modifier.weight(1f))
            Text(
                todayLabel, style = PfType.LabelSm, color = PfColors.OnSurfaceVariant,
                modifier = Modifier.background(PfColors.SurfaceContainer, CircleShape).padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
        Spacer(Modifier.height(PfSpacing.Xs))
        Text("Aaj business mein kya karna hai?", style = PfType.BodyLg.copy(fontWeight = FontWeight.Medium), color = PfColors.OnSurfaceVariant)
    }
}

/** Dominant dark-green voice CTA hero card. */
@Composable
fun VoiceHeroCard(voiceExample: String, onSpeak: () -> Unit, onPlayHelp: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(PfColors.Primary),
    ) {
        // Decorative pulse rings
        Box(Modifier.align(Alignment.TopEnd).offset(x = 80.dp, y = (-80).dp).size(256.dp).background(PfColors.PrimaryContainer.copy(alpha = 0.2f), CircleShape))
        Box(Modifier.align(Alignment.TopEnd).offset(x = 48.dp, y = (-48).dp).size(192.dp).background(PfColors.PrimaryContainer.copy(alpha = 0.4f), CircleShape))

        Column(Modifier.padding(PfSpacing.Lg), verticalArrangement = Arrangement.spacedBy(PfSpacing.Md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    Modifier.background(PfColors.PrimaryFixed, CircleShape).padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.GraphicEq, null, tint = PfColors.OnPrimaryFixed, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("VOICE ASSISTANT", style = PfType.LabelSm, color = PfColors.OnPrimaryFixed)
                }
                Spacer(Modifier.weight(1f))
                Text("Hindi / Hinglish", style = PfType.LabelSm, color = PfColors.PrimaryFixedDim)
            }
            Column {
                Text(
                    buildAnnotatedString {
                        append("PAISAFLOW SE BOLIYE ")
                        withStyle(SpanStyle(color = PfColors.SecondaryFixed, fontSize = 24.sp)) { append("(बोलिए)") }
                    },
                    style = PfType.HeadlineLgMobile, color = Color.White,
                )
                Spacer(Modifier.height(4.dp))
                Text("Koi bhi hisaab ya sawal poochein — Bas bol kar bataiye", style = PfType.BodyMd, color = PfColors.PrimaryFixedDim)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(PfSpacing.Md)) {
                Surface(
                    onClick = onSpeak, shape = CircleShape, color = PfColors.SurfaceContainerLowest,
                    shadowElevation = 6.dp, modifier = Modifier.weight(1f).height(64.dp),
                ) {
                    Row(Modifier.padding(horizontal = PfSpacing.Md), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(44.dp).background(PfColors.SecondaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Mic, null, tint = PfColors.OnSecondaryContainer, modifier = Modifier.size(26.dp))
                        }
                        Spacer(Modifier.width(PfSpacing.Sm))
                        Column {
                            Text("Abhi Boliye", style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = PfColors.Primary)
                            Spacer(Modifier.height(2.dp))
                            Text(voiceExample, style = PfType.LabelSm.copy(fontWeight = FontWeight.Medium), color = PfColors.OnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
                Surface(onClick = onPlayHelp, shape = CircleShape, color = PfColors.PrimaryContainer, shadowElevation = 4.dp, modifier = Modifier.size(56.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.VolumeUp, "Audio playback help", tint = PfColors.PrimaryFixed, modifier = Modifier.size(24.dp))
                    }
                }
            }
        }
    }
}

/** Terracotta overdue banner with "Taqada" action. */
@Composable
fun OverdueBanner(alert: OverdueAlert, onTaqada: () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SecondaryFixed, shadowElevation = 3.dp, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(PfSpacing.Md), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(PfColors.Secondary, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Error, null, tint = PfColors.OnSecondary, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${alert.count} Baaki Udhaar (Overdue)", style = PfType.LabelMd, color = PfColors.OnSecondaryFixed)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "${alert.daysLate} Din", style = PfType.LabelSm, color = PfColors.OnError,
                        modifier = Modifier.background(PfColors.Error, CircleShape).padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                }
                Text(
                    buildAnnotatedString {
                        append("${alert.counterparty} · ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = PfColors.OnSecondaryFixed)) { append("${alert.amount} baaki") }
                    },
                    style = PfType.LabelMd.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSecondaryFixedVariant,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Surface(onClick = onTaqada, shape = RoundedCornerShape(12.dp), color = PfColors.Secondary, shadowElevation = 1.dp) {
                Box(Modifier.height(44.dp).padding(horizontal = 14.dp), contentAlignment = Alignment.Center) {
                    Text("Taqada", style = PfType.LabelMd, color = PfColors.OnSecondary)
                }
            }
        }
    }
}

/** Section header "Kosh Snapshot (व्यापार सार) · Auto-Synced". */
@Composable
fun SectionHeader(title: String, trailing: String? = null, icon: ImageVector = Icons.Default.Insights) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = PfColors.Primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, style = PfType.HeadlineSm, color = PfColors.OnSurface, modifier = Modifier.weight(1f))
        if (trailing != null) Text(trailing, style = PfType.LabelSm.copy(fontWeight = FontWeight.Medium), color = PfColors.OnSurfaceVariant)
    }
}

private fun MetricIcon.vector(): ImageVector = when (this) {
    MetricIcon.PAYMENTS -> Icons.Default.Payments
    MetricIcon.TRENDING_UP -> Icons.Default.TrendingUp
    MetricIcon.INVENTORY -> Icons.Default.Inventory2
    MetricIcon.PENDING -> Icons.Default.PendingActions
    MetricIcon.CHECK_CIRCLE -> Icons.Default.CheckCircle
    MetricIcon.STOREFRONT -> Icons.Default.Storefront
    MetricIcon.ALARM -> Icons.Default.Alarm
    MetricIcon.GROUP -> Icons.Default.Group
}

/** One Kosh Snapshot tile. */
@Composable
fun MetricTile(m: KoshMetric) {
    val (iconBg, iconFg) = when (m.tone) {
        MetricTone.PRIMARY, MetricTone.NEUTRAL -> PfColors.PrimaryFixed to PfColors.OnPrimaryFixed
        MetricTone.WARNING -> PfColors.SecondaryFixed to PfColors.OnSecondaryFixed
        MetricTone.SECONDARY -> PfColors.SurfaceContainerHigh to PfColors.OnSurface
    }
    val valueColor = when (m.tone) {
        MetricTone.PRIMARY -> PfColors.Primary
        MetricTone.WARNING -> PfColors.Secondary
        else -> PfColors.OnSurface
    }
    val suffixColor = when (m.tone) {
        MetricTone.PRIMARY -> FactDot
        MetricTone.NEUTRAL -> PfColors.Primary
        MetricTone.WARNING -> PfColors.Secondary
        MetricTone.SECONDARY -> PfColors.OnSurfaceVariant
    }
    val footerColor = if (m.tone == MetricTone.WARNING) PfColors.Secondary else PfColors.OnSurfaceVariant
    val footerIconColor = if (m.tone == MetricTone.WARNING) PfColors.Secondary else if (m.tone == MetricTone.SECONDARY) PfColors.OnSurfaceVariant else PfColors.Primary

    Surface(shape = RoundedCornerShape(24.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(Modifier.size(44.dp).background(iconBg, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                    Icon(m.icon.vector(), null, tint = iconFg, modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.weight(1f))
                Row(
                    Modifier.background(FactBg, CircleShape).padding(horizontal = 10.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.size(6.dp).background(FactDot, CircleShape))
                    Spacer(Modifier.width(4.dp))
                    Text(m.evidence.display, style = PfType.LabelSm, color = FactFg)
                }
            }
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(m.value, style = PfType.HeadlineLgMobile, color = valueColor)
                    if (m.valueSuffix != null) {
                        Spacer(Modifier.width(4.dp))
                        Text(m.valueSuffix, style = PfType.LabelSm, color = suffixColor, modifier = Modifier.padding(bottom = 6.dp))
                    }
                }
                Text(m.title, style = PfType.LabelMd.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
            }
            Row(Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    m.footer,
                    style = PfType.LabelSm.copy(fontWeight = if (m.tone == MetricTone.WARNING) FontWeight.Bold else FontWeight.Normal),
                    color = footerColor, modifier = Modifier.weight(1f),
                )
                Icon(m.footerIcon.vector(), null, tint = footerIconColor, modifier = Modifier.size(18.dp))
            }
        }
    }
}

/** "🎯 Aaj ke Top 3 Kaam" preview widget. */
@Composable
fun TopActionsPreview(actions: List<TopAction>, onSeeAll: () -> Unit, onAction: (TopAction) -> Unit) {
    Surface(shape = RoundedCornerShape(24.dp), color = PfColors.SurfaceContainerLow, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🎯", style = PfType.BodyXl)
                Spacer(Modifier.width(8.dp))
                Text("Aaj ke Top 3 Kaam", style = PfType.HeadlineSm, color = PfColors.OnSurface, modifier = Modifier.weight(1f))
                Row(Modifier.clip(CircleShape).defaultMinSize(minHeight = 40.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(onClick = onSeeAll, color = Color.Transparent) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Sabhi Dekhein", style = PfType.LabelMd, color = PfColors.Primary)
                            Icon(Icons.Default.ChevronRight, null, tint = PfColors.Primary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
            actions.forEach { a -> TopActionRow(a) { onAction(a) } }
        }
    }
}

@Composable
private fun TopActionRow(a: TopAction, onClick: () -> Unit) {
    val (badgeBg, badgeFg) = when (a.rank) {
        1 -> PfColors.SecondaryFixed to PfColors.OnSecondaryFixed
        2 -> PfColors.PrimaryFixed to PfColors.OnPrimaryFixed
        else -> PfColors.SurfaceContainer to PfColors.OnSurface
    }
    val icon = when (a.kind) {
        ActionKind.SEND -> Icons.AutoMirrored.Filled.Send
        ActionKind.CHECK -> Icons.Default.Check
        ActionKind.CALL -> Icons.Default.Call
    }
    Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLowest, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(28.dp).background(badgeBg, CircleShape), contentAlignment = Alignment.Center) {
                Text("${a.rank}", style = PfType.LabelSm, color = badgeFg)
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Column(Modifier.weight(1f)) {
                Text(a.title, style = PfType.LabelMd, color = PfColors.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(a.subtitle, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Surface(onClick = onClick, shape = CircleShape, color = PfColors.SurfaceContainer) {
                Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                    Icon(icon, a.kind.name, tint = PfColors.Primary, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

/** Living Digital Twin teaser bar. */
@Composable
fun TwinTeaser(twin: TwinStatus, onOpen: () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerHighest, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(PfSpacing.Md), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(PfColors.Primary, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.SmartToy, null, tint = PfColors.OnPrimary, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(twin.name, style = PfType.LabelMd, color = PfColors.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.width(6.dp))
                    Box(Modifier.size(8.dp).background(if (twin.isLive) FactDot else PfColors.Outline, CircleShape))
                }
                Text(twin.status, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant, maxLines = 2)
            }
            Spacer(Modifier.width(PfSpacing.Sm))
            Surface(onClick = onOpen, shape = CircleShape, color = PfColors.PrimaryContainer) {
                Box(Modifier.height(40.dp).padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                    Text("Dekhein", style = PfType.LabelSm, color = PfColors.PrimaryFixed)
                }
            }
        }
    }
}
