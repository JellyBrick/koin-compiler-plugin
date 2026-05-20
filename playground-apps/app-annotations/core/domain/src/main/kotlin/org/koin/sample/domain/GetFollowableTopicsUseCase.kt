package org.koin.sample.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory
import org.koin.sample.data.repository.TopicsRepository
import org.koin.sample.data.repository.UserDataRepository
import org.koin.sample.model.FollowableTopic

@Factory
class GetFollowableTopicsUseCase(
    private val topicsRepository: TopicsRepository,
    private val userDataRepository: UserDataRepository,
) {
    operator fun invoke(): Flow<List<FollowableTopic>> =
        combine(topicsRepository.getTopics(), userDataRepository.userData) { topics, userData ->
            topics.map { topic ->
                FollowableTopic(
                    topic = topic,
                    isFollowed = topic.id in userData.followedTopicIds,
                )
            }
        }
}
