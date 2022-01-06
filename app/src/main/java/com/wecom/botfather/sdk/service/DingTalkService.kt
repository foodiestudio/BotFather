package com.wecom.botfather.sdk.service

import com.wecom.botfather.sdk.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface DingTalkService {

    @Headers("Content-Type: application/json")
    @POST("robot/send")
    suspend fun sendMsgWithSign(
        @Query("access_token") token: String, @Query("timestamp") current: Long,
        @Query("sign") sign: String, @Body msg: DingTalkMsg
    ): Response

    @Headers("Content-Type: application/json")
    @POST("robot/send")
    suspend fun sendMsgWithoutSign(
        @Query("access_token") token: String,
        @Body msg: DingTalkMsg
    ): Response
}

data class DingTalkMsg(
    val msgtype: String,
    val text: DingTalkContent?,
    val markdown: DingTalkText?
)

data class DingTalkContent(
    val content: String
)

data class DingTalkText(
    val text: String,
    val title: String
) {
    init {
        require(title.isNotEmpty())
    }
}