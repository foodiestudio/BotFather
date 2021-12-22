package com.wecom.botfather.sdk

import com.wecom.botfather.data.Bot

data class BotBean(
    val id: String,
    val name: String = "Bot_$id",
    var avatar: String? = null
) {
    constructor(bot: Bot): this(bot.id, bot.name, bot.avatar)
}