package org.koin.sample.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import org.koin.sample.data.repository.UserDataRepository
import org.koin.sample.data.repository.UserNewsResourceRepository
import org.koin.sample.domain.GetFollowableTopicsUseCase
import org.koin.sample.model.FollowableTopic
import org.koin.sample.model.UserNewsResource

@KoinViewModel
class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userNewsResourceRepository: UserNewsResourceRepository,
    private val userDataRepository: UserDataRepository,
    private val getFollowableTopics: GetFollowableTopicsUseCase,
) : ViewModel() {

    val newsResources: StateFlow<List<UserNewsResource>> =
        userNewsResourceRepository.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val followableTopics: StateFlow<List<FollowableTopic>> =
        getFollowableTopics()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun toggleBookmark(newsId: String, bookmarked: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleBookmark(newsId, bookmarked)
        }
    }

    fun toggleFollowedTopic(topicId: String, followed: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleFollowedTopic(topicId, followed)
        }
    }
}
