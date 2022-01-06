package com.wecom.botfather.sdk

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