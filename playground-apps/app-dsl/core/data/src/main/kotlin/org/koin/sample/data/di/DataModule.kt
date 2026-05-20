package org.koin.sample.data.di

import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single
import org.koin.sample.analytics.di.analyticsModule
import org.koin.sample.data.repository.CompositeUserNewsResourceRepository
import org.koin.sample.data.repository.DefaultRecentSearchRepository
import org.koin.sample.data.repository.DefaultSearchContentsRepository
import org.koin.sample.data.repository.NewsRepository
import org.koin.sample.data.repository.OfflineFirstNewsRepository
import org.koin.sample.data.repository.OfflineFirstTopicsRepository
import org.koin.sample.data.repository.OfflineFirstUserDataRepository
import org.koin.sample.data.repository.RecentSearchRepository
import org.koin.sample.data.repository.SearchContentsRepository
import org.koin.sample.data.repository.TopicsRepository
import org.koin.sample.data.repository.UserDataRepository
import org.koin.sample.data.repository.UserNewsResourceRepository
import org.koin.sample.database.di.databaseModule
import org.koin.sample.datastore.di.dataStoreModule
import org.koin.sample.network.di.networkModule

val dataModule = module {
    includes(databaseModule, dataStoreModule, analyticsModule, networkModule)

    single<OfflineFirstNewsRepository>() bind NewsRepository::class
    single<OfflineFirstTopicsRepository>() bind TopicsRepository::class
    single<OfflineFirstUserDataRepository>() bind UserDataRepository::class
    single<CompositeUserNewsResourceRepository>() bind UserNewsResourceRepository::class
    single<DefaultRecentSearchRepository>() bind RecentSearchRepository::class
    single<DefaultSearchContentsRepository>() bind SearchContentsRepository::class
}
