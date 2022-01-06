package com.wecom.botfather.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.wecom.botfather.R
import com.wecom.botfather.sdk.BotBean
import com.wecom.botfather.sdk.TextMessage
import com.wecom.botfather.ui.theme.BotFatherTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

/**
 * TODO
 * - 使用模版编辑内容
 * - 展示历史发送过的列表
 */
class ChatActivity : ComponentActivity() {

    private val viewModel: ChatViewModel by inject()

    private val botId: String by lazy {
        intent.getStringExtra(EXTRA_ID)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.queryBotById(botId)?.let {
                setContent {
                    BotFatherTheme {
                        ChatScreen(it, viewModel)
                    }
                }
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
fun ChatScreen(bot: BotBean, viewModel: ChatViewModel) {
    var chatMsg by remember { mutableStateOf(TEMPLATE) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberImagePainter(
                                data = bot.avatar,
                                builder = {
                                    crossfade(true)
                                    placeholder(R.mipmap.ic_launcher)
                                    transformations(CircleCropTransformation())
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp).clip(CircleShape)
                        )
                        Spacer(Modifier.size(12.dp))
                        Column {
                            Text(bot.name)
                            Text(text = "Key: ${bot.id}", fontSize = 12.sp)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.sendMsg(bot.id, TextMessage.Markdown(chatMsg))
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
                    Text("type something...")
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

