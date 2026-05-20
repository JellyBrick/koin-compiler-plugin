// RUN_PIPELINE_TILL: BACKEND
// FILE: test.kt
// Def has NO @InjectedParam — `koin.get<Service>()` without parametersOf must NOT fire D006.
// Sanity check that the guard rule only triggers when the def actually needs params.
package testpkg

import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Singleton
import org.koin.dsl.koinApplication

@Module
@ComponentScan("testpkg")
class TestModule

@Singleton
class Service

fun useIt() {
    val koin = koinApplication { modules(TestModule().module()) }.koin
    val s = koin.get<Service>()
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, lambdaLiteral, localProperty,
   primaryConstructor */
