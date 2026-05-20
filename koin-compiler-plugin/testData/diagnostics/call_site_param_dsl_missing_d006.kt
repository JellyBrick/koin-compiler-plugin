// RUN_PIPELINE_TILL: BACKEND
// FILE: test.kt
// DSL-only registration (no @Module / @Singleton): `factory<Greeter>()` registers a class with
// an `@InjectedParam` slot. Calling `get<Greeter>()` without `parametersOf(...)` MUST still
// fire KOIN-D006 — equivalent to the annotation-based path.
package testpkg

import org.koin.core.annotation.InjectedParam
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

class Greeter(@InjectedParam val name: String)

val myModule = module { factory<Greeter>() }

fun useIt() {
    val koin = koinApplication { modules(myModule) }.koin
    val g = koin.get<Greeter>()
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, lambdaLiteral, localProperty,
   primaryConstructor, propertyDeclaration, topLevelPropertyDeclaration */
