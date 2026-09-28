package com.paisaflow.app.feature.simulate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paisaflow.app.core.model.LoanSimResponse
import com.paisaflow.app.core.model.SafePlanOut
import com.paisaflow.app.core.model.ScenarioOut
import com.paisaflow.app.core.model.inr
import com.paisaflow.app.shared.PillButton
import com.paisaflow.app.shared.PillVariant
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfSpacing
import com.paisaflow.app.ui.theme.PfType

private val AmberBg = Color(0xFFFEF3C7); private val AmberFg = Color(0xFF92400E)
private val RedBg = Color(0xFFFEE2E2); private val RedFg = Color(0xFF991B1B); private val Red = Color(0xFFB91C1C)
private val GreenBg = Color(0xFFDCFCE7); private val GreenFg = Color(0xFF14532D)

private fun statusChip(status: String): Triple<Color, Color, String> = when (status) {
    "DEFICIT" -> Triple(RedBg, RedFg, "DEFICIT RED")
    "TIGHT" -> Triple(AmberBg, AmberFg, "TIGHT AMBER")
    else -> Triple(GreenBg, GreenFg, "SAFE GREEN")
}

/** Screen 09/10 — What-If Simulator + Scenario Results (DESIGN.md §17–18), live from the finance engine. */
@Composable
fun SimulateScreen(
    vm: SimulateViewModel = viewModel(),
    onSpeak: () -> Unit,
    onExplain: () -> Unit,
    onToast: (String) -> Unit,
) {
    val st by vm.state.collectAsStateWithLifecycle()
    val r = st.result
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = PfSpacing.Md, vertical = PfSpacing.Md),
        verticalArrangement = Arrangement.spacedBy(PfSpacing.Md),
    ) {
        item { HeaderRow() }
        item { QuestionCard(r.question, onListen = { onToast("नतीजा सुना रहा हूँ (Hindi/Bhojpuri)…") }) }
        item { LoanSliderCard(st.request.loan, st.request.newUnits, st.request.annualRatePct, st.request.tenureMonths, r, st.loading, vm::setLoan) }
        if (r.winterSurplusAfterEmi < 0) item { WarningCard(r) }
        item { ScenariosHeader() }
        items(r.scenarios.size) { i -> ScenarioCard(i + 1, r.scenarios[i]) }
        r.safePlan?.let { sp -> item { SafePlanCard(sp, onTest = { vm.applySafePlan(); onToast("सुरक्षित प्लान लागू — नतीजे अपडेट हो रहे हैं") }) } }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                PillButton("आवाज़ में समझें", { onToast("आवाज़ में समझा रहा हूँ…") }, Modifier.weight(1f), PillVariant.NEUTRAL, Icons.Default.Campaign)
                PillButton("समझ नहीं आया?", onExplain, Modifier.weight(1f), PillVariant.SECONDARY, Icons.AutoMirrored.Filled.HelpOutline)
            }
        }
        item { PillButton("बोलकर नया परिदृश्य जांचें (Speak New Scenario)", onSpeak, Modifier.fillMaxWidth(), PillVariant.PRIMARY, Icons.Default.Mic, elevated = true) }
        item { PillButton("विवरण व वित्तीय गणित देखें (View Formula & Breakdown)", { onToast("Reducing-balance EMI · 12-month projection · 4 stress cases") }, Modifier.fillMaxWidth(), PillVariant.NEUTRAL, Icons.Default.Calculate) }
        item {
            Text(
                (if (st.usingSampleData) "Sample data · " else "") + r.note,
                style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.Outline, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun HeaderRow() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("लाइव अनुमान · SCENARIO SIMULATOR", style = PfType.LabelSm, color = PfColors.Primary, modifier = Modifier.weight(1f))
        Row(Modifier.background(PfColors.TertiaryFixed, CircleShape).padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Psychology, null, tint = PfColors.OnTertiaryFixed, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text("AI Inferred", style = PfType.LabelSm, color = PfColors.OnTertiaryFixed)
        }
    }
}

@Composable
private fun QuestionCard(question: String, onListen: () -> Unit) {
    Column {
        Text(question, style = PfType.HeadlineMd, color = PfColors.OnSurface)
        Spacer(Modifier.height(4.dp))
        Text("सुनीता जी, 6 नई मुर्राह भैंस/गायें खरीदने पर आने वाले 5 साल के मुनाफ़े और जोखिम की जांच:", style = PfType.BodyMd, color = PfColors.OnSurfaceVariant)
        Spacer(Modifier.height(PfSpacing.Sm))
        Surface(onClick = onListen, shape = RoundedCornerShape(16.dp), color = PfColors.Primary, modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(PfSpacing.Sm), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).background(PfColors.PrimaryFixed, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.VolumeUp, null, tint = PfColors.Primary, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.width(PfSpacing.Sm))
                Column {
                    Text("सिमुलेशन का नतीजा सुनें (Hindi/Bhojpuri)", style = PfType.LabelMd, color = PfColors.OnPrimary)
                    Text("मुनीम जी द्वारा 45 सेकंड की सीधी आवाज़ व्याख्या", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.PrimaryFixedDim)
                }
            }
        }
    }
}

@Composable
private fun LoanSliderCard(loan: Int, units: Int, rate: Double, tenure: Int, r: LoanSimResponse, loading: Boolean, onLoan: (Int) -> Unit) {
    Surface(shape = RoundedCornerShape(24.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Tune, null, tint = PfColors.Primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Column(Modifier.weight(1f)) {
                    Text("प्रस्तावित लोन व विस्तार", style = PfType.HeadlineSm, color = PfColors.OnSurface)
                    Text("Loan & Dairy Expansion Slider", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                }
                Text("अनुमान [ESTIMATE]", style = PfType.LabelSm, color = PfColors.OnSecondaryFixed,
                    modifier = Modifier.background(PfColors.SecondaryFixed, CircleShape).padding(horizontal = 8.dp, vertical = 3.dp))
            }
            Spacer(Modifier.height(PfSpacing.Md))
            Row(verticalAlignment = Alignment.Bottom) {
                Column(Modifier.weight(1f)) {
                    Text("कुल ऋण राशि (Principal)", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                    Text(loan.inr(), style = PfType.HeadlineLgMobile, color = PfColors.Primary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Row(Modifier.background(PfColors.PrimaryFixed, CircleShape).padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Pets, null, tint = PfColors.OnPrimaryFixed, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("+$units गायें (कुल ${4 + units} मुर्राह)", style = PfType.LabelSm, color = PfColors.OnPrimaryFixed)
                    }
                    if (loan > r.safeLimit) Text("उच्च जोखिम सीमा", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = Red)
                }
            }
            Slider(
                value = loan.toFloat(), onValueChange = { onLoan((it / 50_000f).toInt() * 50_000) },
                valueRange = 200_000f..1_200_000f, steps = 19,
                colors = SliderDefaults.colors(thumbColor = if (loan > r.safeLimit) PfColors.Secondary else PfColors.Primary, activeTrackColor = if (loan > r.safeLimit) PfColors.Secondary else PfColors.Primary),
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("₹2 लाख", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                Spacer(Modifier.weight(1f))
                if (r.safeLimit > 0) Row(Modifier.background(GreenBg, CircleShape).padding(horizontal = 8.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Verified, null, tint = GreenFg, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("${r.safeLimit.inr()} सुरक्षित सीमा", style = PfType.LabelSm, color = GreenFg)
                }
                Spacer(Modifier.weight(1f))
                Text("₹12 लाख", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
            }
            if (loading) LinearProgressIndicator(Modifier.fillMaxWidth().padding(top = 6.dp), color = PfColors.Primary, trackColor = PfColors.SurfaceContainerHigh)
            Spacer(Modifier.height(PfSpacing.Sm))
            Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                Stat("ब्याज दर", "${rate}%", "वार्षिक (p.a.)", Modifier.weight(1f))
                Stat("अवधि", "${tenure / 12} वर्ष", "$tenure किश्तें", Modifier.weight(1f))
                Stat("मासिक EMI", r.emi.inr(), "प्रति माह", Modifier.weight(1f), highlight = true)
            }
        }
    }
}

@Composable
private fun Stat(label: String, value: String, sub: String, modifier: Modifier = Modifier, highlight: Boolean = false) {
    Column(modifier.background(if (highlight) PfColors.PrimaryFixed else PfColors.SurfaceContainerLow, RoundedCornerShape(12.dp)).padding(10.dp)) {
        Text(label, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant, maxLines = 1)
        Text(value, style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = if (highlight) PfColors.OnPrimaryFixed else PfColors.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(sub, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant, maxLines = 1)
    }
}

@Composable
private fun WarningCard(r: LoanSimResponse) {
    val base = r.scenarios.first { it.key == "base" }
    val winter = r.scenarios.first { it.key == "winter" }
    Surface(shape = RoundedCornerShape(24.dp), color = RedBg, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(Modifier.size(40.dp).background(Red, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.CrisisAlert, null, tint = Color.White, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.width(PfSpacing.Sm))
                Column {
                    Text("चेतावनी: यह लोन सर्दियों में भारी नकद घाटा देगा!", style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = RedFg)
                    Text("[DEFICIT RED] • गंभीर नकद ख़तरा", style = PfType.LabelSm, color = Red)
                }
            }
            Text(
                "नवंबर से जनवरी में गायों का प्राकृतिक दूध उत्पादन गिर जाता है। ${r.emi.inr()} की EMI उस समय आपकी कुल मासिक बचत (${winter.surplus.inr()}) से ज़्यादा हो जाएगी!",
                style = PfType.BodyMd, color = RedFg,
            )
            CompareRow(Icons.Default.WbSunny, "सामान्य दिनों में शुद्ध बचत:", base.surplus.inr(), "EMI बोझ: ${base.repaymentBurdenPct}%", PfColors.OnSurface)
            CompareRow(Icons.Default.AcUnit, "सर्दियों (Dec Lean) में बचत:", winter.surplus.inr(), "EMI बोझ: ${winter.repaymentBurdenPct}%", Red, "दिवालिया ख़तरा (Insolvent)")
            Row(Modifier.fillMaxWidth().background(Red, RoundedCornerShape(12.dp)).padding(PfSpacing.Sm), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.TrendingDown, null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("हर महीने जेब से नकद घाटा:", style = PfType.LabelMd, color = Color.White, modifier = Modifier.weight(1f))
                Text("${r.winterSurplusAfterEmi.inr()}/माह", style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold), color = Color.White)
            }
        }
    }
}

@Composable
private fun CompareRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, sub: String, valueColor: Color, warn: String? = null) {
    Row(Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp)).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = valueColor, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(label, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
            if (warn != null) Text(warn, style = PfType.LabelSm, color = Red)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(value, style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold), color = valueColor)
            Text(sub, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
        }
    }
}

@Composable
private fun ScenariosHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.GridView, null, tint = PfColors.Primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(6.dp))
        Text("4 वित्तीय स्थितियां (Stress Scenarios)", style = PfType.HeadlineSm, color = PfColors.OnSurface, modifier = Modifier.weight(1f))
        Text("PRD §13 मानक", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
    }
}

@Composable
private fun ScenarioCard(n: Int, s: ScenarioOut) {
    val (chipBg, chipFg, chipText) = statusChip(s.status)
    val deficit = s.surplusAfterEmi < 0
    Surface(shape = RoundedCornerShape(20.dp), color = PfColors.SurfaceContainerLowest, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(28.dp).background(if (deficit) RedBg else PfColors.SurfaceContainerHigh, CircleShape), contentAlignment = Alignment.Center) {
                    Text("$n", style = PfType.LabelSm, color = if (deficit) RedFg else PfColors.OnSurface)
                }
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(s.title, style = PfType.LabelLg.copy(fontWeight = FontWeight.Bold), color = PfColors.OnSurface)
                    s.subtitle?.let { Text(it, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant) }
                }
                Text(chipText, style = PfType.LabelSm, color = chipFg, modifier = Modifier.background(chipBg, CircleShape).padding(horizontal = 8.dp, vertical = 3.dp))
            }
            if (s.key == "base") {
                Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                    Stat("कुल बिक्री", s.revenue.inr(), "प्रति माह", Modifier.weight(1f))
                    Stat("चारा व संचालन खर्च", s.costs.inr(), "प्रति माह", Modifier.weight(1f))
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (deficit) "EMI चुकाने के बाद शुद्ध घाटा:" else "EMI चुकाने के बाद बचत:", style = PfType.BodyMd, color = PfColors.OnSurfaceVariant, modifier = Modifier.weight(1f))
                Text(
                    (if (deficit) "" else "+") + "${s.surplusAfterEmi.inr()}/माह",
                    style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold), color = if (deficit) Red else PfColors.Primary,
                )
            }
            s.note?.let { Text(it, style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = if (deficit) Red else PfColors.OnSurfaceVariant) }
        }
    }
}

@Composable
private fun SafePlanCard(sp: SafePlanOut, onTest: () -> Unit) {
    Surface(shape = RoundedCornerShape(24.dp), color = PfColors.PrimaryFixed, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(PfSpacing.Md), verticalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VerifiedUser, null, tint = PfColors.Primary, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(6.dp))
                Column(Modifier.weight(1f)) {
                    Text("मुनीम जी की सुरक्षित सलाह", style = PfType.HeadlineSm, color = PfColors.OnPrimaryFixed)
                    Text("Safe Borrowability Recommendation", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnPrimaryFixedVariant)
                }
                Text("सुरक्षित विकल्प (Safe Plan)", style = PfType.LabelSm, color = PfColors.OnPrimary,
                    modifier = Modifier.background(PfColors.Primary, CircleShape).padding(horizontal = 8.dp, vertical = 3.dp))
            }
            Text("“${sp.advice}”", style = PfType.BodyMd, color = PfColors.OnPrimaryFixed)
            Row(horizontalArrangement = Arrangement.spacedBy(PfSpacing.Sm)) {
                Column(Modifier.weight(1f).background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(12.dp)).padding(10.dp)) {
                    Text("सुरक्षित मासिक EMI", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                    Text(sp.emi.inr(), style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold), color = PfColors.Primary)
                }
                Column(Modifier.weight(1f).background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(12.dp)).padding(10.dp)) {
                    Text("सर्दियों में भी बचत", style = PfType.LabelSm.copy(fontWeight = FontWeight.Normal), color = PfColors.OnSurfaceVariant)
                    Text("+${sp.winterSurplusAfterEmi.inr()}/माह", style = PfType.HeadlineSm.copy(fontWeight = FontWeight.Bold), color = GreenFg)
                }
            }
            Surface(onClick = onTest, shape = CircleShape, color = PfColors.Primary, modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = PfSpacing.TouchTarget)) {
                Row(Modifier.padding(horizontal = PfSpacing.Md), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.ThumbUp, null, tint = PfColors.OnPrimary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("${sp.loan.inr()} वाला सुरक्षित प्लान देखें (Test Safe Plan)", style = PfType.LabelMd, color = PfColors.OnPrimary, textAlign = TextAlign.Center)
                }
            }
        }
    }
}
