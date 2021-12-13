package com.wecom

import android.app.Application
import com.wecom.botfather.di.appModules
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import timber.log.Timber

typealias T = Timber

class BotFatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        T.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            modules(appModules)
        }
    }
}