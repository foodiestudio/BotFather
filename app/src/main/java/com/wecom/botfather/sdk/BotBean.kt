package com.wecom.botfather.sdk

import com.wecom.botfather.data.Bot

data class BotBean(
    val id: String,
    var platform: Platform,
    val name: String = "Bot_$id",
    var avatar: String? = null
) {
    constructor(bot: Bot) : this(bot.id, Platform(bot.platform)!!, bot.name, bot.avatar)
}