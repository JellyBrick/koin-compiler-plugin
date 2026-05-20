package org.koin.sample.data.repository

import kotlinx.coroutines.flow.Flow
import org.koin.sample.model.UserData

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun toggleBookmark(newsId: String, bookmarked: Boolean)
    suspend fun toggleFollowedTopic(topicId: String, followed: Boolean)
    suspend fun setDarkTheme(enabled: Boolean)
}
