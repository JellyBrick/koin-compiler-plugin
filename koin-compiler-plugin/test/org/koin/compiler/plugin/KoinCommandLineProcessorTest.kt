package org.koin.compiler.plugin

import org.jetbrains.kotlin.config.CompilerConfiguration
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KoinCommandLineProcessorTest {

    @Test
    fun `publishHints option is registered and processed`() {
        val processor = KoinCommandLineProcessor()
        val option = processor.pluginOptions.single {
            it.optionName == KoinCommandLineProcessor.OPTION_PUBLISH_HINTS
        }
        val configuration = CompilerConfiguration()

        processor.processOption(option, "false", configuration)

        assertFalse(configuration.get(KoinConfigurationKeys.PUBLISH_HINTS, true))

        processor.processOption(option, "true", configuration)

        assertTrue(configuration.get(KoinConfigurationKeys.PUBLISH_HINTS, false))
    }
}
