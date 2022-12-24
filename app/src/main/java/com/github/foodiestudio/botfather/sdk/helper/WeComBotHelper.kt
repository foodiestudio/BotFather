package com.github.foodiestudio.botfather.sdk.helper

import com.github.foodiestudio.botfather.data.BotQueries
import com.github.foodiestudio.botfather.sdk.BotHelper
import com.github.foodiestudio.botfather.sdk.Response
import com.github.foodiestudio.botfather.sdk.TextMessage
import com.github.foodiestudio.botfather.sdk.service.*
import com.github.foodiestudio.botfather.sdk.service.WeComService
import org.koin.java.KoinJavaComponent.inject

/**
 * 企业微信机器人
 */
class WeComBotHelper(botQueries: BotQueries) : BotHelper(botQueries) {

    private val service: WeComService by inject(WeComService::class.java)

    /**
     * 发送给所有
     */
    override suspend fun sendMsgToAll(msg: TextMessage) {
    }

    /**
     * 发送文本消息
     * 注意，目前 Markdown 所能支持的格式很有限
     */
    override suspend fun sendMsg(botId: String, msg: TextMessage): Response {
        return when (msg) {
            is TextMessage.Markdown -> {
                service.sendMsg(botId, WeComMsg("markdown", null, WecomContent(msg.content)))
            }
            is TextMessage.PlainText -> {
                service.sendMsg(botId, WeComMsg("text", WecomContent(msg.content), null))
            }
        }
    }
}
