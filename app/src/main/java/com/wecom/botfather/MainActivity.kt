package com.wecom.botfather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.wecom.botfather.sdk.TextMessage
import com.wecom.botfather.sdk.WeComBotHelper
import com.wecom.botfather.ui.theme.BotFatherTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WeComBotHelper.bots.add(MockData.bot)

        setContent {
            BotFatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val scope = rememberCoroutineScope()
                    Greeting("Android") {
                        scope.launch {
                            WeComBotHelper.sendMsgToAll(TextMessage.Markdown("😁"))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, onClick: () -> Unit) {
    Column {
        Text(text = "Hello $name!")
        Button(onClick = onClick) {
            Text("Send")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BotFatherTheme {
        Greeting("Android") {

        }
    }
}