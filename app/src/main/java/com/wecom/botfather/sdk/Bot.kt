package com.wecom.botfather.sdk

data class Bot(
    val id: String,
    val name: String = "Bot_$id"
)