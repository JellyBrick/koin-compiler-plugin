package org.koin.sample.sync.di

import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single
import org.koin.plugin.module.dsl.worker
import org.koin.sample.sync.SyncManager
import org.koin.sample.sync.SyncWorker
import org.koin.sample.sync.WorkManagerSyncManager

val syncModule = module {
    single<WorkManagerSyncManager>() bind SyncManager::class
    worker<SyncWorker>()
}
