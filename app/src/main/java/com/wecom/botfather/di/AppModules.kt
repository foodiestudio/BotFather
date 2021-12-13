package com.wecom.botfather.di

import com.wecom.botfather.mock.MockService
import com.wecom.botfather.sdk.service.WeComService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val debug = true

val appModules = module {
    if (debug) {
        // 使用日志打印代替请求
        single<WeComService> { MockService() }
    } else {
        single<WeComService> {
            Retrofit.Builder()
                .baseUrl("https://qyapi.weixin.qq.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeComService::class.java)
        }
    }
}