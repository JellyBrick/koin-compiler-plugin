package org.koin.sample.app.di

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.logger.Level
import org.koin.dsl.koinApplication
import org.koin.plugin.module.dsl.module
import org.koin.plugin.module.dsl.modules

class KoinTest {

    // for debugging purpose only
    @OptIn(KoinInternalApi::class)
    @Test
    fun testKoin() {

        val koin = koinApplication {
            printLogger(level = Level.DEBUG)
            module<ActivityModule>()
        }.koin

        assertEquals(1,koin.instanceRegistry.instances.size)
    }

    @OptIn(KoinInternalApi::class)
    @Test
    fun testKoin2() {

        val koin = koinApplication {
            printLogger(level = Level.DEBUG)
            modules(ActivityModule::class, FeaturesModule::class)
        }.koin

        assertEquals(6,koin.instanceRegistry.instances.size)
    }
}
