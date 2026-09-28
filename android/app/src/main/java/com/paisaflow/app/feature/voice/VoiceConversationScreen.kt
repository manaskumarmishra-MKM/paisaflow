package com.paisaflow.app.feature.voice

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paisaflow.app.core.i18n.AppLanguage
import com.paisaflow.app.feature.voice.components.ActionZone
import com.paisaflow.app.feature.voice.components.BrainCard
import com.paisaflow.app.feature.voice.components.ClarificationCard
import com.paisaflow.app.feature.voice.components.ContextPill
import com.paisaflow.app.feature.voice.components.LanguageChips
import com.paisaflow.app.feature.voice.components.MicHero
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/** Screen 04/05 — Voice Conversation + Confirmation + one clarification question. Live mic via Bhashini. */
@Composable
fun VoiceConversationScreen(
    vm: VoiceViewModel = viewModel(),
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onToast: (String) -> Unit,
    onExplain: () -> Unit,
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val u = state.understanding
    val context = LocalContext.current

    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) vm.onMicTap() else onToast("Mic ki anumati nahi mili. Type karke batayein.")
    }
    val micTap: () -> Unit = {
        val ok = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        if (ok) vm.onMicTap() else permission.launch(Manifest.permission.RECORD_AUDIO)
    }

    LaunchedEffect(state.message) { state.message?.let { onToast(it); vm.messageShown() } }
    LaunchedEffect(language) { vm.setLanguage(language.bhashini) }
    LaunchedEffect(language) { vm.setLanguage(language.bhashini) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = PfSpacing.GutterMobile, vertical = PfSpacing.Md),
        verticalArrangement = Arrangement.spacedBy(PfSpacing.Md),
    ) {
        if (u != null) item { ContextPill(step = u.onboardingStep, totalSteps = u.onboardingTotal) }
        item { LanguageChips(selected = language, onSelect = onLanguageChange) }
        item {
            MicHero(
                voiceState = state.voiceState,
                transcript = u?.transcript,
                onMicClick = micTap,
                onReplayClick = { u?.transcript?.let(vm::speak) },
            )
        }
        if (u != null) {
            item { BrainCard(facts = u.facts, readBack = u.readBack, onPlayReadBack = { vm.speak(u.readBack) }) }
            item {
                ClarificationCard(
                    question = u.nextQuestion,
                    selected = state.selectedOption,
                    onSelect = vm::selectOption,
                    onVoiceClick = micTap,
                    onPlayQuestion = { vm.speak(u.nextQuestion.question) },
                )
            }
        }
        item {
            ActionZone(
                onConfirm = { onToast("Saved: ${state.selectedOption} ${u?.nextQuestion?.unitLabel ?: ""}. Next question coming…") },
                onChange = { onToast("Badalna hai — correction flow") },
                onExplain = onExplain,
            )
        }
        if (state.usingSampleData) {
            item {
                Text(
                    "Sample data · demo business (not real statistics)",
                    style = PfType.LabelSm, color = PfColors.Outline,
                    textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
