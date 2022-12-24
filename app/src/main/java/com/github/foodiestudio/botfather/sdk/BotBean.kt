package com.github.foodiestudio.botfather.sdk

import com.github.foodiestudio.botfather.data.Bot

data class BotBean(
    val id: String,
    var platform: Platform,
    val name: String = "Bot_$id",
    var avatar: String? = null
) {
    constructor(bot: Bot) : this(bot.id, Platform(bot.platform)!!, bot.name, bot.avatar)
}
