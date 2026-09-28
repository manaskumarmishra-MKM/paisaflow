package com.paisaflow.app.feature.voice.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.i18n.AppLanguage
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfType

/** Language selector: हिंदी · Hinglish · English · भोजपुरी · मगही · मैथिली (app-wide). */
@Composable
fun LanguageChips(selected: AppLanguage, onSelect: (AppLanguage) -> Unit) {
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AppLanguage.entries.forEach { lang ->
            val active = lang == selected
            Surface(onClick = { onSelect(lang) }, shape = CircleShape, color = if (active) PfColors.Primary else PfColors.SurfaceContainerHigh) {
                Text(lang.native, style = PfType.LabelMd, color = if (active) PfColors.OnPrimary else PfColors.OnSurface,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
            }
        }
    }
}
