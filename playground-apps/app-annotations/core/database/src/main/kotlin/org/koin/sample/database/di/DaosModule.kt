package org.koin.sample.database.di

import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.sample.database.AppDatabase
import org.koin.sample.database.dao.NewsResourceDao
import org.koin.sample.database.dao.RecentSearchDao
import org.koin.sample.database.dao.TopicDao

@Module(includes = [DatabaseModule::class])
@Configuration
class DaosModule {

    @Singleton
    fun providesTopicDao(database: AppDatabase): TopicDao = database.topicDao()

    @Singleton
    fun providesNewsResourceDao(database: AppDatabase): NewsResourceDao = database.newsResourceDao()

    @Singleton
    fun providesRecentSearchDao(database: AppDatabase): RecentSearchDao = database.recentSearchDao()
}
