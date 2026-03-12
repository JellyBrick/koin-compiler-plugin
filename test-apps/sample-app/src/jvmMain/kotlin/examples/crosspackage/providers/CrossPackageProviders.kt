package examples.crosspackage.providers

import org.koin.core.annotation.*
import examples.crosspackage.models.*

/**
 * Provider functions in package "providers" that return types from package "models".
 * This tests the funcpkg_* hint parameter: @ComponentScan("examples.crosspackage.providers")
 * must find these functions even though their return types are in a different package.
 */

@Singleton
fun provideApiClient(): ApiClient = OkHttpApiClient()

@Singleton
@Named("app")
fun provideAppSettings(): AppSettings = AppSettings(debug = true)
