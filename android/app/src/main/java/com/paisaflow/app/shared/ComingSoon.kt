package com.paisaflow.app.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

/** Meaningful empty state for tabs not built yet (DESIGN.md §33). */
@Composable
fun ComingSoon(icon: ImageVector, title: String, hint: String) {
    Box(Modifier.fillMaxSize().padding(PfSpacing.Lg), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.size(72.dp).background(PfColors.PrimaryFixed, CircleShape),
                contentAlignment = Alignment.Center,
            ) { Icon(icon, null, tint = PfColors.Primary, modifier = Modifier.size(36.dp)) }
            Spacer(Modifier.height(PfSpacing.Md))
            Text(title, style = PfType.HeadlineSm, color = PfColors.Primary)
            Spacer(Modifier.height(PfSpacing.Xs))
            Text(hint, style = PfType.BodyMd, color = PfColors.OnSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}
