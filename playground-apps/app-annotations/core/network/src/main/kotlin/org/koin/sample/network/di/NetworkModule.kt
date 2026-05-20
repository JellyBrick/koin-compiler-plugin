package org.koin.sample.network.di

import android.content.Context
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.sample.network.AppHttpClient
import org.koin.sample.network.AppImageLoader

@Module
@Configuration
@ComponentScan("org.koin.sample.network.demo")
class NetworkModule {

    @Singleton
    fun providesJson(): Json = Json { ignoreUnknownKeys = true }

    @Singleton
    fun providesHttpClient(): AppHttpClient = AppHttpClient()

    @Singleton
    fun providesImageLoader(
        httpClient: Lazy<AppHttpClient>,
        context: Context,
    ): AppImageLoader = AppImageLoader(context, httpClient)
}
