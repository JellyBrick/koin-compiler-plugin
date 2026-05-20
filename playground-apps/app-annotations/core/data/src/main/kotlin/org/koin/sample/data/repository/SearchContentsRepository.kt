package org.koin.sample.data.repository

import kotlinx.coroutines.flow.Flow
import org.koin.sample.model.SearchResult

interface SearchContentsRepository {
    fun searchContents(query: String): Flow<SearchResult>
}
