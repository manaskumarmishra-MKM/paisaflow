package com.paisaflow.app.feature.profile

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
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.i18n.AppLanguage
import com.paisaflow.app.core.i18n.strings2
import com.paisaflow.app.shared.SimpleTopBar
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/** Screen 19 — Profile / Business settings (DESIGN.md §27). */
@Composable
fun ProfileScreen(language: AppLanguage, onBack: () -> Unit, onOpenMemory: () -> Unit, onOpenAlerts: () -> Unit, onLanguage: () -> Unit, onToast: (String) -> Unit) {
    val s2 = strings2
    var lock by rememberSaveable { mutableStateOf(true) }
    val rows: List<Triple<ImageVector, String, () -> Unit>> = listOf(
        Triple(Icons.Default.Storefront, s2.businessProfile, { onToast(s2.businessProfile) }),
        Triple(Icons.Default.Payments, s2.financialInfo, { onToast(s2.financialInfo) }),
        Triple(Icons.Default.Inventory2, s2.inventoryMenu, { onToast(s2.inventoryMenu) }),
        Triple(Icons.Default.Group, s2.customers, { onToast(s2.customers) }),
        Triple(Icons.Default.Place, s2.location, { onToast("शेखपुरा, बिहार") }),
        Triple(Icons.Default.Psychology, s2.memoryMenu, onOpenMemory),
        Triple(Icons.Default.Notifications, s2.alertsMenu, onOpenAlerts),
        Triple(Icons.Default.Translate, "${s2.languageMenu} · ${language.native}", onLanguage),
        Triple(Icons.Default.Lock, s2.privacy, { onToast(s2.privacy) }),
        Triple(Icons.AutoMirrored.Filled.HelpOutline, s2.help, { onToast(s2.help) }),
    )
    Column(Modifier.fillMaxSize().background(PfColors.Surface)) {
        SimpleTopBar(s2.profileTitle, onBack)
        LazyColumn(contentPadding = PaddingValues(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            item {
                Row(Modifier.fillMaxWidth().padding(bottom = PfSpacing.Sm), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(56.dp).background(PfColors.PrimaryFixed, CircleShape), contentAlignment = Alignment.Center) { Text("SD", style = PfType.HeadlineSm, color = PfColors.Primary) }
                    Spacer(Modifier.width(PfSpacing.Sm))
                    Column { Text("सुनीता देवी", style = PfType.HeadlineSm, color = PfColors.OnSurface); Text("सुनीता डेयरी · शेखपुरा", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant) }
                }
            }
            items(rows.size) { i ->
                val (icon, label, action) = rows[i]
                Surface(onClick = action, shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(horizontal = PfSpacing.Md, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, null, tint = PfColors.Primary); Spacer(Modifier.width(12.dp))
                        Text(label, style = PfType.LabelLg, color = PfColors.OnSurface, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, null, tint = PfColors.Outline)
                    }
                }
            }
            item {
                Surface(shape = RoundedCornerShape(16.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(horizontal = PfSpacing.Md, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Fingerprint, null, tint = PfColors.Primary); Spacer(Modifier.width(12.dp))
                        Text(s2.lockApp, style = PfType.LabelLg, color = PfColors.OnSurface, modifier = Modifier.weight(1f))
                        Switch(checked = lock, onCheckedChange = { lock = it })
                    }
                }
            }
        }
    }
}
