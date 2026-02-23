// FILE: test.kt
import org.koin.dsl.koinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Singleton
import org.koin.core.annotation.Scoped
import org.koin.core.annotation.Scope
import org.koin.core.qualifier.named

// Scoped definitions requiring root-scope deps should pass safety check
// (root-scope is visible to all scopes)
@Module
@ComponentScan
class TestModule

class SessionScope

@Singleton
class AuthService

@Scoped
@Scope(SessionScope::class)
class SessionData(val auth: AuthService)

fun box(): String {
    val koin = koinApplication {
        modules(TestModule().module())
    }.koin

    val scope = koin.createScope("session1", named<SessionScope>())
    val sessionData = scope.get<SessionData>()

    scope.close()

    return if (sessionData.auth != null) "OK" else "FAIL"
}
