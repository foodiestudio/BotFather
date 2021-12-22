package com.wecom.botfather.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.wecom.botfather.Database
import com.wecom.botfather.mock.MockService
import com.wecom.botfather.sdk.WeComBotHelper
import com.wecom.botfather.sdk.service.WeComService
import com.wecom.botfather.ui.chat.ChatViewModel
import com.wecom.botfather.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val debug = true


val uiModules = module {
    viewModel { HomeViewModel(get()) }
    viewModel { ChatViewModel(get()) }
}

val sdkModules = module {
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

    single { WeComBotHelper(get<Database>().botQueries) }

}

val appModules = sdkModules + uiModules
