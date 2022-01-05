package com.wecom.botfather.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import com.wecom.ABORT_REQUEST_KEY
import com.wecom.botfather.BuildConfig
import com.wecom.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * TODO
 * - 导出/导入数据
 */
class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefValue = dataStore.data.map {
            it[ABORT_REQUEST_KEY] ?: BuildConfig.DEBUG
        }
        setContent {
            val scope = rememberCoroutineScope()
            SettingScreen(prefValue) { newValue ->
                scope.launch {
                    dataStore.edit {
                        it[ABORT_REQUEST_KEY] = newValue
                    }
                }
            }
        }
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, SettingsActivity::class.java)
            context.startActivity(starter)
        }
    }

}

@Composable
private fun SettingScreen(prefValue: Flow<Boolean>, onValueChange: (Boolean) -> Unit) {
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