package com.paisaflow.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Type scale from the mockup (Plus Jakarta Sans headlines / Inter body).
 * Uses the system sans-serif so Devanagari renders correctly everywhere;
 * drop the two font files into res/font/ and swap [Headline]/[Body] to use them.
 */
private val Headline = FontFamily.SansSerif
private val Body = FontFamily.SansSerif

object PfType {
    val HeadlineLg = TextStyle(fontFamily = Headline, fontSize = 36.sp, lineHeight = 44.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.72).sp)
    val HeadlineLgMobile = TextStyle(fontFamily = Headline, fontSize = 28.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.28).sp)
    val HeadlineMd = TextStyle(fontFamily = Headline, fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold)
    val HeadlineSm = TextStyle(fontFamily = Headline, fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold)

    val BodyXl = TextStyle(fontFamily = Body, fontSize = 20.sp, lineHeight = 30.sp, fontWeight = FontWeight.Medium)
    val BodyLg = TextStyle(fontFamily = Body, fontSize = 18.sp, lineHeight = 28.sp, fontWeight = FontWeight.Normal)
    val BodyMd = TextStyle(fontFamily = Body, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal)

    val LabelLg = TextStyle(fontFamily = Body, fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.16.sp)
    val LabelMd = TextStyle(fontFamily = Body, fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.56.sp)
    val LabelSm = TextStyle(fontFamily = Body, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.72.sp)
}

val PfTypography = Typography(
    headlineLarge = PfType.HeadlineLg,
    headlineMedium = PfType.HeadlineMd,
    headlineSmall = PfType.HeadlineSm,
    bodyLarge = PfType.BodyLg,
    bodyMedium = PfType.BodyMd,
    labelLarge = PfType.LabelLg,
    labelMedium = PfType.LabelMd,
    labelSmall = PfType.LabelSm,
)
