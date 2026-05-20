package org.koin.sample.analytics

import android.util.Log
import jakarta.inject.Inject
import org.koin.core.annotation.Singleton

@Singleton
class StubAnalyticsHelper @Inject constructor() : AnalyticsHelper {
    override fun logEvent(name: String, params: Map<String, String>) {
        Log.d("Analytics", "Event: $name, params: $params")
    }

    override fun setUserProperty(name: String, value: String) {
        Log.d("Analytics", "UserProperty: $name = $value")
    }
}
