package org.koin.sample.data.repository

import kotlinx.coroutines.flow.Flow
import org.koin.sample.analytics.AnalyticsHelper
import org.koin.sample.datastore.UserPreferencesDataSource
import org.koin.sample.model.UserData

class OfflineFirstUserDataRepository(
    private val preferencesDataSource: UserPreferencesDataSource,
    private val analyticsHelper: AnalyticsHelper,
) : UserDataRepository {

    override val userData: Flow<UserData> = preferencesDataSource.userData

    override suspend fun toggleBookmark(newsId: String, bookmarked: Boolean) {
        preferencesDataSource.toggleBookmark(newsId, bookmarked)
        analyticsHelper.logEvent("bookmark_toggled", mapOf("newsId" to newsId, "bookmarked" to "$bookmarked"))
    }

    override suspend fun toggleFollowedTopic(topicId: String, followed: Boolean) {
        preferencesDataSource.toggleFollowedTopic(topicId, followed)
        analyticsHelper.logEvent("topic_follow_toggled", mapOf("topicId" to topicId, "followed" to "$followed"))
    }

    override suspend fun setDarkTheme(enabled: Boolean) {
        preferencesDataSource.setDarkTheme(enabled)
    }
}
