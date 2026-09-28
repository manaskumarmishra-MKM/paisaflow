package com.paisaflow.app.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paisaflow.app.core.model.EvidenceLabel
import com.paisaflow.app.ui.theme.PfColors
import com.paisaflow.app.ui.theme.PfType

/** FACT / OBSERVATION / ESTIMATE / ASSUMPTION / AI INFERENCE chip. Colour = certainty, not decoration. */
@Composable
fun EvidenceChip(label: EvidenceLabel, modifier: Modifier = Modifier) {
    val (bg, fg) = when (label) {
        EvidenceLabel.FACT -> PfColors.PrimaryFixed to PfColors.OnPrimaryFixed
        EvidenceLabel.OBSERVATION -> PfColors.PrimaryFixedDim to PfColors.OnPrimaryFixed
        EvidenceLabel.ESTIMATE -> PfColors.Secondary to PfColors.OnSecondary
        EvidenceLabel.ASSUMPTION -> PfColors.SecondaryFixedDim to PfColors.OnSecondaryFixed
        EvidenceLabel.AI_INFERENCE -> PfColors.TertiaryFixed to PfColors.OnTertiaryFixed
    }
    Text(
        text = label.display,
        style = PfType.LabelSm,
        color = fg,
        modifier = modifier
            .background(bg, CircleShape)
            .padding(horizontal = 8.dp, vertical = 2.dp),
    )
}
