package org.koin.sample.app.di

import org.koin.core.annotation.InjectedParam
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory
import org.koin.plugin.module.dsl.viewModel
import org.koin.sample.analytics.di.analyticsModule
import org.koin.sample.app.MainActivityViewModel
import org.koin.sample.common.di.dispatchersModule
import org.koin.sample.data.di.dataModule
import org.koin.sample.database.di.databaseModule
import org.koin.sample.datastore.di.dataStoreModule
import org.koin.sample.domain.GetFollowableTopicsUseCase
import org.koin.sample.domain.GetRecentSearchesUseCase
import org.koin.sample.domain.GetSearchContentsUseCase
import org.koin.sample.feature.bookmarks.BookmarksViewModel
import org.koin.sample.feature.detail.DetailViewModel
import org.koin.sample.feature.home.HomeViewModel
import org.koin.sample.feature.settings.SettingsViewModel
import org.koin.sample.network.di.networkModule
import org.koin.sample.notifications.di.notificationsModule
import org.koin.sample.sync.di.syncModule

// always keep the appModule as the last one, to avoid any dependency issues with other modules
val useCasesModule = module {
    // Domain use cases
    factory<GetFollowableTopicsUseCase>()
    factory<GetRecentSearchesUseCase>()
    factory<GetSearchContentsUseCase>()
}

val appModule = module {
    includes(
        activityModule,
        useCasesModule,
        dispatchersModule,
        analyticsModule,
        notificationsModule,
        databaseModule,
        dataStoreModule,
        networkModule,
        dataModule,
        syncModule,
    )

    // Feature ViewModels
    viewModel<MainActivityViewModel>()
    viewModel<HomeViewModel>()
    viewModel<BookmarksViewModel>()
    viewModel<DetailViewModel>()
    viewModel<SettingsViewModel>()


//    factory<A>()
//    factory<B>()
//    factory<MyInj>()
}

//class A(val b : B)
//class B(val a : A)

//class MyInj(@InjectedParam val id : String)