// RUN_PIPELINE_TILL: BACKEND
// PUBLISH_HINTS_OFF
// FILE: ports.kt
// Repro for the truloop-core false positive (publishHints=false regression of KTZ-4256 / #51).
//
// Two @Configuration (default label) @ComponentScan modules in the SAME compilation:
// UseCaseScanModule scans app.usecase (GreetUseCase needs GreeterPort), ImplScanModule scans
// boot.impl (DefaultGreeter provides GreeterPort via auto-binding). Both are assembled at the
// @KoinApplication entry point, so the graph is complete and NO diagnostic must fire.
//
// With publishHints=false, FIR generates no configuration_default hint callables, so A2 sibling
// discovery found nothing and GreeterPort was unresolved in UseCaseScanModule's visibility set.
// The KTZ-4256 provider-hint oracle then also came up empty (component-scanned ClassDefs are
// excluded from the local source, and no hints exist), so instead of deferring to A3 the plugin
// hard-errored KOIN-D001 — a false positive. Local @Configuration siblings must be discovered
// from the in-memory module list, independent of hint publication.
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
