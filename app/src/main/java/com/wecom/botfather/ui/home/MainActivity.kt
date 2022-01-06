package com.wecom.botfather.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.wecom.botfather.R
import com.wecom.botfather.sdk.BotBean
import com.wecom.botfather.sdk.Platform
import com.wecom.botfather.ui.chat.ChatActivity
import com.wecom.botfather.ui.settings.SettingsActivity
import com.wecom.botfather.ui.theme.BotFatherTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * 类似与IM那样，首页展示 Bot 列表，底部有个 FAB(添加)
 *
 * TODO
 * - 列表展示已添加的
 * - 设置页面
 */
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotFatherTheme {
                Content(homeViewModel)
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                homeViewModel.fetchData()
            }
        }
    }
}

@Composable
private fun Content(viewModel: HomeViewModel) {
    val context = LocalContext.current
    val bots by viewModel.bots.observeAsState(emptyList())
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
            Chats(bots) {
                ChatActivity.start(context, it.id, it.platform)
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
}

@Composable
fun Chats(bots: List<BotBean>, modifier: Modifier = Modifier, onClick: (BotBean) -> Unit) {
    LazyColumn(modifier = modifier) {
        items(bots, key = { bot -> bot.id }) { bot ->
            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable(onClick = { onClick(bot) })
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
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
                        modifier = Modifier.size(52.dp).padding(2.dp).clip(CircleShape)
                    )
                    ChatLabel(
                        bot = bot,
                        modifier = Modifier.size(18.dp)
                            .align(Alignment.BottomEnd)
                            .border(
                                BorderStroke(1.dp, Color.White),
                                CircleShape
                            )
                    )
                }
                Spacer(Modifier.size(12.dp))
                Text(text = bot.name)
            }
            Divider()
        }
    }
}

@Composable
fun ChatLabel(bot: BotBean, modifier: Modifier = Modifier) {
    when (bot.platform) {
        Platform.WeCom -> {
            Icon(
                painter = painterResource(id = R.drawable.ic_wecom),
                contentDescription = "wecom bot",
                modifier = modifier,
                tint = Color.Unspecified
            )
        }
        Platform.DingTalk -> {
            Icon(
                painter = painterResource(id = R.drawable.ic_dingtalk),
                contentDescription = "dingtalk bot",
                modifier = modifier,
                tint = Color.Unspecified
            )
        }
        else -> throw IllegalArgumentException("Not support ${bot.platform} yet")
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BotFatherTheme {
        Chats(
            listOf(
                BotBean("111", Platform.WeCom)
            )
        ) {
        }
    }
}