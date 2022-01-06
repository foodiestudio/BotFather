package com.wecom.botfather.sdk

enum class Platform(val type: Long) {
    WeCom(1), DingTalk(2);

    companion object {
        operator fun invoke(rawValue: Long) = Platform.values().find { it.type == rawValue }
    }
}