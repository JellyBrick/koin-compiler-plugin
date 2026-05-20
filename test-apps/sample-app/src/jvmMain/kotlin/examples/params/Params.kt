package examples.params

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@Module
@ComponentScan
@Configuration
@KoinApplication
class ParamsAppModule

@Factory
data class MyFactory(@InjectedParam val param1: String, @InjectedParam val param2: String)

@Factory
data class MyFactory2(@InjectedParam val param1: String, @InjectedParam val param2: Int)