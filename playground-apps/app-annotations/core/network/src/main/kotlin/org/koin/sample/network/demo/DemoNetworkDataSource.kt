package org.koin.sample.network.demo

import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton
import org.koin.sample.common.Dispatcher
import org.koin.sample.common.NiaDispatchers
import org.koin.sample.model.NewsResource
import org.koin.sample.model.Topic
import org.koin.sample.network.NetworkDataSource

@Singleton
class DemoNetworkDataSource @Inject constructor(
    @Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : NetworkDataSource {

    override suspend fun getTopics(): List<Topic> = withContext(ioDispatcher) {
        listOf(
            Topic("1", "Kotlin", "Kotlin programming language"),
            Topic("2", "Android", "Android development"),
            Topic("3", "Compose", "Jetpack Compose UI toolkit"),
        )
    }

    override suspend fun getNewsResources(): List<NewsResource> = withContext(ioDispatcher) {
        listOf(
            NewsResource("1", "Kotlin 2.0 Released", "Kotlin 2.0 is here with K2 compiler.", listOf("1")),
            NewsResource("2", "Compose Multiplatform", "Compose goes multiplatform.", listOf("2", "3")),
            NewsResource("3", "Android 15 Features", "New features in Android 15.", listOf("2")),
        )
    }
}
