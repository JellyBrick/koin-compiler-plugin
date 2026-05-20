package org.koin.sample.app

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.sample.app.di.appModule
import org.koin.sample.network.AppImageLoader

class StressTestApplication : Application() {

    private val imageLoader: AppImageLoader by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@StressTestApplication)
            workManagerFactory()

            modules(appModule)
        }
    }
}
