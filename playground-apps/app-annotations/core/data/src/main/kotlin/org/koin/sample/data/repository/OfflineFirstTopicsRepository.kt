package org.koin.sample.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import org.koin.sample.database.dao.TopicDao
import org.koin.sample.database.entity.TopicEntity
import org.koin.sample.model.Topic
import org.koin.sample.network.NetworkDataSource

@Singleton
class OfflineFirstTopicsRepository(
    private val topicDao: TopicDao,
    private val network: NetworkDataSource,
) : TopicsRepository {

    override fun getTopics(): Flow<List<Topic>> =
        topicDao.getAll().map { entities -> entities.map { it.toModel() } }

    override fun getTopic(id: String): Flow<Topic> =
        topicDao.getById(id).map { it?.toModel() ?: Topic(id, "Unknown", "") }

    override suspend fun syncWith(): Boolean {
        val networkTopics = network.getTopics()
        topicDao.insertAll(networkTopics.map { it.toEntity() })
        return true
    }
}

private fun TopicEntity.toModel() = Topic(id, name, description)
private fun Topic.toEntity() = TopicEntity(id, name, description)
