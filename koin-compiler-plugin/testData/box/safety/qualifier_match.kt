// FILE: test.kt
import org.koin.dsl.koinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Singleton
import org.koin.core.annotation.Named
import org.koin.core.qualifier.named

// Qualified dependency with matching provider should pass safety check
@Module
@ComponentScan
class TestModule

interface Cache

@Singleton
@Named("local")
class LocalCache : Cache

@Singleton
@Named("remote")
class RemoteCache : Cache

@Singleton
class CacheManager(
    @Named("local") val local: Cache,
    @Named("remote") val remote: Cache
)

fun box(): String {
    val koin = koinApplication {
        modules(TestModule().module())
    }.koin

    val manager = koin.get<CacheManager>()

    val localOk = manager.local is LocalCache
    val remoteOk = manager.remote is RemoteCache

    return if (localOk && remoteOk) "OK" else "FAIL: local=$localOk, remote=$remoteOk"
}
