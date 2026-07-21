package org.jetbrains.kotlin.compiler.plugin.template.services

import org.jetbrains.kotlin.test.directives.model.SimpleDirectivesContainer

/**
 * Custom testData directives for Koin plugin behavior flags.
 *
 * Registered by [ExtensionRegistrarConfigurator] so testData files can toggle
 * per-compilation plugin options that Gradle users set via the `koinCompiler {}` block.
 */
object KoinTestDirectives : SimpleDirectivesContainer() {
    /**
     * Run the compilation with `publishHints = false` (the `koinCompiler { publishHints = false }`
     * Gradle setting). No hint callables are generated in FIR, so hint-driven discovery
     * (configuration siblings, provider-hint oracle, DSL and demand hints) sees nothing from
     * this compilation. Reproduces consumer projects that opt out of hint publication.
     */
    val PUBLISH_HINTS_OFF by directive(
        description = "Compile with the Koin plugin option publishHints=false"
    )
}
