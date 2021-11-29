package com.wecom.botfather.sdk.service

import retrofit2.http.*

internal interface WeComService {

    @Headers("Content-Type: application/json")
    @POST("cgi-bin/webhook/send")
    suspend fun sendMsg(@Query("key") key: String, @Body msg: Msg): Response
}

data class Msg(
    val msgtype: String,
    val text: Text?,
    val markdown: Text?
)

data class Text(
    val content: String
)

sealed class Response(
    val errcode: Int,
    val errmsg: String
) {
    class Success : Response(0, "")

    class Fail(
        errcode: Int,
        errmsg: String
    ) : Response(errcode, errmsg)
}
