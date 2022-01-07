package com.wecom.botfather.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wecom.T
import com.wecom.botfather.sdk.*
import com.wecom.botfather.sdk.helper.DingTalkBotHelper
import com.wecom.botfather.sdk.helper.WeComBotHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class ChatViewModel(private val platform: Platform) : ViewModel() {

    private val _sendResult: MutableLiveData<Response> = MutableLiveData()
    val sendResult: LiveData<Response>
        get() = _sendResult

    private val sdk: BotHelper by when (platform) {
        Platform.WeCom -> {
            inject<WeComBotHelper>(WeComBotHelper::class.java)
        }
        Platform.DingTalk -> {
            inject<DingTalkBotHelper>(DingTalkBotHelper::class.java)
        }
    }

    suspend fun queryBotById(botId: String): BotBean? {
        val result: BotBean?
        withContext(Dispatchers.IO) {
            result = sdk.queryBot(botId)
        }
        return result
    }

    val dingTalkErrMsgs = mapOf(
        "keywords not in content" to "当前消息并不包含任何关键词，请检查安全设置",
        "invalid timestamp" to "时间戳检验失败",
        "sign not match" to "签名校验不通过，请检查安全设置",
        "not in whitelist" to "当前网络环境不在白名单里，请检查安全设置"
    )

    fun sendMsg(id: String, markdown: TextMessage.Markdown) {
        viewModelScope.launch {
            val origin = sdk.sendMsg(id, markdown).doOnFail {
                T.w(it)
            }.doOnSuccess {
                T.i("send success!")
            }
            _sendResult.value = when {
                platform == Platform.DingTalk && origin.errcode == 310000 /*消息校验没通过*/ -> {
                    var result = origin
                    dingTalkErrMsgs.forEach { (key, value) ->
                        if (origin.errmsg.contains(key)) {
                            result = origin.copy(errmsg = value)
                            return@forEach
                        }
                    }
                    result
                }
                else -> origin
            }
        }
    }
}