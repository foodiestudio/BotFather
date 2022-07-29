package com.github.foodiestudio.botfather.sdk.helper

import android.util.Base64
import com.github.foodiestudio.botfather.data.BotQueries
import com.github.foodiestudio.botfather.sdk.BotHelper
import com.github.foodiestudio.botfather.sdk.Response
import com.github.foodiestudio.botfather.sdk.TextMessage
import com.github.foodiestudio.botfather.sdk.service.DingTalkContent
import com.github.foodiestudio.botfather.sdk.service.DingTalkMsg
import com.github.foodiestudio.botfather.sdk.service.DingTalkService
import com.github.foodiestudio.botfather.sdk.service.DingTalkText
import org.koin.java.KoinJavaComponent
import java.net.URLEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * 钉钉机器人
 * 有安全设置，
 */
class DingTalkBotHelper(botQueries: BotQueries) : BotHelper(botQueries) {

    private val service: DingTalkService by KoinJavaComponent.inject(DingTalkService::class.java)

    /**
     * FIXME 临时测试用
     */
    private val secret = "SECfc47c4441a3b48e6388a652088ee8a5ab3274f5d1ddc19e60c8a1c0093e97c72"

    override suspend fun sendMsg(botId: String, msg: TextMessage): Response {
        val timestamp = System.currentTimeMillis()

        return when (msg) {
            is TextMessage.Markdown -> {
                service.sendMsgWithSign(
                    botId,
                    timestamp,
                    sign(timestamp),
                    DingTalkMsg(
                        "markdown",
                        null,
                        DingTalkText(title = msg.content.take(10), text = msg.content)
                    )
                )
            }
            is TextMessage.PlainText -> {
                service.sendMsgWithSign(
                    botId,
                    timestamp,
                    sign(timestamp),
                    DingTalkMsg("text", DingTalkContent(msg.content), null)
                )
            }
        }
    }

    private fun sign(timestamp: Long): String {
        val stringToSign = """
            $timestamp
            $secret
            """.trimIndent()
        val mac: Mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secret.toByteArray(charset("UTF-8")), "HmacSHA256"))
        val signData: ByteArray = mac.doFinal(stringToSign.toByteArray(charset("UTF-8")))
        return URLEncoder.encode(Base64.encodeToString(signData, Base64.NO_WRAP), "UTF-8")
    }

    override suspend fun sendMsgToAll(msg: TextMessage) {
        bots.forEach {
            sendMsg(it.id, msg)
        }
    }
}