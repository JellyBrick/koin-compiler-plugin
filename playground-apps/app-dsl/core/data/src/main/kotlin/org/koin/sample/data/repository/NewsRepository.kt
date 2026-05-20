package org.koin.sample.data.repository

import kotlinx.coroutines.flow.Flow
import org.koin.sample.model.NewsResource

interface NewsRepository {
    fun getNewsResources(): Flow<List<NewsResource>>
    fun getNewsResource(id: String): Flow<NewsResource>
    suspend fun syncWith(): Boolean
}
