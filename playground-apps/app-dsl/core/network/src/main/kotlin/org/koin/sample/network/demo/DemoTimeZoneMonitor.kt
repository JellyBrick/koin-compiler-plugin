package org.koin.sample.network.demo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.sample.network.TimeZoneMonitor
import java.util.TimeZone

class DemoTimeZoneMonitor : TimeZoneMonitor {
    override val currentTimeZone: Flow<TimeZone> = flowOf(TimeZone.getDefault())
}
