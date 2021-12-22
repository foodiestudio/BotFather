package com.wecom.botfather.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wecom.botfather.sdk.BotBean
import com.wecom.botfather.sdk.TextMessage
import com.wecom.botfather.sdk.WeComBotHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class ChatViewModel(private val sdk: WeComBotHelper) : ViewModel() {
    suspend fun queryBotById(botId: String): BotBean? {
        val result: BotBean?
        withContext(Dispatchers.IO) {
            result = sdk.queryBot(botId)
        }
        return result
    }

    fun sendMsg(id: String, markdown: TextMessage.Markdown) {
        viewModelScope.launch {
            sdk.sendMsg(id, markdown)
        }
    }
}