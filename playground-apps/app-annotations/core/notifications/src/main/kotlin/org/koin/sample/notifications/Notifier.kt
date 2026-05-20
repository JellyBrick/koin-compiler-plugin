package org.koin.sample.notifications

import org.koin.sample.model.NewsResource

interface Notifier {
    fun postNewsNotifications(newsResources: List<NewsResource>)
}
