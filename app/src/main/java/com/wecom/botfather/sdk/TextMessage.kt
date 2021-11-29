package com.wecom.botfather.sdk

sealed class TextMessage(
    val content: String
) {

    class PlainText(content: String) : TextMessage(content)

    class Markdown(content: String) : TextMessage(content)
}