package com.github.foodiestudio.botfather.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import com.github.foodiestudio.botfather.ABORT_REQUEST_KEY
import com.github.foodiestudio.botfather.BuildConfig
import com.github.foodiestudio.botfather.dataStore
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * TODO
 * - 导出/导入数据
 */
@Destination
@Composable
fun SettingScreen() {
    val context = LocalContext.current

    val prefValue = context.dataStore.data.map {
        it[ABORT_REQUEST_KEY] ?: BuildConfig.DEBUG
    }
    val scope = rememberCoroutineScope()
    SettingContent(prefValue) { newValue ->
        scope.launch {
            context.dataStore.edit {
                it[ABORT_REQUEST_KEY] = newValue
            }
        }
    }
}

@Composable
private fun SettingContent(prefValue: Flow<Boolean>, onValueChange: (Boolean) -> Unit) {
    val abortRequest by prefValue.collectAsState(false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        },
        content = {
            SwitchPreference(
                title = { Text("禁用请求") },
                description = {
                    Text(
                        text = "不进行网络请求，改为 Log 输出",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                },
                checked = abortRequest,
                onCheckedChange = onValueChange,
                modifier = Modifier.clickable {
                    onValueChange(!abortRequest)
                }
            )
        }
    )
}