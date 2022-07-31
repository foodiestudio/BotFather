package com.github.foodiestudio.botfather.di

import com.github.foodiestudio.botfather.Database
import com.github.foodiestudio.botfather.sdk.helper.DingTalkBotHelper
import com.github.foodiestudio.botfather.sdk.helper.LarkBotHelper
import com.github.foodiestudio.botfather.sdk.helper.WeComBotHelper
import com.github.foodiestudio.botfather.sdk.service.DingTalkService
import com.github.foodiestudio.botfather.sdk.service.LarkService
import com.github.foodiestudio.botfather.sdk.service.WeComService
import com.github.foodiestudio.botfather.ui.chat.ChatViewModel
import com.github.foodiestudio.botfather.ui.home.HomeViewModel
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

    factory { LarkBotHelper(get<Database>().botQueries) }
}

val serviceModule = module {
    factory {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single<WeComService> {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://qyapi.weixin.qq.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeComService::class.java)
    }

    single<DingTalkService> {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://oapi.dingtalk.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DingTalkService::class.java)
    }

    single<LarkService> {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://open.feishu.cn/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LarkService::class.java)
    }
}

val appModule = uiModules + sdkModules + serviceModule

