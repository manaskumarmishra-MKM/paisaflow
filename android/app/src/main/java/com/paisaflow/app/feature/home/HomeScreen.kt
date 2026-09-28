package com.paisaflow.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.paisaflow.app.core.mock.DemoHome
import com.paisaflow.app.core.model.HomeData
import com.paisaflow.app.feature.home.components.Greeting
import com.paisaflow.app.feature.home.components.KoshBadgeBar
import com.paisaflow.app.feature.home.components.MetricTile
import com.paisaflow.app.feature.home.components.OverdueBanner
import com.paisaflow.app.feature.home.components.SectionHeader
import com.paisaflow.app.feature.home.components.TopActionsPreview
import com.paisaflow.app.feature.home.components.TwinTeaser
import com.paisaflow.app.feature.home.components.VoiceHeroCard
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/** Screen 06 — Home / Kosh dashboard. Voice CTA first, then snapshot, Top 3, Twin teaser. */
@Composable
fun HomeScreen(
    data: HomeData = DemoHome.data,
    onSpeak: () -> Unit,
    onOpenActions: () -> Unit,
    onOpenTwin: () -> Unit,
    onToast: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = PfSpacing.Md, vertical = PfSpacing.Md),
        verticalArrangement = Arrangement.spacedBy(PfSpacing.Md),
    ) {
        item { KoshBadgeBar() }
        item { Greeting(data.ownerFirstName, data.todayLabel) }
        item { VoiceHeroCard(data.voiceExample, onSpeak = onSpeak, onPlayHelp = { onToast("Bolne ka tareeka sunaa raha hoon…") }) }
        data.overdue?.let { alert ->
            item { OverdueBanner(alert) { onToast("Taqada draft ban raha hai — aap approve karenge tabhi jayega") } }
        }
        item { SectionHeader("Kosh Snapshot (व्यापार सार)", trailing = "Auto-Synced") }
        items(data.metrics.size) { i -> MetricTile(data.metrics[i]) }
        item { TopActionsPreview(data.topActions, onSeeAll = onOpenActions) { onToast("${it.title} — approval ke baad") } }
        item { TwinTeaser(data.twin, onOpen = onOpenTwin) }
        item {
            Text(
                "Sample data · demo business (not real statistics)",
                style = PfType.LabelSm, color = PfColors.Outline,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
