package org.koin.sample.analytics

interface AnalyticsHelper {
    fun logEvent(name: String, params: Map<String, String> = emptyMap())
    fun setUserProperty(name: String, value: String)
}
