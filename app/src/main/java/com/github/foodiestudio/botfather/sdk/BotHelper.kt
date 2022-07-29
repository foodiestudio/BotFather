package com.github.foodiestudio.botfather.sdk

import com.github.foodiestudio.botfather.data.BotQueries

/**
 * 企业机器人
 */
abstract class BotHelper(private val botQueries: BotQueries) : OutGoingBot {

    /**
     * 所有机器人列表
     */
    val bots: Set<BotBean>
        get() = botQueries.selectAll().executeAsList().map { BotBean(it) }.toSet()

    fun addNewBot(bot: BotBean) {
        botQueries.addNewOne(
            id = bot.id,
            avatar = bot.avatar,
            name = bot.name,
            platform = bot.platform.type
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
}

interface OutGoingBot {
    suspend fun sendMsgToAll(msg: TextMessage)

    suspend fun sendMsg(botId: String, msg: TextMessage): Response
}