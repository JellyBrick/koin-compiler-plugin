package org.koin.sample.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single
import org.koin.sample.common.di.dispatchersModule
import org.koin.sample.datastore.UserPreferencesDataSource
import java.io.File

fun datastore(cs : CoroutineScope, context: Context): DataStore<Preferences>
    = PreferenceDataStoreFactory.create(
        scope = cs,
        produceFile = {
            File(
                context.filesDir,
                "datastore/user_preferences.preferences_pb"
            )
        },
    )

val dataStoreModule = module {
    includes(dispatchersModule)

    single<DataStore<Preferences>> { create(::datastore) }
    single<UserPreferencesDataSource>()
}
