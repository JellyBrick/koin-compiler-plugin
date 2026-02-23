// RUN_PIPELINE_TILL: BACKEND
// FILE: test.kt
import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Singleton

// Service requires Repository, but Repository is NOT provided
@Module
@ComponentScan
class TestModule

@Singleton
class Repository

@Singleton
class Service(val repo: Repository, val missing: MissingDep)

class MissingDep

/* GENERATED_FIR_TAGS: classDeclaration, primaryConstructor, propertyDeclaration */
