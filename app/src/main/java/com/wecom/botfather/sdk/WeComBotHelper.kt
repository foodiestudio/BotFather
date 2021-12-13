package com.wecom.botfather.sdk

import com.wecom.botfather.sdk.service.Msg
import com.wecom.botfather.sdk.service.Text
import com.wecom.botfather.sdk.service.WeComService
import org.koin.java.KoinJavaComponent.inject

/**
 * 企业微信机器人
 */
object WeComBotHelper {

    private val service: WeComService by inject(WeComService::class.java)

    /**
     * 所有机器人列表
     */
    val bots: MutableSet<Bot> = mutableSetOf()

    /**
     * 根据 id 查询
     */
    fun queryBot(botId: String): Bot? = bots.firstOrNull { it.id == botId }

    /**
     * 发送给所有
     */
    suspend fun sendMsgToAll(msg: TextMessage) {
        bots.forEach {
            sendMsg(it.id, msg)
        }
    }

    /**
     * 发送文本消息
     * 注意，目前 Markdown 所能支持的格式很有限
     */
    suspend fun sendMsg(botId: String, msg: TextMessage) {
        when (msg) {
            is TextMessage.Markdown -> {
                service.sendMsg(botId, Msg("markdown", null, Text(msg.content)))
            }
            is TextMessage.PlainText -> {
                service.sendMsg(botId, Msg("text", Text(msg.content), null))
            }
        }
    }
}