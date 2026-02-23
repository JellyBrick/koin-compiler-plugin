// FILE: test.kt
import org.koin.dsl.koinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Singleton

// Lazy<T> with T provided should pass safety check
@Module
@ComponentScan
class TestModule

@Singleton
class HeavyService

@Singleton
class Consumer(val heavy: Lazy<HeavyService>)

fun box(): String {
    val koin = koinApplication {
        modules(TestModule().module())
    }.koin

    val consumer = koin.get<Consumer>()
    val heavy = consumer.heavy.value

    return if (heavy != null) "OK" else "FAIL"
}
