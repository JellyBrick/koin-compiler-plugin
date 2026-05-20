package org.koin.sample.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.sample.model.UserNewsResource

class CompositeUserNewsResourceRepository(
    private val newsRepository: NewsRepository,
    private val userDataRepository: UserDataRepository,
) : UserNewsResourceRepository {

    override fun observeAll(): Flow<List<UserNewsResource>> =
        combine(newsRepository.getNewsResources(), userDataRepository.userData) { news, userData ->
            news.map { resource ->
                UserNewsResource(
                    newsResource = resource.copy(isBookmarked = resource.id in userData.bookmarkedNewsIds),
                    userData = userData,
                )
            }
        }
}
