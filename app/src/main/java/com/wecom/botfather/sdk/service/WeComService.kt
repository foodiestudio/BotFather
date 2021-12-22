package com.wecom.botfather.sdk.service

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

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

open class Response(
    val errcode: Int,
    val errmsg: String
) {

    fun doOnSuccess(action: () -> Unit): Response {
        if (errcode == 0) {
            action()
        }
        return this
    }

    fun doOnFail(action: (String) -> Unit): Response {
        if (errcode != 0) {
            action(errmsg)
        }
        return this
    }

    class Success : Response(0, "")

    class Fail(
        errcode: Int,
        errmsg: String
    ) : Response(errcode, errmsg)
}
