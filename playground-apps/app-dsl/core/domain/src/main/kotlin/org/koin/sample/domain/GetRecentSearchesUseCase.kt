package org.koin.sample.domain

import kotlinx.coroutines.flow.Flow
import org.koin.sample.data.repository.RecentSearchRepository
import org.koin.sample.model.RecentSearch

class GetRecentSearchesUseCase(
    private val recentSearchRepository: RecentSearchRepository,
) {
    operator fun invoke(): Flow<List<RecentSearch>> =
        recentSearchRepository.getRecentSearches()
}
