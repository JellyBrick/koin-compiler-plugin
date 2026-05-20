package org.koin.sample.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.sample.data.repository.UserDataRepository

class MainActivityViewModel(
    userDataRepository: UserDataRepository,
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> =
        userDataRepository.userData
            .map { it.darkThemeEnabled }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
}
