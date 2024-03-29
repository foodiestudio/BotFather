package com.github.foodiestudio.botfather.sdk

enum class Platform(val type: Long) {
    WeCom(1), DingTalk(2), Lark(3);

    companion object {
        operator fun invoke(rawValue: Long) = values().find { it.type == rawValue }
    }
}
