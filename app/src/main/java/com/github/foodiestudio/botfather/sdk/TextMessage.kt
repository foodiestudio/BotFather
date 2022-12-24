package com.github.foodiestudio.botfather.sdk

sealed class TextMessage(
    val content: String
) {

    class PlainText(content: String) : TextMessage(content)

    class Markdown(content: String) : TextMessage(content)
}
