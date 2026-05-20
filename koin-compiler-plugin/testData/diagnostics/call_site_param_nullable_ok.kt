// RUN_PIPELINE_TILL: BACKEND
// FILE: test.kt
// Slot is `name: String?` (nullable) — a non-null String literal is a valid arg. No diagnostic.
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
class Greeter(@InjectedParam val name: String?)

fun useIt() {
    val koin = koinApplication { modules(TestModule().module()) }.koin
    val g = koin.get<Greeter> { parametersOf("hello") }
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, lambdaLiteral, localProperty,
   nullableType, primaryConstructor, propertyDeclaration, stringLiteral */
