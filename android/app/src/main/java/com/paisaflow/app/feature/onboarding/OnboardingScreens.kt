package com.paisaflow.app.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.i18n.AppLanguage
import com.paisaflow.app.core.i18n.strings2
import com.paisaflow.app.core.i18n.strings2For
import com.paisaflow.app.shared.PillButton
import com.paisaflow.app.shared.PillVariant
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType
import kotlinx.coroutines.delay

/** Screen 01 — Splash: brand + tagline, 1.5 s, no gratuitous animation (DESIGN.md §9). */
@Composable
fun SplashScreen(onDone: () -> Unit) {
    val s = strings2
    LaunchedEffect(Unit) { delay(1500); onDone() }
    Box(Modifier.fillMaxSize().background(PfColors.Primary), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(88.dp).background(PfColors.PrimaryFixed, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Payments, null, tint = PfColors.Primary, modifier = Modifier.size(44.dp))
            }
            Spacer(Modifier.height(PfSpacing.Lg))
            Text("PAISAFLOW", style = PfType.HeadlineLg, color = PfColors.OnPrimary)
            Text(s.tagline, style = PfType.BodyLg, color = PfColors.PrimaryFixedDim)
            Spacer(Modifier.height(PfSpacing.Xl))
            Text(s.loop, style = PfType.LabelMd, color = PfColors.PrimaryFixed)
        }
    }
}

/** Screen 02 — Language selection (DESIGN.md §10). Shown once; changeable later from the top bar. */
@Composable
fun LanguageScreen(selected: AppLanguage, onSelect: (AppLanguage) -> Unit, onContinue: () -> Unit) {
    val s = strings2For(selected)
    Column(Modifier.fillMaxSize().background(PfColors.Surface).padding(PfSpacing.Lg), verticalArrangement = Arrangement.Center) {
        Text(s.chooseLanguage, style = PfType.HeadlineMd, color = PfColors.Primary)
        Spacer(Modifier.height(PfSpacing.Lg))
        AppLanguage.entries.forEach { lang ->
            val active = lang == selected
            Surface(
                onClick = { onSelect(lang) }, shape = RoundedCornerShape(16.dp),
                color = if (active) PfColors.PrimaryFixed else PfColors.SurfaceContainerLowest, shadowElevation = if (active) 2.dp else 1.dp,
                modifier = Modifier.fillMaxWidth().padding(bottom = PfSpacing.Sm),
            ) {
                Row(Modifier.padding(PfSpacing.Md).height(32.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(lang.native, style = PfType.BodyLg.copy(fontWeight = FontWeight.SemiBold), color = if (active) PfColors.OnPrimaryFixed else PfColors.OnSurface, modifier = Modifier.weight(1f))
                    if (active) Icon(Icons.Default.CheckCircle, null, tint = PfColors.Primary)
                }
            }
        }
        Spacer(Modifier.height(PfSpacing.Md))
        PillButton(s.continueBtn, onContinue, Modifier.fillMaxWidth(), elevated = true)
    }
}

/** Screen 03 — First-time onboarding: one voice prompt, text fallback (DESIGN.md §11). */
@Composable
fun OnboardingScreen(onSpeak: () -> Unit, onSubmitText: (String) -> Unit) {
    val s = strings2
    var text by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(PfColors.Surface).padding(PfSpacing.Lg), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(s.namaste, style = PfType.HeadlineLgMobile, color = PfColors.Primary)
        Spacer(Modifier.height(PfSpacing.Sm))
        Text(s.onboardIntro, style = PfType.BodyLg, color = PfColors.OnSurfaceVariant, textAlign = TextAlign.Center)
        Spacer(Modifier.height(PfSpacing.Xl))
        Surface(onClick = onSpeak, shape = CircleShape, color = PfColors.Primary, shadowElevation = 10.dp, modifier = Modifier.size(96.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Mic, "Boliye", tint = PfColors.OnPrimary, modifier = Modifier.size(44.dp)) }
        }
        Spacer(Modifier.height(PfSpacing.Sm))
        Text("बोलिए (Boliye)", style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold), color = PfColors.Primary)
        Spacer(Modifier.height(PfSpacing.Md))
        Text(s.onboardPrompt, style = PfType.BodyLg.copy(fontWeight = FontWeight.SemiBold), color = PfColors.OnSurface, textAlign = TextAlign.Center)
        Spacer(Modifier.height(PfSpacing.Xl))
        Text(s.orType, style = PfType.LabelMd, color = PfColors.OnSurfaceVariant)
        Spacer(Modifier.height(PfSpacing.Sm))
        OutlinedTextField(
            value = text, onValueChange = { text = it }, placeholder = { Text(s.typeHint) }, singleLine = true,
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PfColors.Primary, unfocusedBorderColor = PfColors.OutlineVariant),
        )
        Spacer(Modifier.height(PfSpacing.Sm))
        PillButton(s.continueBtn, { if (text.isNotBlank()) onSubmitText(text.trim()) }, Modifier.fillMaxWidth(), PillVariant.NEUTRAL)
    }
}
