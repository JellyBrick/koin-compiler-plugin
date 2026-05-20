package org.koin.sample.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
)

@Entity(tableName = "news_resources")
data class NewsResourceEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
)

@Entity(tableName = "news_resource_topics")
data class NewsResourceTopicCrossRef(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val newsResourceId: String,
    val topicId: String,
)

@Entity(tableName = "recent_searches")
data class RecentSearchEntity(
    @PrimaryKey val query: String,
    val timestamp: Long,
)
