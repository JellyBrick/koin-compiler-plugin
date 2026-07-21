// RUN_PIPELINE_TILL: BACKEND
// FILE: ports.kt
// ClassDef variant of cross_module_sibling_koinapp_ok.kt (hints ON, the default).
//
// The provider is a component-scanned CLASS definition (DefaultGreeter) in a plain @Module
// (no @Configuration); the consumer FeatureModule is linked to it only via
// @KoinApplication(modules = [...]). Unlike the FunctionDef shape of #51, component-scanned
// ClassDefs were excluded from the KTZ-4256 provider-hint oracle wholesale, so GreeterPort
// looked "genuinely missing" at A2 and hard-errored KOIN-D001 even though the entry-point
// graph is complete (this exact shape sat hidden in box/safety/startkoin_full_graph.kt, whose
// runner never asserted compiler messages). The exclusion must only apply to label-gated
// (@Configuration) modules; here the binding defers and settles at A3. No diagnostic must fire.
package app.port

interface GreeterPort {
    fun greet(): String
}

// FILE: impl.kt
package impl.greet

import app.port.GreeterPort
import org.koin.core.annotation.Single

@Single
class DefaultGreeter : GreeterPort {
    override fun greet(): String = "hello"
}

// FILE: main.kt
import app.port.GreeterPort
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.plugin.module.dsl.startKoin

class Service(val greeter: GreeterPort)

@Module
@ComponentScan("impl.greet")
class ImplScanModule

@Module
class FeatureModule {
    @Factory
    fun service(greeter: GreeterPort): Service = Service(greeter)
}

@KoinApplication(modules = [ImplScanModule::class, FeatureModule::class])
object MyApp

fun main() {
    startKoin<MyApp> {}
}

/* GENERATED_FIR_TAGS: classDeclaration, classReference, collectionLiteral, functionDeclaration, interfaceDeclaration,
lambdaLiteral, objectDeclaration, override, primaryConstructor, propertyDeclaration, stringLiteral */
