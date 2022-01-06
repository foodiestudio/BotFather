package com.wecom.botfather.sdk.service

import com.wecom.botfather.sdk.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

internal interface WeComService {

    @Headers("Content-Type: application/json")
    @POST("cgi-bin/webhook/send")
    suspend fun sendMsg(@Query("key") key: String, @Body msg: WeComMsg): Response
}

data class WeComMsg(
    val msgtype: String,
    val text: WecomContent?,
    val markdown: WecomContent?
)

data class WecomContent(
    val content: String
)
