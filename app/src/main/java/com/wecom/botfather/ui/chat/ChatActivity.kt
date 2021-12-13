package com.wecom.botfather.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wecom.botfather.sdk.Bot
import com.wecom.botfather.sdk.TextMessage
import com.wecom.botfather.sdk.WeComBotHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * TODO
 * - 使用模版编辑内容
 * - 展示历史发送过的列表
 */
class ChatActivity : ComponentActivity() {

    private val botId: String by lazy {
        intent.getStringExtra(EXTRA_ID)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeComBotHelper.queryBot(botId)?.let {
                ChatScreen(it)
            }
        }
    }

    companion object {
        private const val EXTRA_ID = "BotId"

        fun start(context: Context, id: String) {
            val starter = Intent(context, ChatActivity::class.java)
                .putExtra(EXTRA_ID, id)
            context.startActivity(starter)
        }
    }
}

private val TEMPLATE = """
        ## 群人数周变化
        最近一次更新: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}
        > Tuya 大群：<font color="comment">3508(-17)</font>
        > 智慧商业大群：<font color="comment">543(-7)</font> 
        > APP: <font color="comment">255(-1)</font>
    """.trimIndent()

@Composable
fun ChatScreen(bot: Bot) {
    var chatMsg by remember { mutableStateOf(TEMPLATE) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(bot.name)
                        Text(text = "Key: ${bot.id}", fontSize = 12.sp)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            WeComBotHelper.sendMsg(bot.id, TextMessage.Markdown(chatMsg))
                        }
                    }) {
                        Icon(Icons.Default.Send, "send")
                    }
                })
        },
        content = {
            Column(
                Modifier
                    .padding(it.calculateTopPadding())
                    .fillMaxWidth()
            ) {

            }
            OutlinedTextField(
                placeholder = {
                    Text("type something freely...")
                },
                label = {
                    Text("Content")
                },
                value = chatMsg,
                onValueChange = { chatMsg = it },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            )

        }
    )
}

