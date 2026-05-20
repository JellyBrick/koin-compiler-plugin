package org.koin.sample.database.di

import android.content.Context
import androidx.room.Room
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.sample.database.AppDatabase

@Module
internal object DatabaseModule {

    @Singleton
    fun providesDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "stress-test-database",
        ).build()
}
