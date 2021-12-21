package com.wecom.botfather

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.wecom.botfather.mock.MockData
import com.wecom.botfather.sdk.Bot
import com.wecom.botfather.sdk.WeComBotHelper
import com.wecom.botfather.ui.chat.ChatActivity
import com.wecom.botfather.ui.settings.SettingsActivity
import com.wecom.botfather.ui.theme.BotFatherTheme

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
    val context = LocalContext.current
    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = {
                        SettingsActivity.start(context)
                    }) {
                        Icon(Icons.Default.Settings, "settings")
                    }
                })
        },
        content = {
            Chats(WeComBotHelper.bots.toList()) {
                ChatActivity.start(context, MockData.bot.id)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Toast.makeText(context, "TODO Add a bot", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    )
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//
//                }
}

@Composable
fun Chats(bots: List<Bot>, onClick: (String) -> Unit) {
    LazyColumn {
        items(bots, key = { bot -> bot.id }) { bot ->
            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable(onClick = { onClick(bot.id) })
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                )
                Spacer(Modifier.size(8.dp))
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