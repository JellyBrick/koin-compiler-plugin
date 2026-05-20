package org.koin.sample.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory
import org.koin.sample.common.Dispatcher
import org.koin.sample.common.NiaDispatchers
import org.koin.sample.model.SearchResult

@Factory
class DefaultSearchContentsRepository(
    private val topicsRepository: TopicsRepository,
    private val newsRepository: NewsRepository,
    @Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : SearchContentsRepository {

    override fun searchContents(query: String): Flow<SearchResult> =
        combine(topicsRepository.getTopics(), newsRepository.getNewsResources()) { topics, news ->
            SearchResult(
                topics = topics.filter { it.name.contains(query, ignoreCase = true) },
                newsResources = news.filter { it.title.contains(query, ignoreCase = true) },
            )
        }.flowOn(ioDispatcher)
}
