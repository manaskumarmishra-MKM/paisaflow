package com.paisaflow.app.shared

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paisaflow.app.core.i18n.LocalAppLanguage
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/** 80dp top bar: logo + product name + AI badge, business line, language pill, speaker, avatar. */
@Composable
fun AppTopBar(
    businessLabel: String,
    language: com.paisaflow.app.core.i18n.AppLanguage = com.paisaflow.app.core.i18n.AppLanguage.HI,
    onLanguageClick: () -> Unit,
    onSpeakClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Surface(color = PfColors.Surface.copy(alpha = 0.92f), shadowElevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(PfSpacing.TopBarHeight)
                .padding(horizontal = PfSpacing.GutterMobile),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.size(40.dp).background(PfColors.Primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.Default.Payments, null, tint = PfColors.OnPrimary, modifier = Modifier.size(22.dp)) }
            Spacer(Modifier.width(PfSpacing.Sm))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "PaisaFlow",
                        style = PfType.HeadlineSm.copy(letterSpacing = (-0.4).sp),
                        color = PfColors.Primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.width(PfSpacing.Xs))
                    Text(
                        "AI", style = PfType.LabelSm.copy(fontSize = 11.sp), color = PfColors.OnPrimaryFixed,
                        modifier = Modifier.background(PfColors.PrimaryContainer, CircleShape).padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Storefront, null, tint = PfColors.Secondary, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(businessLabel, style = PfType.LabelSm, color = PfColors.OnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Spacer(Modifier.width(PfSpacing.Xs))
            Surface(onClick = onLanguageClick, shape = CircleShape, color = PfColors.SurfaceContainerHigh) {
                Row(
                    Modifier.defaultMinSize(minHeight = 44.dp).padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.Translate, null, tint = PfColors.Primary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(LocalAppLanguage.current.short + "/EN", style = PfType.LabelSm, color = PfColors.OnSurface)
                }
            }
            Spacer(Modifier.width(PfSpacing.Xs))
            Surface(onClick = onSpeakClick, shape = CircleShape, color = PfColors.SurfaceContainerHigh) {
                Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.VolumeUp, "Sunen", tint = PfColors.Primary, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(Modifier.width(PfSpacing.Xs))
            Box(
                Modifier.size(32.dp).background(PfColors.Primary, CircleShape).clickable(onClick = onProfileClick),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.Default.Person, "Profile", tint = PfColors.OnPrimary, modifier = Modifier.size(18.dp)) }
        }
    }
}

/** Small "live" dot that fades in and out. */
@Composable
fun PulseDot(color: androidx.compose.ui.graphics.Color, size: androidx.compose.ui.unit.Dp) {
    val t = rememberInfiniteTransition(label = "pulse")
    val alpha by t.animateFloat(
        initialValue = 1f, targetValue = 0.4f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "alpha",
    )
    Box(Modifier.size(size).alpha(alpha).background(color, CircleShape))
}
