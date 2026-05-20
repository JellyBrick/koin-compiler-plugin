// RUN_PIPELINE_TILL: BACKEND
// FILE: test.kt
// Def requires @InjectedParam but the call site forgot `parametersOf(...)` entirely → KOIN-D006.
package testpkg

import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.dsl.koinApplication

@Module
@ComponentScan("testpkg")
class TestModule

@Factory
class Greeter(@InjectedParam val name: String)

fun useIt() {
    val koin = koinApplication { modules(TestModule().module()) }.koin
    val g = koin.get<Greeter>()
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, lambdaLiteral, localProperty,
   primaryConstructor, propertyDeclaration */
