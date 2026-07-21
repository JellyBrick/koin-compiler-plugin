// RUN_PIPELINE_TILL: BACKEND
// PUBLISH_HINTS_OFF
// FILE: ports.kt
// ClassDef variant of cross_module_sibling_koinapp_ok.kt, with hint publication OFF.
//
// The provider is a component-scanned CLASS definition (DefaultGreeter) in a plain @Module
// (no @Configuration), so A2 sibling visibility does not apply; the modules are linked only
// via @KoinApplication(modules = [...]). With publishHints=false there are no hint callables
// either, so the KTZ-4256 oracle must fall back to the LOCAL definitions of this compilation.
//
// Before the fix, component-scanned ClassDefs were excluded from the local oracle source
// wholesale, so GreeterPort looked "genuinely missing" and A2 hard-errored KOIN-D001. The
// exclusion exists to keep @Configuration label mismatches a hard error (see
// configuration_label_mismatch.kt), so it must only apply to label-gated modules. Here the
// provider module has no configuration label: the unresolved binding defers and settles at
// the complete A3 closure. No diagnostic must fire.
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
