// FILE: test.kt
import org.koin.dsl.koinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Singleton

// Nullable parameter without a provider should NOT trigger a safety error
@Module
@ComponentScan
class TestModule

class MissingDep

@Singleton
class Service(val dep: MissingDep?)

fun box(): String {
    val koin = koinApplication {
        modules(TestModule().module())
    }.koin

    val service = koin.get<Service>()

    // dep should be null since MissingDep is not provided
    return if (service.dep == null) "OK" else "FAIL: nullable dep should be null"
}
