package org.koin.sample.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Singleton
import org.koin.sample.database.dao.RecentSearchDao
import org.koin.sample.database.entity.RecentSearchEntity
import org.koin.sample.model.RecentSearch

@Singleton
class DefaultRecentSearchRepository(
    private val recentSearchDao: RecentSearchDao,
) : RecentSearchRepository {

    override fun getRecentSearches(): Flow<List<RecentSearch>> =
        recentSearchDao.getAll().map { entities ->
            entities.map { RecentSearch(it.query, it.timestamp) }
        }

    override suspend fun insertRecentSearch(query: String) {
        recentSearchDao.insert(RecentSearchEntity(query, System.currentTimeMillis()))
    }

    override suspend fun clearRecentSearches() {
        recentSearchDao.deleteAll()
    }
}
