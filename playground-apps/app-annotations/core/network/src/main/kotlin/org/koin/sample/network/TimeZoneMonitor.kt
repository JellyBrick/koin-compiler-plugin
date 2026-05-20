package org.koin.sample.network

import kotlinx.coroutines.flow.Flow
import java.util.TimeZone

interface TimeZoneMonitor {
    val currentTimeZone: Flow<TimeZone>
}
