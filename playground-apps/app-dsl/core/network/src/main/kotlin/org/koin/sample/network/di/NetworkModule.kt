package org.koin.sample.network.di

import kotlinx.serialization.json.Json
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single
import org.koin.sample.common.di.dispatchersModule
import org.koin.sample.network.AppHttpClient
import org.koin.sample.network.AppImageLoader
import org.koin.sample.network.NetworkDataSource
import org.koin.sample.network.NetworkMonitor
import org.koin.sample.network.TimeZoneMonitor
import org.koin.sample.network.demo.DemoNetworkDataSource
import org.koin.sample.network.demo.DemoNetworkMonitor
import org.koin.sample.network.demo.DemoTimeZoneMonitor

val networkModule = module {
    includes(dispatchersModule)

    single { create(::json) }
    single<AppHttpClient>()
    single<AppImageLoader>()
    single<DemoNetworkDataSource>() bind NetworkDataSource::class
    single<DemoNetworkMonitor>() bind NetworkMonitor::class
    single<DemoTimeZoneMonitor>() bind TimeZoneMonitor::class
}

private fun json(): Json = Json { ignoreUnknownKeys = true }
