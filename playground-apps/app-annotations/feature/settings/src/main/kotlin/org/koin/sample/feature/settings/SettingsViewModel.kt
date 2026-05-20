package org.koin.sample.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import org.koin.sample.data.repository.UserDataRepository

@KoinViewModel
class SettingsViewModel(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> =
        userDataRepository.userData
            .map { it.darkThemeEnabled }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun toggleDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            userDataRepository.setDarkTheme(enabled)
        }
    }
}
