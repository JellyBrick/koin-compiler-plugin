// RUN_PIPELINE_TILL: BACKEND
// FILE: test.kt
// Repro for the truloop-core RequestContext false positive.
//
// A DSL definition whose USER-AUTHORED lambda constructs the instance from runtime call-site
// parameters: `factory { params -> SessionHolder(params.get()) }`. The transformer records this
// shape as a provider-only DslDef (issues #36/#49): the plugin never wires SessionHolder's
// constructor, the user's lambda does. Its constructor parameters are therefore NOT graph
// requirements — Connection arrives via parametersOf(...) at the call site.
//
// Before the fix, A3 full-graph validation at the typed entry point analyzed the primary
// constructor of provider-only DslDefs anyway and hard-errored
// KOIN-D001 "Missing dependency: Connection required by dsl:SessionHolder (parameter
// 'connection')". No diagnostic must fire.
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.dsl.module
import org.koin.plugin.module.dsl.startKoin

class Connection

class SessionHolder(val connection: Connection)

class Clock

val sessionModule = module {
    factory { params -> SessionHolder(params.get()) }
}

@Module
class CoreModule {
    @Single
    fun clock(): Clock = Clock()
}

@KoinApplication(modules = [CoreModule::class])
object MyApp

fun main() {
    startKoin<MyApp> {}
}

/* GENERATED_FIR_TAGS: classDeclaration, classReference, collectionLiteral, functionDeclaration, lambdaLiteral,
objectDeclaration, primaryConstructor, propertyDeclaration */
