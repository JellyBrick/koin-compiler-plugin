package org.koin.compiler.plugin.ir

import org.koin.compiler.plugin.KoinPluginLogger

/**
 * Computes a Gradle-module-unique prefix for synthetic hint file names so that two modules
 * emitting hints for the same target type don't collide at the dex merge phase.
 *
 * The prefix is derived from `koin.moduleId` (passed by the Gradle plugin as `project.path`,
 * e.g. `:featureA:ui`) composed with the FIR module-data name (which distinguishes KMP
 * targets within the same Gradle module, e.g. `<project_jvmMain>` vs `<project_iosMain>`).
 *
 * When `koin.moduleId` is absent (bare CLI builds, tests without the Gradle plugin), only
 * the FIR module-data name is used — preserving prior behavior. In that case cross-module
 * disambiguation degrades to whatever uniqueness the leaf compilation unit name provides.
 */
internal object HintFilePrefix {

    private val NON_IDENTIFIER_CHAR = Regex("[^A-Za-z0-9]")

    /**
     * Build the prefix segment to prepend to a hint file name.
     * The result is sanitized to be Kotlin-identifier-safe and ends with a `__` separator
     * so callers can concatenate directly: `"${prefix}${rest}"`.
     *
     * Returns an empty string when neither moduleId nor firModuleName is usable; callers
     * then emit the unprefixed file name (legacy behavior).
     */
    fun of(firModuleName: String?): String {
        val moduleId = KoinPluginLogger.moduleId
        val parts = listOfNotNull(
            moduleId?.let { sanitize(it) }?.takeIf { it.isNotEmpty() },
            firModuleName?.let { sanitize(it) }?.takeIf { it.isNotEmpty() },
        )
        if (parts.isEmpty()) return ""
        return parts.joinToString(separator = "_") + "__"
    }

    private fun sanitize(raw: String): String =
        raw.removePrefix("<").removeSuffix(">")
            .replace(NON_IDENTIFIER_CHAR, "_")
            .trim('_')
}
