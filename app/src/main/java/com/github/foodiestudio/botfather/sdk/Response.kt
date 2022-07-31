package com.github.foodiestudio.botfather.sdk

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName( value = "errcode",alternate = arrayOf("code"))
    val errcode: Int,
    @SerializedName( value = "errmsg",alternate = arrayOf("msg"))
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