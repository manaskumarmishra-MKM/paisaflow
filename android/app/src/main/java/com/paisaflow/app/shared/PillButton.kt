package com.paisaflow.app.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

enum class PillVariant { PRIMARY, NEUTRAL, SECONDARY }

/** 56dp-tall pill button — the standard PaisaFlow CTA shape. */
@Composable
fun PillButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PillVariant = PillVariant.PRIMARY,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    elevated: Boolean = false,
) {
    val (bg, fg, iconColor, style) = when (variant) {
        PillVariant.PRIMARY -> Quad(PfColors.Primary, PfColors.OnPrimary, PfColors.OnPrimary, PfType.LabelLg)
        PillVariant.NEUTRAL -> Quad(PfColors.SurfaceContainerHigh, PfColors.OnSurface, PfColors.Outline, PfType.LabelMd)
        PillVariant.SECONDARY -> Quad(PfColors.SecondaryFixed, PfColors.OnSecondaryFixed, PfColors.Secondary, PfType.LabelMd)
    }
    Surface(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = PfSpacing.TouchTarget),
        shape = CircleShape,
        color = bg,
        contentColor = fg,
        shadowElevation = if (elevated) 6.dp else 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = PfSpacing.Md, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            if (leadingIcon != null) {
                Icon(leadingIcon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
            }
            Text(label, style = style, color = fg, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (trailingIcon != null) {
                Spacer(Modifier.width(PfSpacing.Sm))
                Icon(trailingIcon, contentDescription = null, tint = fg, modifier = Modifier.size(22.dp))
            }
        }
    }
}

private data class Quad<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
