package org.koin.sample.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import java.io.File

@Module
@Configuration
@ComponentScan("org.koin.sample.datastore")
class DataStoreModule {

    @Singleton
    fun providesDataStore(
        context: Context,
        scope: CoroutineScope,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = scope,
        produceFile = { File(context.filesDir, "datastore/user_preferences.preferences_pb") },
    )
}
