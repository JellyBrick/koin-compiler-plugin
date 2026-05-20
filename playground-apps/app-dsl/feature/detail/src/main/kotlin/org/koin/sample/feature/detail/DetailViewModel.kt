package org.koin.sample.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.sample.data.repository.NewsRepository
import org.koin.sample.data.repository.UserDataRepository
import org.koin.sample.data.repository.UserNewsResourceRepository
import org.koin.sample.model.NewsResource

class DetailViewModel(
    private val newsRepository: NewsRepository,
    private val userDataRepository: UserDataRepository,
    private val userNewsResourceRepository: UserNewsResourceRepository,
    @InjectedParam val newsId: String = "",
) : ViewModel() {

    val newsResource: StateFlow<NewsResource?> =
        newsRepository.getNewsResource(newsId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun toggleBookmark(bookmarked: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleBookmark(newsId, bookmarked)
        }
    }
}
