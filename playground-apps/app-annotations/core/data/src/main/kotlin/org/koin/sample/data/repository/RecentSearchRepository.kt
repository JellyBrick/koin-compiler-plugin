package org.koin.sample.data.repository

import kotlinx.coroutines.flow.Flow
import org.koin.sample.model.RecentSearch

interface RecentSearchRepository {
    fun getRecentSearches(): Flow<List<RecentSearch>>
    suspend fun insertRecentSearch(query: String)
    suspend fun clearRecentSearches()
}
