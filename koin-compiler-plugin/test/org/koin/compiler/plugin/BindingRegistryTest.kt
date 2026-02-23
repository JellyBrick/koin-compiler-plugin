package org.koin.compiler.plugin

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.compiler.plugin.ir.*
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit tests for compile-time safety validation logic.
 *
 * Tests the Requirement.requiresValidation() rules that determine
 * which parameters need a matching provider.
 */
class BindingRegistryTest {

    @BeforeEach
    fun setUp() {
        // Ensure skipDefaultValues is enabled (default)
        // This is set in KoinPluginLogger, which is a singleton
    }

    // ================================================================================
    // Requirement.requiresValidation() tests
    // ================================================================================

    @Test
    fun `regular non-null parameter requires validation`() {
        val req = makeRequirement()
        assertTrue(req.requiresValidation())
    }

    @Test
    fun `nullable parameter does not require validation`() {
        val req = makeRequirement(isNullable = true)
        assertFalse(req.requiresValidation())
    }

    @Test
    fun `injected param does not require validation`() {
        val req = makeRequirement(isInjectedParam = true)
        assertFalse(req.requiresValidation())
    }

    @Test
    fun `list parameter does not require validation`() {
        val req = makeRequirement(isList = true)
        assertFalse(req.requiresValidation())
    }

    @Test
    fun `property parameter does not require validation`() {
        val req = makeRequirement(isProperty = true, propertyKey = "my.key")
        assertFalse(req.requiresValidation())
    }

    @Test
    fun `parameter with default value does not require validation when skipDefaultValues enabled`() {
        // KoinPluginLogger.skipDefaultValuesEnabled defaults to true
        val req = makeRequirement(hasDefault = true)
        assertFalse(req.requiresValidation())
    }

    @Test
    fun `parameter with default value AND qualifier still requires validation`() {
        val req = makeRequirement(
            hasDefault = true,
            qualifier = QualifierValue.StringQualifier("named")
        )
        // Even with skipDefaultValues, a qualified param must be validated
        assertTrue(req.requiresValidation())
    }

    @Test
    fun `lazy parameter requires validation`() {
        // Lazy<T> still needs T to be provided
        val req = makeRequirement(isLazy = true)
        assertTrue(req.requiresValidation())
    }

    // ================================================================================
    // TypeKey tests
    // ================================================================================

    @Test
    fun `TypeKey render with fqName`() {
        val key = TypeKey(
            classId = ClassId.topLevel(FqName("com.example.MyClass")),
            fqName = FqName("com.example.MyClass")
        )
        assertEquals("com.example.MyClass", key.render())
    }

    @Test
    fun `TypeKey render with only classId`() {
        val key = TypeKey(
            classId = ClassId.topLevel(FqName("com.example.MyClass")),
            fqName = null
        )
        assertEquals("com.example.MyClass", key.render())
    }

    @Test
    fun `TypeKey render with nothing`() {
        val key = TypeKey(classId = null, fqName = null)
        assertEquals("<unknown>", key.render())
    }

    // ================================================================================
    // Helpers
    // ================================================================================

    private fun makeRequirement(
        typeFqName: String = "com.example.Dependency",
        paramName: String = "dep",
        isNullable: Boolean = false,
        hasDefault: Boolean = false,
        isInjectedParam: Boolean = false,
        isLazy: Boolean = false,
        isList: Boolean = false,
        isProperty: Boolean = false,
        propertyKey: String? = null,
        qualifier: QualifierValue? = null
    ): Requirement {
        return Requirement(
            typeKey = TypeKey(
                classId = ClassId.topLevel(FqName(typeFqName)),
                fqName = FqName(typeFqName)
            ),
            paramName = paramName,
            isNullable = isNullable,
            hasDefault = hasDefault,
            isInjectedParam = isInjectedParam,
            isLazy = isLazy,
            isList = isList,
            isProperty = isProperty,
            propertyKey = propertyKey,
            qualifier = qualifier
        )
    }
}
