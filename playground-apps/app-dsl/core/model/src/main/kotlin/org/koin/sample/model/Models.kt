package org.koin.sample.model

data class Topic(
    val id: String,
    val name: String,
    val description: String,
)

data class NewsResource(
    val id: String,
    val title: String,
    val content: String,
    val topicIds: List<String>,
    val isBookmarked: Boolean = false,
)

data class UserData(
    val bookmarkedNewsIds: Set<String> = emptySet(),
    val followedTopicIds: Set<String> = emptySet(),
    val darkThemeEnabled: Boolean = false,
)

data class FollowableTopic(
    val topic: Topic,
    val isFollowed: Boolean,
)

data class UserNewsResource(
    val newsResource: NewsResource,
    val userData: UserData,
)

data class SearchResult(
    val topics: List<Topic> = emptyList(),
    val newsResources: List<NewsResource> = emptyList(),
)

data class RecentSearch(
    val query: String,
    val timestamp: Long,
)
