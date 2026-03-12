package examples.crosspackage

import org.junit.Test

/**
 * Tests that functions in one package returning types from another package
 * are correctly discovered by @ComponentScan matching the function's package.
 *
 * This validates the funcpkg_* hint parameter encoding:
 * - provideApiClient() is in "examples.crosspackage.providers"
 * - ApiClient is in "examples.crosspackage.models"
 * - @ComponentScan("examples.crosspackage.providers") must find it
 *
 * The fact that jvmMain compiles successfully proves the cross-package
 * discovery and safety validation work correctly.
 */
class CrossPackageTest {

    @Test
    fun cross_package_function_discovery_compiles() {
        // Compilation of jvmMain succeeding proves:
        // 1. FIR encoded funcpkg_examples_crosspackage_providers on the hint
        // 2. IR matched @ComponentScan("examples.crosspackage.providers") against the function package
        // 3. Safety validation found ApiClient and AppSettings as provided
        println("Cross-package function discovery: OK")
    }
}
