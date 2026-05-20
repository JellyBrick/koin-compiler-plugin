// RUN_PIPELINE_TILL: BACKEND
// FILE: test.kt
// Reproducer for predicate-dep check: @KoinWorker is present but the
// `koin-android-workmanager` artifact (which provides Module.buildWorker)
// is not on the classpath. Plugin must emit a compile error naming the
// missing artifact, instead of silently skipping the definition.
package testpkg

import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.android.annotation.KoinWorker

@Module
@ComponentScan("testpkg")
class AppModule

@KoinWorker
class MyWorker

/* GENERATED_FIR_TAGS: classDeclaration, primaryConstructor */
