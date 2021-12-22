package com.wecom

import android.app.Application
import com.wecom.botfather.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

typealias T = Timber

class BotFatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        T.plant(Timber.DebugTree())
        T.tag(this::class.java.simpleName)

        startKoin {
            androidContext(this@BotFatherApp)
            androidLogger()
            modules(appModules)
        }
    }
}