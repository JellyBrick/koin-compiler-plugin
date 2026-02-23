// FILE: test.kt
import org.koin.dsl.koinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Singleton
import org.koin.core.annotation.Single

// Definitions from included modules should be visible for safety checks
@Module
class InfraModule {
    @Single
    fun provideDatabase(): Database = Database()
}

class Database

@Module(includes = [InfraModule::class])
class AppModule {
    @Single
    fun provideService(db: Database): AppService = AppService(db)
}

class AppService(val db: Database)

fun box(): String {
    val koin = koinApplication {
        modules(AppModule().module())
    }.koin

    val service = koin.get<AppService>()

    return if (service.db != null) "OK" else "FAIL"
}
