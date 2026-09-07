package dev.kuromiichi.damprep.ui.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatingSystemSelector(
    selectedOperatingSystem: TargetOperatingSystem?,
    onOperatingSystemSelected: (TargetOperatingSystem) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        TargetOperatingSystem.entries.forEachIndexed { index, operatingSystem ->
            SegmentedButton(
                selected = operatingSystem == selectedOperatingSystem,
                onClick = { onOperatingSystemSelected(operatingSystem) },
                shape =
                    SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = TargetOperatingSystem.entries.size,
                    ),
            ) {
                Text(operatingSystem.label)
            }
        }
    }
}
