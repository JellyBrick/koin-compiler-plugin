package org.koin.sample.notifications

import android.util.Log
import org.koin.core.annotation.Factory
import org.koin.sample.model.NewsResource

@Factory
class NoOpNotifier : Notifier {
    override fun postNewsNotifications(newsResources: List<NewsResource>) {
        Log.d("Notifier", "NoOp: ${newsResources.size} news resources")
    }
}
