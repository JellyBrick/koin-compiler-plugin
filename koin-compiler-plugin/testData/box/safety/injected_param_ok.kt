// FILE: test.kt
import org.koin.dsl.koinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.parameter.parametersOf

// @InjectedParam should NOT trigger a safety error (provided at call site)
@Module
@ComponentScan
class TestModule

@Factory
class Greeter(@InjectedParam val name: String)

fun box(): String {
    val koin = koinApplication {
        modules(TestModule().module())
    }.koin

    val greeter = koin.get<Greeter> { parametersOf("World") }

    return if (greeter.name == "World") "OK" else "FAIL: @InjectedParam not working"
}
