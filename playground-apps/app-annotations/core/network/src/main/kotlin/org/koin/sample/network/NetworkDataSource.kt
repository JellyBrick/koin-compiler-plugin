package org.koin.sample.network

import org.koin.sample.model.NewsResource
import org.koin.sample.model.Topic

interface NetworkDataSource {
    suspend fun getTopics(): List<Topic>
    suspend fun getNewsResources(): List<NewsResource>
}
