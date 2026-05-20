package org.koin.sample.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.sample.data.repository.SearchContentsRepository
import org.koin.sample.data.repository.UserDataRepository
import org.koin.sample.model.SearchResult

class GetSearchContentsUseCase(
    private val searchContentsRepository: SearchContentsRepository,
    private val userDataRepository: UserDataRepository,
) {
    operator fun invoke(query: String): Flow<SearchResult> =
        combine(searchContentsRepository.searchContents(query), userDataRepository.userData) { result, userData ->
            result.copy(
                newsResources = result.newsResources.map { it.copy(isBookmarked = it.id in userData.bookmarkedNewsIds) },
            )
        }
}
