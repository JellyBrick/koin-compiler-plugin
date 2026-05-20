// FILE: test.kt
// Regression: two Unit-returning @Singleton functions distinguished only by qualifier
// must NOT collide in the InjectedParam shape index. Without the ambiguity guard the
// plugin keys slots by return-type FqName alone and false-positives KOIN-D006 on the
// qualifier-only-different sibling that legitimately has no @InjectedParam.
//
// Real-world trigger: KotlinConf 2026 app — `koin.get<Unit>(named("..."))` "launcher"
// pattern with two @Singleton fun returning Unit, one with @InjectedParam slot, one
// without. Plugin must skip D005/D006 for ambiguous targets.

import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import org.koin.core.annotation.Module
import org.koin.core.annotation.ComponentScan
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication

@Module
@ComponentScan
class LaunchersModule

@Named("initWithParam")
@Singleton
fun initWithParam(@InjectedParam tag: String) {
    // side-effect launcher with one injected param
    check(tag == "x")
}

@Named("initNoParam")
@Singleton
fun initNoParam() {
    // side-effect launcher with no injected params — legitimately called without parametersOf
}

fun box(): String {
    val koin = koinApplication {
        modules(LaunchersModule().module())
    }.koin

    koin.get<Unit>(named("initWithParam")) { parametersOf("x") }
    koin.get<Unit>(named("initNoParam"))

    return "OK"
}
