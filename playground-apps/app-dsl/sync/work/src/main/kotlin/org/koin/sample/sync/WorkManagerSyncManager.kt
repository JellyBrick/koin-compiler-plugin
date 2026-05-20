package org.koin.sample.sync

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map

class WorkManagerSyncManager(
    context: Context,
) : SyncManager {

    private val workManager = WorkManager.getInstance(context)

    override val isSyncing: Flow<Boolean> =
        workManager.getWorkInfosForUniqueWorkFlow(SYNC_WORK_NAME)
            .map { it.any { workInfo -> workInfo.state == WorkInfo.State.RUNNING } }
            .conflate()

    override fun requestSync() {
        workManager.enqueueUniqueWork(
            SYNC_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            OneTimeWorkRequestBuilder<SyncWorker>().build(),
        )
    }

    companion object {
        private const val SYNC_WORK_NAME = "SyncWorkName"
    }
}
