package com.github.foodiestudio.botfather.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.github.foodiestudio.botfather.Database
import com.github.foodiestudio.botfather.sdk.helper.DingTalkBotHelper
import com.github.foodiestudio.botfather.sdk.helper.WeComBotHelper
import com.github.foodiestudio.botfather.sdk.service.DingTalkService
import com.github.foodiestudio.botfather.sdk.service.WeComService
import com.github.foodiestudio.botfather.ui.chat.ChatViewModel
import com.github.foodiestudio.botfather.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val uiModules = module {
    viewModel { HomeViewModel(get()) }
    viewModel { params -> ChatViewModel(params.get()) }
}

val sdkModules = module {

    single<SqlDriver> {
        AndroidSqliteDriver(
            Database.Schema,
            androidApplication(),
            "App.db"
        )
    }

    single {
        Database(get())
    }

    factory { WeComBotHelper(get<Database>().botQueries) }

    factory { DingTalkBotHelper(get<Database>().botQueries) }
}

val serviceModule = module {
    single<WeComService> {
        Retrofit.Builder()
            .baseUrl("https://qyapi.weixin.qq.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeComService::class.java)
    }

    single<DingTalkService> {
        Retrofit.Builder()
            .baseUrl("https://oapi.dingtalk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DingTalkService::class.java)
    }
}

val appModule = uiModules + sdkModules + serviceModule

