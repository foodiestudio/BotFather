package com.github.foodiestudio.botfather.mock

import com.github.foodiestudio.botfather.T
import com.github.foodiestudio.botfather.sdk.Response
import com.github.foodiestudio.botfather.sdk.service.*
import com.github.foodiestudio.botfather.sdk.service.WeComService
import kotlinx.coroutines.delay

class MockService : WeComService, DingTalkService, LarkService {

    override suspend fun sendMsg(key: String, msg: WeComMsg): Response {
        delay(1000)
        T.i(
            """
            ============================
            to $key:
            ----------------------------
            ${msg.markdown?.content ?: msg.text?.content}
            ============================
        """.trimIndent()
        )
        return failResp(123, "???")
    }

    override suspend fun sendMsgWithSign(
        token: String,
        current: Long,
        sign: String,
        msg: DingTalkMsg
    ): Response {
        delay(1000)
        T.i(
            """
            ============================
            to $token:
            ----------------------------
            ${msg.markdown?.text ?: msg.text?.content}
            ============================
        """.trimIndent()
        )
        return failResp(123, "???")
    }

    override suspend fun sendMsgWithoutSign(token: String, msg: DingTalkMsg): Response {
        delay(1000)
        T.i(
            """
            ============================
            to $token:
            ----------------------------
            ${msg.markdown?.text ?: msg.text?.content}
            ============================
        """.trimIndent()
        )
        return failResp(123, "???")
    }

    override suspend fun sendMsg(id: String, msg: LarkTextMsg): Response {
        delay(1000)
        T.i(
            """
            ============================
            to $id:
            ----------------------------
            ${msg.content.text}
            ============================
        """.trimIndent()
        )
        return failResp(123, "???")
    }
}

private fun successResp() = Response(0, "")

private fun failResp(
    errcode: Int,
    errmsg: String
) = Response(errcode, errmsg)