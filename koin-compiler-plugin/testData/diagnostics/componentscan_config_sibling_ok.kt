// RUN_PIPELINE_TILL: BACKEND
// FILE: ports.kt
// Twin of componentscan_config_sibling_publish_hints_off_ok.kt with hint publication left ON
// (the default). Same-compilation @Configuration siblings must resolve each other's
// component-scanned definitions at A2 regardless of how they are discovered (in-memory module
// list or configuration_default hint callables). Guards that the hint-independent local
// discovery does not regress the hints-on path. No diagnostic must fire.
package app.port

interface GreeterPort {
    fun greet(): String
}

// FILE: usecase.kt
package app.usecase

import app.port.GreeterPort
import org.koin.core.annotation.Single

@Single
class GreetUseCase(val greeter: GreeterPort)

// FILE: impl.kt
package boot.impl

import app.port.GreeterPort
import org.koin.core.annotation.Single

@Single
class DefaultGreeter : GreeterPort {
    override fun greet(): String = "hello"
}

// FILE: main.kt
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.plugin.module.dsl.startKoin

@Module
@Configuration
@ComponentScan("app.usecase")
class UseCaseScanModule

@Module
@Configuration
@ComponentScan("boot.impl")
class ImplScanModule

@KoinApplication(modules = [UseCaseScanModule::class, ImplScanModule::class])
object MyApp

fun main() {
    startKoin<MyApp> {}
}

/* GENERATED_FIR_TAGS: classDeclaration, classReference, collectionLiteral, functionDeclaration, interfaceDeclaration,
lambdaLiteral, objectDeclaration, override, primaryConstructor, propertyDeclaration, stringLiteral */
