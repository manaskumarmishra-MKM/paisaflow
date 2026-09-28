package com.paisaflow.app.feature.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.i18n.strings2
import com.paisaflow.app.shared.PillButton
import com.paisaflow.app.shared.PillVariant
import com.paisaflow.app.shared.SimpleTopBar
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

data class Alert(val icon: ImageVector, val title: String, val body: String)

/** Screen 17 — Alerts: what happened · why it matters · what to do (DESIGN.md §25). No spam. */
@Composable
fun AlertsScreen(onBack: () -> Unit, onView: (Alert) -> Unit, onToast: (String) -> Unit) {
    val s2 = strings2
    val alerts = remember {
        mutableStateListOf(
            Alert(Icons.Default.Warning, "3 ग्राहकों का भुगतान देर से", "₹18,500 बाकी · सबसे पुराना 12 दिन। आज तगादा करने से कल का चारा नकद में आ जाएगा।"),
            Alert(Icons.Default.Payments, "कैश बफ़र कम हो सकता है", "मौजूदा खर्च जारी रहे तो 3.3 महीने का बफ़र घटकर 2.5 महीने रह सकता है।"),
            Alert(Icons.Default.Inventory2, "चारा स्टॉक 3 दिन में ख़त्म", "10 बोरी आज बुक करने पर ₹1,200 की बचत (28 अक्टूबर से भाव बढ़ने का अनुमान)।"),
        )
    }
    Column(Modifier.fillMaxSize().background(PfColors.Surface)) {
        SimpleTopBar(s2.alertsTitle, onBack)
        LazyColumn(contentPadding = PaddingValues(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            items(alerts.size) { i ->
                val a = alerts[i]
                Surface(shape = RoundedCornerShape(20.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(Modifier.size(40.dp).background(PfColors.SecondaryFixed, CircleShape), contentAlignment = Alignment.Center) { Icon(a.icon, null, tint = PfColors.Secondary) }
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(a.title, style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = PfColors.OnSurface)
                                Text(a.body, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            PillButton(s2.view, { onView(a) }, Modifier.weight(1f))
                            PillButton(s2.remindMe, { onToast("⏰ ${s2.remindMe}") }, Modifier.weight(1f), PillVariant.NEUTRAL)
                            PillButton(s2.dismiss, { alerts.removeAt(i) }, Modifier.weight(1f), PillVariant.NEUTRAL)
                        }
                    }
                }
            }
        }
    }
}
