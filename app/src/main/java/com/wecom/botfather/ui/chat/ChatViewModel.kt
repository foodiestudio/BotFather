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

class ChatViewModel(platform: Platform) : ViewModel() {

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

    fun sendMsg(id: String, markdown: TextMessage.Markdown) {
        viewModelScope.launch {
            _sendResult.value = sdk.sendMsg(id, markdown).doOnFail {
                T.w(it)
            }.doOnSuccess {
                T.i("send success!")
            }
        }
    }
}