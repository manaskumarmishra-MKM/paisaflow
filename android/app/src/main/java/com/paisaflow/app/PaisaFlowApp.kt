package com.paisaflow.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.paisaflow.app.core.Prefs
import com.paisaflow.app.core.i18n.AppLanguage
import com.paisaflow.app.core.i18n.LocalAppLanguage
import com.paisaflow.app.core.mock.DemoHome
import com.paisaflow.app.feature.actions.ActionsScreen
import com.paisaflow.app.feature.alerts.AlertsScreen
import com.paisaflow.app.feature.evidence.EvidenceModeScreen
import com.paisaflow.app.feature.home.HomeHindiScreen
import com.paisaflow.app.feature.home.HomeScreen
import com.paisaflow.app.feature.khata.VoiceKhataScreen
import com.paisaflow.app.feature.memory.MemoryScreen
import com.paisaflow.app.feature.onboarding.LanguageScreen
import com.paisaflow.app.feature.onboarding.OnboardingScreen
import com.paisaflow.app.feature.onboarding.SplashScreen
import com.paisaflow.app.feature.profile.ProfileScreen
import com.paisaflow.app.feature.simulate.SimulateScreen
import com.paisaflow.app.feature.twin.TwinScreen
import com.paisaflow.app.feature.voice.VoiceConversationScreen
import com.paisaflow.app.shared.AppBottomNav
import com.paisaflow.app.shared.AppTab
import com.paisaflow.app.shared.AppTopBar
import com.paisaflow.app.ui.theme.PfColors
import kotlinx.coroutines.launch

/** Secondary full-screen routes layered over the tab shell. */
enum class Route { NONE, EVIDENCE, MEMORY, KHATA, ALERTS, PROFILE }

/** Splash → (Language → Onboarding on first run) → tab shell + routes, under one app-wide language. */
@Composable
fun PaisaFlowApp() {
    val ctx = LocalContext.current
    val prefs = remember { Prefs(ctx) }
    var language by rememberSaveable { mutableStateOf(prefs.language) }
    var splashDone by rememberSaveable { mutableStateOf(false) }
    var onboarded by rememberSaveable { mutableStateOf(prefs.onboarded) }
    var langChosen by rememberSaveable { mutableStateOf(prefs.onboarded) }
    var tab by rememberSaveable { mutableStateOf(AppTab.HOME) }
    var route by rememberSaveable { mutableStateOf(Route.NONE) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val toast: (String) -> Unit = { msg -> scope.launch { snackbar.currentSnackbarData?.dismiss(); snackbar.showSnackbar(msg) } }
    fun setLanguage(l: AppLanguage) { language = l; prefs.language = l }

    CompositionLocalProvider(LocalAppLanguage provides language) {
        when {
            !splashDone -> { SplashScreen { splashDone = true }; return@CompositionLocalProvider }
            !langChosen -> { LanguageScreen(language, ::setLanguage) { langChosen = true }; return@CompositionLocalProvider }
            !onboarded -> {
                OnboardingScreen(
                    onSpeak = { onboarded = true; prefs.onboarded = true; tab = AppTab.TALK },
                    onSubmitText = { onboarded = true; prefs.onboarded = true; tab = AppTab.TALK; toast(it) },
                )
                return@CompositionLocalProvider
            }
        }

        if (route != Route.NONE) {
            BackHandler { route = Route.NONE }
            when (route) {
                Route.EVIDENCE -> EvidenceModeScreen(onBack = { route = Route.NONE }, onSpeak = { route = Route.NONE; tab = AppTab.TALK }, onToast = toast)
                Route.MEMORY -> MemoryScreen(onBack = { route = Route.NONE }, onCorrect = { toast("Badalna hai — correction event") })
                Route.KHATA -> VoiceKhataScreen(onBack = { route = Route.NONE }, onSpeak = { toast("🎙️ Boliye…") }, transcript = "Ramesh ko ₹850 ka maal diya", onToast = toast)
                Route.ALERTS -> AlertsScreen(onBack = { route = Route.NONE }, onView = { route = Route.NONE; tab = AppTab.KHATA }, onToast = toast)
                Route.PROFILE -> ProfileScreen(language, onBack = { route = Route.NONE }, onOpenMemory = { route = Route.MEMORY },
                    onOpenAlerts = { route = Route.ALERTS }, onLanguage = { setLanguage(language.next()); toast(language.native) }, onToast = toast)
                Route.NONE -> Unit
            }
            return@CompositionLocalProvider
        }

        // Hindi Home (v2 mockup) has its own brand bar; other languages use the v1 Home under the global top bar.
        val hindiHome = tab == AppTab.HOME && language == AppLanguage.HI

        Scaffold(
            containerColor = PfColors.Surface,
            topBar = {
                if (!hindiHome) AppTopBar(
                    businessLabel = DemoHome.data.businessLabel,
                    language = language,
                    onLanguageClick = { setLanguage(language.next()); toast(language.native) },
                    onSpeakClick = { toast("Screen ko padh kar sunaa raha hoon…") },
                    onProfileClick = { route = Route.PROFILE },
                )
            },
            bottomBar = { AppBottomNav(current = tab, onSelect = { tab = it }) },
            snackbarHost = { SnackbarHost(snackbar) },
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                when (tab) {
                    AppTab.HOME -> if (hindiHome) HomeHindiScreen(
                        onSpeak = { tab = AppTab.TALK }, onOpenSimulation = { tab = AppTab.WHAT_IF }, onOpenActions = { tab = AppTab.KHATA },
                        onToggleLanguage = { setLanguage(language.next()); toast(language.native) }, onToast = toast,
                    ) else HomeScreen(
                        onSpeak = { tab = AppTab.TALK }, onOpenActions = { tab = AppTab.KHATA }, onOpenTwin = { tab = AppTab.TWIN }, onToast = toast,
                    )
                    AppTab.TALK -> VoiceConversationScreen(onToast = toast, onExplain = { route = Route.EVIDENCE }, language = language, onLanguage = ::setLanguage)
                    AppTab.TWIN -> TwinScreen(onOpenMemory = { route = Route.MEMORY }, onRunWhatIf = { tab = AppTab.WHAT_IF })
                    AppTab.WHAT_IF -> SimulateScreen(onSpeak = { tab = AppTab.TALK }, onExplain = { route = Route.EVIDENCE }, onToast = toast)
                    AppTab.KHATA -> ActionsScreen(onSpeak = { route = Route.KHATA }, onOpenWhatIf = { tab = AppTab.WHAT_IF }, onToast = toast)
                }
            }
        }
    }
}
