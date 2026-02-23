// FILE: test.kt
import org.koin.dsl.koinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Singleton

// List<T> parameter without providers should NOT trigger a safety error
// (getAll() returns empty list)
@Module
@ComponentScan
class TestModule

interface Plugin

@Singleton
class PluginManager(val plugins: List<Plugin>)

fun box(): String {
    val koin = koinApplication {
        modules(TestModule().module())
    }.koin

    val manager = koin.get<PluginManager>()

    return if (manager.plugins.isEmpty()) "OK" else "FAIL: list should be empty"
}
