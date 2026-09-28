package com.paisaflow.app.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.i18n.Strings
import com.paisaflow.app.core.i18n.strings
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/**
 * Five destinations, "बोलिए" as a raised terracotta mic in the centre
 * (Home v2 mockup). Order: होम · व्यापार · [बोलिए] · अनुमान · खाता.
 */
enum class AppTab(val icon: ImageVector, val activeIcon: ImageVector, val label: (Strings) -> String) {
    HOME(Icons.Outlined.Home, Icons.Filled.Home, { it.navHome }),
    TWIN(Icons.Outlined.Hub, Icons.Filled.Hub, { it.navTwin }),
    TALK(Icons.Filled.Mic, Icons.Filled.Mic, { it.navTalk }),
    WHAT_IF(Icons.Outlined.Analytics, Icons.Filled.Analytics, { it.navWhatIf }),
    KHATA(Icons.Outlined.Checklist, Icons.Filled.Checklist, { it.navActions }),
}

@Composable
fun AppBottomNav(current: AppTab, onSelect: (AppTab) -> Unit) {
    val s = strings
    Surface(color = PfColors.Surface.copy(alpha = 0.96f), shadowElevation = 10.dp) {
        Row(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(PfSpacing.BottomNavHeight)
                .padding(horizontal = PfSpacing.Sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppTab.entries.forEach { tab ->
                val active = tab == current
                if (tab == AppTab.TALK) {
                    Box(Modifier.weight(1f).height(PfSpacing.BottomNavHeight), contentAlignment = Alignment.Center) {
                        Column(Modifier.offset(y = (-8).dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                onClick = { onSelect(tab) }, shape = CircleShape, color = PfColors.Secondary,
                                shadowElevation = 10.dp, modifier = Modifier.size(64.dp).semantics { selected = active },
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Filled.Mic, "बोलकर हिसाब दर्ज करें", tint = PfColors.OnSecondary, modifier = Modifier.size(32.dp))
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(tab.label(s), style = PfType.LabelSm, color = PfColors.Secondary)
                        }
                    }
                } else {
                    val color = if (active) PfColors.Primary else PfColors.OnSurfaceVariant
                    Box(
                        Modifier
                            .weight(1f)
                            .height(PfSpacing.TouchTarget)
                            .clickable { onSelect(tab) }
                            .semantics { selected = active },
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(if (active) tab.activeIcon else tab.icon, null, tint = color, modifier = Modifier.size(26.dp))
                            Spacer(Modifier.height(2.dp))
                            Text(tab.label(s), style = PfType.LabelSm.copy(fontWeight = if (active) FontWeight.Bold else FontWeight.SemiBold), color = color)
                        }
                    }
                }
            }
        }
    }
}
