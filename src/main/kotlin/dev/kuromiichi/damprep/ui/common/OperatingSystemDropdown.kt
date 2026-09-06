package dev.kuromiichi.damprep.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.kuromiichi.damprep.data.preferences.TargetOperatingSystem

private val SelectorButtonWidth = 128.dp
private val SelectorMenuWidth = 152.dp
private val SelectorMenuOffset = DpOffset(x = -12.dp, y = 4.dp)

@Composable
fun OperatingSystemDropdown(
    selectedOperatingSystem: TargetOperatingSystem?,
    onOperatingSystemSelected: (TargetOperatingSystem) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "Operating system menu arrow",
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopEnd,
    ) {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .height(40.dp)
                .width(SelectorButtonWidth),
            shape = MaterialTheme.shapes.extraLarge,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            Text(selectedOperatingSystem?.label ?: "Sistema")
            Spacer(modifier = Modifier.size(4.dp))
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = if (expanded) {
                    "Cerrar selector de sistema operativo"
                } else {
                    "Cambiar sistema operativo"
                },
                modifier = Modifier
                    .size(20.dp)
                    .rotate(arrowRotation),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(SelectorMenuWidth),
            offset = SelectorMenuOffset,
            shape = MaterialTheme.shapes.large,
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        ) {
            TargetOperatingSystem.entries.forEach { operatingSystem ->
                val selected = operatingSystem == selectedOperatingSystem

                DropdownMenuItem(
                    text = {
                        Text(
                            text = operatingSystem.label,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    },
                    onClick = {
                        expanded = false
                        onOperatingSystemSelected(operatingSystem)
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (selected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                Color.Transparent
                            },
                        ),
                    leadingIcon = {
                        Box(
                            modifier = Modifier.size(20.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (selected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = if (selected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                    ),
                )
            }
        }
    }
}
