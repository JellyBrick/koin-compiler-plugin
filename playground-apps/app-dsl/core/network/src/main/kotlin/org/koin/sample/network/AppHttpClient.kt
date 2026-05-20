package org.koin.sample.network

import android.util.Log

/**
 * Simple HTTP client placeholder — used to demonstrate Lazy<T> injection pattern.
 */
class AppHttpClient {
    fun get(url: String): String {
        Log.d("AppHttpClient", "GET $url")
        return "{}"
    }
}
