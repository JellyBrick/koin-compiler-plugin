package org.koin.sample.app

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.annotation.KoinApplication
import org.koin.core.logger.Level
import org.koin.plugin.module.dsl.startKoin
import org.koin.sample.network.AppImageLoader

@KoinApplication
class StressTestApplication : Application() {

    private val imageLoader: AppImageLoader by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin<StressTestApplication> {
            androidLogger(Level.DEBUG)
            androidContext(this@StressTestApplication)
            workManagerFactory()
        }
    }
}
