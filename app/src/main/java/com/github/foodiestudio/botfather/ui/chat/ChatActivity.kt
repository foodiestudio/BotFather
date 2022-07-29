package com.github.foodiestudio.botfather.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.github.foodiestudio.application.theme.ApplicationTheme
import com.github.foodiestudio.botfather.R
import com.github.foodiestudio.botfather.sdk.BotBean
import com.github.foodiestudio.botfather.sdk.Platform
import com.github.foodiestudio.botfather.sdk.TextMessage
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * TODO
 * - 使用模版编辑内容
 * - 展示历史发送过的列表
 */
class ChatActivity : ComponentActivity() {

    private val botId: String by lazy {
        intent.getStringExtra(EXTRA_ID)!!
    }

    private val botType: Platform by lazy {
        Platform(intent.getLongExtra(EXTRA_TYPE, 0))!!
    }

    private val viewModel: ChatViewModel by inject { parametersOf(botType) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.sendResult.observe(this, androidx.lifecycle.Observer {
            it.doOnSuccess {
                Toast.makeText(this, "Send Success.", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }.doOnFail {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        lifecycleScope.launch {
            viewModel.queryBotById(botId)?.let {
                setContent {
                    ApplicationTheme {
                        ChatScreen(it, viewModel)
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRA_ID = "BotId"
        private const val EXTRA_TYPE = "BotType"

        fun start(context: Context, id: String, platform: Platform) {
            val starter = Intent(context, ChatActivity::class.java)
                .putExtra(EXTRA_ID, id)
                .putExtra(EXTRA_TYPE, platform.type)
            context.startActivity(starter)
        }
    }
}

/**
 * 企业微信支持额外的 <font color>
 */
private val WeComTEMPLATE = """
        <font color="comment">新年快乐</font>
    """.trimIndent()

@Composable
fun ChatScreen(bot: BotBean, viewModel: ChatViewModel) {
    var chatMsg by remember { mutableStateOf(WeComTEMPLATE) }

    val context = LocalContext.current
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
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                        Spacer(Modifier.size(12.dp))
                        Column {
                            Text(bot.name)
                            Text(
                                text = "Key: ${bot.id}",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (chatMsg.isBlank()) {
                            Toast.makeText(context, "内容不能为空", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.sendMsg(bot.id, TextMessage.Markdown(chatMsg))
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

