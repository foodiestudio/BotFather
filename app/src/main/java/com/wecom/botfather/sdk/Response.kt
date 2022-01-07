package com.wecom.botfather.sdk

data class Response(
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
}