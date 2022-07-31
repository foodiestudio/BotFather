package com.github.foodiestudio.botfather.sdk.helper

import android.util.Base64
import com.github.foodiestudio.botfather.data.BotQueries
import com.github.foodiestudio.botfather.sdk.BotHelper
import com.github.foodiestudio.botfather.sdk.Response
import com.github.foodiestudio.botfather.sdk.TextMessage
import com.github.foodiestudio.botfather.sdk.service.*
import org.koin.java.KoinJavaComponent
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class LarkBotHelper(botQueries: BotQueries) : BotHelper(botQueries) {

    private val service: LarkService by KoinJavaComponent.inject(LarkService::class.java)

    private val secret = "EQIQnGTWcud7AwcH9WlAAd"

    override suspend fun sendMsgToAll(msg: TextMessage) {
        bots.forEach {
            sendMsg(it.id, msg)
        }
    }

    override suspend fun sendMsg(botId: String, msg: TextMessage): Response {
        val epochTime = System.currentTimeMillis() / 1000

        if (msg is TextMessage.Markdown) {
            return FailureResponse("Lark don't support markdown.")
        }

        return service.sendMsg(
            botId,
            LarkTextMsg(
                LarkContent(msg.content),
                timestamp = epochTime.toString(),
                sign = signature(secret, epochTime),
            )
        )
    }

    /**
     * 把 timestamp + "\n" + 密钥 当做签名字符串，使用 HmacSHA256 算法计算签名，再进行 Base64 编码。
     * https://open.feishu.cn/document/ukTMukTMukTM/ucTM5YjL3ETO24yNxkjN#348211be
     */
    private fun signature(secret: String, timestamp: Long): String {
        val stringToSign = """
            $timestamp
            $secret
            """.trimIndent()
        val mac: Mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(stringToSign.toByteArray(charset("UTF-8")), "HmacSHA256"))
        val signData = mac.doFinal(byteArrayOf())
        return Base64.encodeToString(signData, Base64.NO_WRAP)
    }
}

private fun FailureResponse(errorMsg: String, errorCode: Int = -1): Response {
    return Response(errorCode, errorMsg)
}