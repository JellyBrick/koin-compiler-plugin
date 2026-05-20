// FILE: test.kt
// DSL-registered class with @InjectedParam — calling `get<Greeter>()` without parametersOf
// must fire KOIN-D006. The hint `injectedparams_Greeter(name: kotlin.String)` should appear
// in the IR dump regardless.
import org.koin.dsl.module
import org.koin.dsl.koinApplication
import org.koin.plugin.module.dsl.factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.parameter.parametersOf

class Greeter(@InjectedParam val name: String)

val myModule = module {
    factory<Greeter>()
}

fun box(): String {
    val koin = koinApplication { modules(myModule) }.koin
    // Valid call with parametersOf — should NOT fire D006
    val g = koin.get<Greeter> { parametersOf("World") }
    return if (g.name == "World") "OK" else "FAIL"
}
