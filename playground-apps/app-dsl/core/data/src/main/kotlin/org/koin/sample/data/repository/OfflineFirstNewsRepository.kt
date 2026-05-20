package org.koin.sample.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.sample.database.dao.NewsResourceDao
import org.koin.sample.database.entity.NewsResourceEntity
import org.koin.sample.model.NewsResource
import org.koin.sample.network.NetworkDataSource
import org.koin.sample.notifications.Notifier

class OfflineFirstNewsRepository(
    private val newsResourceDao: NewsResourceDao,
    private val network: NetworkDataSource,
    private val notifier: Notifier,
) : NewsRepository {

    override fun getNewsResources(): Flow<List<NewsResource>> =
        newsResourceDao.getAll().map { entities -> entities.map { it.toModel() } }

    override fun getNewsResource(id: String): Flow<NewsResource> =
        newsResourceDao.getById(id).map { it?.toModel() ?: NewsResource(id, "Unknown", "", emptyList()) }

    override suspend fun syncWith(): Boolean {
        val networkNews = network.getNewsResources()
        newsResourceDao.insertAll(networkNews.map { it.toEntity() })
        notifier.postNewsNotifications(networkNews)
        return true
    }
}

private fun NewsResourceEntity.toModel() = NewsResource(id, title, content, emptyList())
private fun NewsResource.toEntity() = NewsResourceEntity(id, title, content)
