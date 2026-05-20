package org.koin.sample.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.koin.sample.database.dao.NewsResourceDao
import org.koin.sample.database.dao.RecentSearchDao
import org.koin.sample.database.dao.TopicDao
import org.koin.sample.database.entity.NewsResourceEntity
import org.koin.sample.database.entity.NewsResourceTopicCrossRef
import org.koin.sample.database.entity.RecentSearchEntity
import org.koin.sample.database.entity.TopicEntity

@Database(
    entities = [
        TopicEntity::class,
        NewsResourceEntity::class,
        NewsResourceTopicCrossRef::class,
        RecentSearchEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun newsResourceDao(): NewsResourceDao
    abstract fun recentSearchDao(): RecentSearchDao
}
