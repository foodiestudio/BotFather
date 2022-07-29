package com.github.foodiestudio.botfather.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PreferenceWidget() {
    TODO()
}

/**
 * 配合 Preference 使用，分组展示
 */
@Composable
fun PreferenceGroup(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Column {
        title()
        content()
    }
}

@Composable
fun SwitchPreference(
    title: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    description: @Composable (() -> Unit)? = null,
) {
    Row(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            title()
            if (description != null) {
                Spacer(Modifier.size(4.dp))
                description()
            }
        }
        Spacer(Modifier.size(4.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.alignByBaseline()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SwitchPreferencePreview() {
    SwitchPreference(
        title = { Text("hahah") },
        description = { Text(text = "xxxx", fontSize = 12.sp, color = Color.Gray) },
        checked = true,
        onCheckedChange = {},
        modifier = Modifier.clickable {

        }
    )
}