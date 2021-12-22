package com.wecom.botfather.sdk

import com.wecom.botfather.data.BotQueries
import com.wecom.botfather.sdk.service.Msg
import com.wecom.botfather.sdk.service.Text
import com.wecom.botfather.sdk.service.WeComService
import org.koin.java.KoinJavaComponent.inject

/**
 * 企业微信机器人
 */
class WeComBotHelper(private val botQueries: BotQueries) {

    private val service: WeComService by inject(WeComService::class.java)

    /**
     * 所有机器人列表
     */
    val bots: Set<BotBean>
        get() = botQueries.selectAll().executeAsList().map { BotBean(it) }.toSet()

    fun addNewBot(bot: BotBean) {
        botQueries.addNewOne(
            id = bot.id,
            avatar = bot.avatar,
            name = bot.name
        )
    }

    fun updateBotName(id: String, newName: String) {
        botQueries.updateName(id = id, name = newName)
    }

    fun updateBotAvatar(id: String, newAvatar: String) {
        botQueries.updateAvatar(id = id, avatar = newAvatar)
    }

    /**
     * 根据 id 查询
     */
    fun queryBot(botId: String): BotBean? =
        botQueries.queryById(botId).executeAsOneOrNull()?.let { BotBean(it) }

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