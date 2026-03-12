package examples.crosspackage

import org.koin.core.annotation.*
import examples.crosspackage.models.ApiClient
import examples.crosspackage.models.AppSettings

/**
 * Tests cross-package function discovery: functions in "providers" package
 * return types from "models" package. @ComponentScan scans "providers" only.
 *
 * Without the funcpkg_* hint encoding, the function hints would be indexed
 * by the return type's package ("models") and @ComponentScan("providers")
 * would NOT find them — causing a false "Missing dependency" compile error.
 */

@Singleton
class CrossPackageConsumer(
    val client: ApiClient,
    @Named("app") val settings: AppSettings
)

@Module
@ComponentScan("examples.crosspackage.providers", "examples.crosspackage")
class CrossPackageModule

@KoinApplication(modules = [CrossPackageModule::class])
interface CrossPackageApp
