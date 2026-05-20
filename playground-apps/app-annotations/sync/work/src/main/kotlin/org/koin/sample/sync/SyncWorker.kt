package org.koin.sample.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinWorker
import org.koin.sample.common.Dispatcher
import org.koin.sample.common.NiaDispatchers
import org.koin.sample.data.repository.NewsRepository
import org.koin.sample.data.repository.TopicsRepository

@KoinWorker
class SyncWorker(
    context: Context,
    params: WorkerParameters,
    private val topicsRepository: TopicsRepository,
    private val newsRepository: NewsRepository,
    @Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        try {
            topicsRepository.syncWith()
            newsRepository.syncWith()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
