// RUN_PIPELINE_TILL: BACKEND
// FILE: scopes.kt
// Repro for the visibility-set dedup false positive (truloop CoroutineScope shape).
//
// Two @Configuration sibling modules provide the SAME type with DIFFERENT qualifiers:
// EventModule provides @Named("eventBus") WorkScope, InfraModule provides the unqualified
// WorkScope. A component-scanned consumer needs the unqualified one. Both providers are
// assembled at the entry point, so the graph is complete and NO diagnostic must fire.
//
// Before the fix, buildVisibleDefinitions deduplicated sibling definitions by bare
// return-type FqName: whichever WorkScope definition was seen first (the @Named one)
// consumed the type's slot and the unqualified provider was silently dropped from the A2
// visibility set. The consumer then hard-errored KOIN-D001 with the misleading hint
// "Found similar binding: WorkScope with qualifier @Named(\"eventBus\")". The dedup key
// must be (type, qualifier, scope) — the identity of a provider — not the bare type.
package app.scopes

class WorkScope

// FILE: usecase.kt
package app.usecase

import app.scopes.WorkScope
import org.koin.core.annotation.Single

@Single
class Worker(val scope: WorkScope)

// FILE: main.kt
import app.scopes.WorkScope
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.koin.plugin.module.dsl.startKoin

// Declared BEFORE InfraModule so its @Named definition is the first WorkScope the
// visibility builder sees — the order that used to shadow the unqualified provider.
@Module
@Configuration
class EventModule {
    @Single
    @Named("eventBus")
    fun eventBusScope(): WorkScope = WorkScope()
}

@Module
@Configuration
class InfraModule {
    @Single
    fun workScope(): WorkScope = WorkScope()
}

@Module
@Configuration
@ComponentScan("app.usecase")
class UseCaseScanModule

@KoinApplication(modules = [EventModule::class, InfraModule::class, UseCaseScanModule::class])
object MyApp

fun main() {
    startKoin<MyApp> {}
}

/* GENERATED_FIR_TAGS: classDeclaration, classReference, collectionLiteral, functionDeclaration, lambdaLiteral,
objectDeclaration, primaryConstructor, propertyDeclaration, stringLiteral */
