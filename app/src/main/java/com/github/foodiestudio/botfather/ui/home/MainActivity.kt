package com.github.foodiestudio.botfather.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.ViewModelInitializer
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.github.foodiestudio.application.theme.ApplicationTheme
import com.github.foodiestudio.botfather.R
import com.github.foodiestudio.botfather.T
import com.github.foodiestudio.botfather.data.Bot
import com.github.foodiestudio.botfather.sdk.BotBean
import com.github.foodiestudio.botfather.sdk.Platform
import com.github.foodiestudio.botfather.ui.NavGraphs
import com.github.foodiestudio.botfather.ui.chat.ChatActivity
import com.github.foodiestudio.botfather.ui.destinations.SettingScreenDestination
import com.github.foodiestudio.botfather.ui.theme.DefaultTransition
import com.github.kkoshin.ctc.compose.山茶红
import com.github.kkoshin.ctc.compose.月白
import com.github.kkoshin.ctc.compose.蓝绿
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * 类似与IM那样，首页展示 Bot 列表，底部有个 FAB(添加)
 *
 * TODO
 * - 列表展示已添加的
 * - 设置页面
 */
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val engine = rememberAnimatedNavHostEngine()
            ApplicationTheme {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    engine = engine,
                )
            }
        }
    }
}

@Destination(style = DefaultTransition::class)
@RootNavGraph(start = true)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navigator: DestinationsNavigator
) {
    val viewModel = viewModel<HomeViewModel>()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val bots by viewModel.bots.observeAsState(emptyList())
    val scope = rememberCoroutineScope()

    scope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.fetchData()
        }
    }

    Scaffold(
        backgroundColor = 月白,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = {
                        navigator.navigate(SettingScreenDestination)
                    }) {
                        Icon(Icons.Default.Settings, "settings")
                    }
                })
        },
        content = {
            Chats(bots,
                modifier = Modifier
                    .wrapContentHeight(),
                onClick = { bot ->
                    ChatActivity.start(context, bot.id, bot.platform)
                }, onDelete = { id ->
                    viewModel.removeItem(id)
                })
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Chats(
    bots: List<BotBean>,
    modifier: Modifier = Modifier,
    onClick: (BotBean) -> Unit,
    onDelete: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(bots, key = { bot -> bot.id }) { bot ->
            val dismissState = rememberDismissState(confirmStateChange = {
                if (it == DismissValue.DismissedToStart) {
                    onDelete(bot.id)
                }
                true
            })
            SwipeToDismiss(
                state = dismissState,
                dismissThresholds = { FractionalThreshold(0.33f) },
                background = {
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss

                    val color by animateColorAsState(
                        targetValue = when (dismissState.targetValue) {
                            DismissValue.Default -> Color.Transparent
                            DismissValue.DismissedToEnd -> 蓝绿
                            DismissValue.DismissedToStart -> 山茶红
                        }
                    )
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.Default.Done
                        DismissDirection.EndToStart -> Icons.Default.Delete
                    }

                    val scale by animateFloatAsState(targetValue = if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f)

                    val alignment = when (direction) {
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 12.dp),
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            modifier = Modifier
                                .scale(scale)
                                .align(alignment)
                        )
                    }
                },
                dismissContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                shape = when (dismissState.targetValue) {
                                    DismissValue.DismissedToEnd -> RoundedCornerShape(
                                        topStart = 8.dp,
                                        bottomStart = 8.dp
                                    )
                                    DismissValue.DismissedToStart -> RoundedCornerShape(
                                        topEnd = 8.dp,
                                        bottomEnd = 8.dp
                                    )
                                    DismissValue.Default -> RoundedCornerShape(0f)
                                }
                            )
                            .background(MaterialTheme.colors.surface)
                            .clickable(onClick = { onClick(bot) })
                            .padding(16.dp)
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
                                modifier = Modifier
                                    .size(52.dp)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                            )
                            ChatLabel(
                                bot = bot,
                                modifier = Modifier
                                    .size(18.dp)
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
                }
            )
//            Divider()
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
        Platform.Lark -> {
            Icon(
                painter = painterResource(id = R.drawable.ic_lark),
                contentDescription = "lark bot",
                modifier = modifier,
                tint = Color.Unspecified
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ApplicationTheme {
        Chats(
            listOf(
                BotBean("111", Platform.WeCom)
            ), onClick = {},
            onDelete = {
            })
    }
}