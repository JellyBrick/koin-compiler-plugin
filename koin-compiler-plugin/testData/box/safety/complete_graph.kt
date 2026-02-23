// FILE: test.kt
import org.koin.dsl.koinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Singleton
import org.koin.core.annotation.Factory

// A complete dependency graph: all deps satisfied
@Module
@ComponentScan
class TestModule

@Singleton
class Repository

@Singleton
class Service(val repo: Repository)

@Factory
class Presenter(val service: Service)

fun box(): String {
    val koin = koinApplication {
        modules(TestModule().module())
    }.koin

    val presenter = koin.get<Presenter>()
    val service = koin.get<Service>()

    return if (presenter.service === service && service.repo != null) "OK" else "FAIL"
}
