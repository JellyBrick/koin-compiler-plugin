package org.koin.sample.database.di

import android.content.Context
import androidx.room.Room
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.sample.database.AppDatabase
import org.koin.sample.database.dao.NewsResourceDao
import org.koin.sample.database.dao.RecentSearchDao
import org.koin.sample.database.dao.TopicDao

fun database(context: Context): AppDatabase
    = Room.databaseBuilder(context,
        AppDatabase::class.java,
        "stress-test-database",
    ).build()

fun topicDao(db : AppDatabase): TopicDao = db.topicDao()
fun newsResourceDao(db : AppDatabase): NewsResourceDao = db.newsResourceDao()
fun recentSearchDao(db : AppDatabase): RecentSearchDao = db.recentSearchDao()

val databaseModule = module {
    single { create(::database) }
    single { create(::topicDao) }
    single { create(::newsResourceDao) }
    single { create(::recentSearchDao) }
}
