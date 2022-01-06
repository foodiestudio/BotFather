package com.wecom

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.wecom.botfather.BuildConfig
import com.wecom.botfather.di.appModule
import com.wecom.botfather.di.serviceModule
import com.wecom.botfather.mock.MockService
import com.wecom.botfather.sdk.service.DingTalkService
import com.wecom.botfather.sdk.service.WeComService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import timber.log.Timber

typealias T = Timber

class BotFatherApp : Application(), CoroutineScope by MainScope() {

    override fun onCreate() {
        super.onCreate()
        T.plant(Timber.DebugTree())
        T.tag(this::class.java.simpleName)

        startKoin {
            allowOverride(true)
            androidContext(this@BotFatherApp)
            androidLogger()
            modules(appModule)
        }

        launch {
            // 监听变更
            dataStore.data.collectLatest {
                loadServiceModule(it[ABORT_REQUEST_KEY] ?: BuildConfig.DEBUG)
            }
        }
    }

    companion object {
        private val debugModule = module {
            single<WeComService> { MockService() }
            single<DingTalkService> { MockService() }
        }

        /**
         * Koin 允许加载同类的 module，覆盖之前的
         */
        fun loadServiceModule(debug: Boolean) {
            if (debug) {
                loadKoinModules(debugModule)
            } else {
                unloadKoinModules(debugModule)
                loadKoinModules(serviceModule)
            }
        }
    }
}

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// abort api request
val ABORT_REQUEST_KEY = booleanPreferencesKey("abort_request")
