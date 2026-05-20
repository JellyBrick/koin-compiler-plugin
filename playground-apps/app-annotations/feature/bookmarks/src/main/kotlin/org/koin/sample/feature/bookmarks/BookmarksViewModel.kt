package org.koin.sample.feature.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import org.koin.sample.data.repository.UserDataRepository
import org.koin.sample.data.repository.UserNewsResourceRepository
import org.koin.sample.model.UserNewsResource

@KoinViewModel
class BookmarksViewModel(
    private val userNewsResourceRepository: UserNewsResourceRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val bookmarkedNews: StateFlow<List<UserNewsResource>> =
        userNewsResourceRepository.observeAll()
            .map { resources -> resources.filter { it.newsResource.isBookmarked } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun removeBookmark(newsId: String) {
        viewModelScope.launch {
            userDataRepository.toggleBookmark(newsId, false)
        }
    }
}
