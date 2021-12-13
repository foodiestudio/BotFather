package com.wecom.botfather.mock

import com.wecom.T
import com.wecom.botfather.sdk.service.Msg
import com.wecom.botfather.sdk.service.Response
import com.wecom.botfather.sdk.service.WeComService
import kotlinx.coroutines.delay

class MockService : WeComService {

    override suspend fun sendMsg(key: String, msg: Msg): Response {
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
        return Response.Fail(0, "???")
    }
}