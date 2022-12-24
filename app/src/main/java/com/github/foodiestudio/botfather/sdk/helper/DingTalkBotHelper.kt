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

        val response = when (msg) {
            is TextMessage.Markdown -> {
                service.sendMsgWithSign(
                    botId,
                    timestamp,
                    sign(secret, timestamp),
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
                    sign(secret, timestamp),
                    DingTalkMsg("text", DingTalkContent(msg.content), null)
                )
            }
        }
        return convertErrorMsg(response)
    }

    private val dingTalkErrMsgs = mapOf(
        "keywords not in content" to "当前消息并不包含任何关键词，请检查安全设置",
        "invalid timestamp" to "时间戳检验失败",
        "sign not match" to "签名校验不通过，请检查安全设置",
        "not in whitelist" to "当前网络环境不在白名单里，请检查安全设置"
    )

    private fun convertErrorMsg(origin: Response): Response {
        var result = origin
        if (origin.errcode == 310000 /*消息校验没通过*/) {
            dingTalkErrMsgs.forEach { (key, value) ->
                if (origin.errmsg.contains(key)) {
                    result = origin.copy(errmsg = value)
                    return@forEach
                }
            }
            origin.errmsg.split(";").firstOrNull {
                it.contains("description:")
            }?.removePrefix("description:")?.let {
                return origin.copy(errmsg = it)
            }
        }
        return result
    }

    /**
     * 以密钥为key，把 timestamp + "\n" + 密钥 当做待签名字符串，使用 HmacSHA256 算法计算签名，再进行 Base64 编码。
     * https://open.dingtalk.com/document/group/customize-robot-security-settings
     */
    private fun sign(secret: String, timestamp: Long): String {
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
