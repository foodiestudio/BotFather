package com.wecom.botfather.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.wecom.botfather.Database
import com.wecom.botfather.sdk.WeComBotHelper
import com.wecom.botfather.sdk.service.WeComService
import com.wecom.botfather.ui.chat.ChatViewModel
import com.wecom.botfather.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val uiModules = module {
    viewModel { HomeViewModel(get()) }
    viewModel { ChatViewModel(get()) }
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

}

val serviceModule = module {
    single<WeComService> {
        Retrofit.Builder()
            .baseUrl("https://qyapi.weixin.qq.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeComService::class.java)
    }
}

val appModule = uiModules + sdkModules + serviceModule

