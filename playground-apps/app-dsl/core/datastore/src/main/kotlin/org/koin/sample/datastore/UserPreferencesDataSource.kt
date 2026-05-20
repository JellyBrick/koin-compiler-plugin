package org.koin.sample.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.sample.model.UserData

class UserPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) {
    private val bookmarkedNewsIds = stringSetPreferencesKey("bookmarked_news_ids")
    private val followedTopicIds = stringSetPreferencesKey("followed_topic_ids")
    private val darkTheme = booleanPreferencesKey("dark_theme")

    val userData: Flow<UserData> = dataStore.data.map { prefs ->
        UserData(
            bookmarkedNewsIds = prefs[bookmarkedNewsIds] ?: emptySet(),
            followedTopicIds = prefs[followedTopicIds] ?: emptySet(),
            darkThemeEnabled = prefs[darkTheme] ?: false,
        )
    }

    suspend fun toggleBookmark(newsId: String, bookmarked: Boolean) {
        dataStore.edit { prefs ->
            val current = prefs[bookmarkedNewsIds] ?: emptySet()
            prefs[bookmarkedNewsIds] = if (bookmarked) current + newsId else current - newsId
        }
    }

    suspend fun toggleFollowedTopic(topicId: String, followed: Boolean) {
        dataStore.edit { prefs ->
            val current = prefs[followedTopicIds] ?: emptySet()
            prefs[followedTopicIds] = if (followed) current + topicId else current - topicId
        }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[darkTheme] = enabled }
    }
}
