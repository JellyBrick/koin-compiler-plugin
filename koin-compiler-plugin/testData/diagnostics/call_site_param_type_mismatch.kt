// RUN_PIPELINE_TILL: BACKEND
// FILE: test.kt
// `parametersOf(42)` (Int) doesn't match the slot `name: String` → KOIN-D005 TYPE.
package testpkg

import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication

@Module
@ComponentScan("testpkg")
class TestModule

@Factory
class Greeter(@InjectedParam val name: String)

fun useIt() {
    val koin = koinApplication { modules(TestModule().module()) }.koin
    val g = koin.get<Greeter> { parametersOf(42) }
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, integerLiteral, lambdaLiteral,
   localProperty, primaryConstructor, propertyDeclaration */
