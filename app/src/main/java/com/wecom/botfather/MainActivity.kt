package com.wecom.botfather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.wecom.botfather.mock.MockData
import com.wecom.botfather.sdk.Bot
import com.wecom.botfather.sdk.TextMessage
import com.wecom.botfather.sdk.WeComBotHelper
import com.wecom.botfather.ui.chat.ChatActivity
import com.wecom.botfather.ui.theme.BotFatherTheme
import kotlinx.coroutines.launch

/**
 * 类似与IM那样，首页展示 Bot 列表，底部有个 FAB(添加)
 *
 * TODO
 * - 列表展示已添加的
 * - 设置页面
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WeComBotHelper.bots.add(MockData.bot)

        setContent {
            BotFatherTheme {
                Content()
            }
        }
    }
}

@Composable
private fun Content() {
    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        },
        content = {
            val scope = rememberCoroutineScope()
            Chats(WeComBotHelper.bots.toList()) {
                scope.launch {
                    WeComBotHelper.sendMsgToAll(TextMessage.Markdown("😁"))
                }
            }
        },
        floatingActionButton = {
            val context = LocalContext.current
            FloatingActionButton(onClick = {
                ChatActivity.start(context, MockData.bot.id)
            }) {
                Icon(Icons.Default.Edit, "edit")
            }
        }
    )
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//
//                }
}

@Composable
fun Chats(bots: List<Bot>, onClick: () -> Unit) {
    LazyColumn {
        items(bots, key = { bot -> bot.id }) { bot ->
            Row(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = rememberImagePainter(bot.avatar),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Text(text = bot.name)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BotFatherTheme {
        Chats(
            listOf(
                Bot("111")
            )
        ) {
        }
    }
}