package com.wecom.botfather.mock

import com.wecom.T
import com.wecom.botfather.sdk.Response
import com.wecom.botfather.sdk.service.DingTalkMsg
import com.wecom.botfather.sdk.service.DingTalkService
import com.wecom.botfather.sdk.service.WeComMsg
import com.wecom.botfather.sdk.service.WeComService
import kotlinx.coroutines.delay

class MockService : WeComService, DingTalkService {

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
        return Response.Fail(123, "???")
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
        return Response.Fail(123, "???")
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
        return Response.Fail(123, "???")
    }
}