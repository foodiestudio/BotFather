package com.github.foodiestudio.botfather.sdk.service

import com.github.foodiestudio.botfather.sdk.Response
import retrofit2.http.*

interface LarkService {

    @Headers("Content-Type: application/json")
    @POST("open-apis/bot/v2/hook/{id}")
    suspend fun sendMsg(
        @Path("id") id: String,
        @Body msg: LarkTextMsg
    ): Response
}

data class LarkTextMsg(
    val content: LarkContent,
    val sign: String? = null,
    val timestamp: String? = null
) {
    val msg_type: String = "text"
}

data class LarkContent(
    val text: String
)