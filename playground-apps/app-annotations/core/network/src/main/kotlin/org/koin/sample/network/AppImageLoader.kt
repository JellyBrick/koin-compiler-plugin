package org.koin.sample.network

import android.content.Context
import android.util.Log

/**
 * Simple image loader placeholder — demonstrates Lazy<T> injection
 * (mirrors NIA's ImageLoader with Lazy<Call.Factory>).
 */
class AppImageLoader(
    private val context: Context,
    httpClientProvider: Lazy<AppHttpClient>,
) {
    // Lazy access — defers initialization (like NIA's Lazy<Call.Factory> for OkHttp)
    private val httpClient by lazy { httpClientProvider.value }

    fun load(url: String) {
        Log.d("AppImageLoader", "Loading image: $url via ${httpClient.get(url)}")
    }
}
