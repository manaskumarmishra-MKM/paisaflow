package com.paisaflow.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = PfColors.Primary,
    onPrimary = PfColors.OnPrimary,
    primaryContainer = PfColors.PrimaryContainer,
    onPrimaryContainer = PfColors.OnPrimaryContainer,
    inversePrimary = PfColors.InversePrimary,
    secondary = PfColors.Secondary,
    onSecondary = PfColors.OnSecondary,
    secondaryContainer = PfColors.SecondaryContainer,
    onSecondaryContainer = PfColors.OnSecondaryContainer,
    tertiary = PfColors.Tertiary,
    onTertiary = PfColors.OnTertiary,
    tertiaryContainer = PfColors.TertiaryContainer,
    onTertiaryContainer = PfColors.OnTertiaryContainer,
    error = PfColors.Error,
    onError = PfColors.OnError,
    errorContainer = PfColors.ErrorContainer,
    onErrorContainer = PfColors.OnErrorContainer,
    background = PfColors.Surface,
    onBackground = PfColors.OnSurface,
    surface = PfColors.Surface,
    onSurface = PfColors.OnSurface,
    surfaceVariant = PfColors.SurfaceVariant,
    onSurfaceVariant = PfColors.OnSurfaceVariant,
    surfaceTint = PfColors.SurfaceTint,
    inverseSurface = PfColors.InverseSurface,
    inverseOnSurface = PfColors.InverseOnSurface,
    outline = PfColors.Outline,
    outlineVariant = PfColors.OutlineVariant,
    surfaceBright = PfColors.SurfaceBright,
    surfaceDim = PfColors.SurfaceDim,
    surfaceContainer = PfColors.SurfaceContainer,
    surfaceContainerHigh = PfColors.SurfaceContainerHigh,
    surfaceContainerHighest = PfColors.SurfaceContainerHighest,
    surfaceContainerLow = PfColors.SurfaceContainerLow,
    surfaceContainerLowest = PfColors.SurfaceContainerLowest,
)

private val PfShapes = Shapes(
    small = RoundedCornerShape(PfRadius.Base),
    medium = RoundedCornerShape(PfRadius.Base),
    large = RoundedCornerShape(PfRadius.Lg),
    extraLarge = RoundedCornerShape(PfRadius.Xl),
)

@Composable
fun PaisaFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightScheme,
        typography = PfTypography,
        shapes = PfShapes,
        content = content,
    )
}
