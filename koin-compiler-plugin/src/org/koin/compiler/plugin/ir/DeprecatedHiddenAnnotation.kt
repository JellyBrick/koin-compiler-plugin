package org.koin.compiler.plugin.ir

import org.jetbrains.kotlin.DeprecatedForRemovalCompilerApi
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrEnumEntry
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.impl.IrAnnotationImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetEnumValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.fromSymbolOwner
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol
import org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.StandardClassIds

/**
 * Adds `@Deprecated("Koin compiler plugin internal hint function", level = DeprecationLevel.HIDDEN)`
 * annotation to an IR function.
 *
 * This prevents the function from being exported to ObjC headers on Kotlin/Native,
 * which would otherwise crash with "An operation is not implemented" in findSourceFile.
 *
 * Mirrors the FIR-phase annotation added by [org.koin.compiler.plugin.fir.KoinModuleFirGenerator.markAsDeprecatedHidden].
 */
fun IrSimpleFunction.addDeprecatedHiddenAnnotation(context: IrPluginContext) {
    val annotation = buildDeprecatedHiddenAnnotation(context) ?: return
    annotations = annotations + annotation
}

/**
 * Resolved symbols + types needed to construct `@Deprecated(..., level = HIDDEN)` annotations.
 *
 * Symbol-table lookups (`referenceClass`) and `declarations.filterIsInstance<...>().firstOrNull`
 * walks are not free — `kotlin.Deprecated` has many constructors and `DeprecationLevel` enumerates
 * 4 entries. For a module with 70 `@Factory` we emit 70+ hint functions, so the un-cached form
 * paid that resolution 70+ times per module. We cache the symbol lookups in this template; the
 * actual `IrConstructorCall` is still cheap to allocate per call site, but callers that emit
 * many hint functions in one batch should call [buildDeprecatedHiddenAnnotation] once and
 * reuse the result via `function.annotations + sharedAnnotation`. `IrConstructorCall` is an
 * `IrExpression` with no `parent` field, so reusing one instance across multiple functions'
 * annotation lists is safe (only `IrDeclaration` subclasses care about parent uniqueness).
 *
 * Keyed by `IrPluginContext` identity so each IR pass / plugin instantiation gets its own cache.
 * Concurrent compile invocations from different daemons run with separate contexts so there's no
 * cross-compilation pollution.
 */
private class DeprecatedHiddenAnnotationTemplate(
    val deprecatedClassSymbol: IrClassSymbol,
    val deprecatedType: IrType,
    val constructorSymbol: IrConstructorSymbol,
    val deprecationLevelType: IrType,
    val hiddenEntrySymbol: IrEnumEntrySymbol,
    val messageType: IrType,
)

private val templateCache = java.util.WeakHashMap<IrPluginContext, DeprecatedHiddenAnnotationTemplate?>()

@OptIn(DeprecatedForRemovalCompilerApi::class)
private fun resolveTemplate(context: IrPluginContext): DeprecatedHiddenAnnotationTemplate? {
    // WeakHashMap.computeIfAbsent doesn't tolerate null returns, so do the get/put dance manually.
    if (templateCache.containsKey(context)) return templateCache[context]

    val deprecatedClassSymbol = context.referenceClass(StandardClassIds.Annotations.Deprecated)
    if (deprecatedClassSymbol == null) {
        templateCache[context] = null
        return null
    }
    val constructor = deprecatedClassSymbol.owner.declarations
        .filterIsInstance<IrConstructor>()
        .firstOrNull { it.isPrimary }
    if (constructor == null) {
        templateCache[context] = null
        return null
    }

    val deprecationLevelClassId = ClassId(FqName("kotlin"), Name.identifier("DeprecationLevel"))
    val deprecationLevelClassSymbol = context.referenceClass(deprecationLevelClassId)
    if (deprecationLevelClassSymbol == null) {
        templateCache[context] = null
        return null
    }
    val hiddenEntry = deprecationLevelClassSymbol.owner.declarations
        .filterIsInstance<IrEnumEntry>()
        .firstOrNull { it.name.asString() == "HIDDEN" }
    if (hiddenEntry == null) {
        templateCache[context] = null
        return null
    }

    val template = DeprecatedHiddenAnnotationTemplate(
        deprecatedClassSymbol = deprecatedClassSymbol,
        deprecatedType = deprecatedClassSymbol.defaultType,
        constructorSymbol = constructor.symbol,
        deprecationLevelType = deprecationLevelClassSymbol.defaultType,
        hiddenEntrySymbol = hiddenEntry.symbol,
        messageType = context.irBuiltIns.stringType,
    )
    templateCache[context] = template
    return template
}

/**
 * Build an `IrConstructorCall` representing `@Deprecated(message = "...", level = DeprecationLevel.HIDDEN)`.
 * Returns null if the Deprecated class cannot be resolved.
 *
 * Symbol-table lookups go through a per-`IrPluginContext` cache (see [resolveTemplate]), so the
 * first call per compile pays the resolution cost and every subsequent call reuses it. Callers
 * that emit many hint functions in one batch should call this once and append the same
 * `IrConstructorCall` to every function's annotation list — `IrConstructorCall` is an
 * `IrExpression` (no `parent` field) so sharing across annotation lists is safe.
 */
@OptIn(DeprecatedForRemovalCompilerApi::class)
internal fun buildDeprecatedHiddenAnnotation(context: IrPluginContext): IrConstructorCall? {
    val tpl = resolveTemplate(context) ?: return null

    val messageExpr = IrConstImpl.string(
        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
        tpl.messageType,
        "Koin compiler plugin internal hint function"
    )
    val levelExpr = IrGetEnumValueImpl(
        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
        tpl.deprecationLevelType,
        tpl.hiddenEntrySymbol
    )

    return IrAnnotationImpl.fromSymbolOwner(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        tpl.deprecatedType,
        tpl.constructorSymbol
    ).apply {
        // Positional value arguments (used by codegen)
        // arg 0: message (String); arg 1: replaceWith (defaulted); arg 2: level (DeprecationLevel.HIDDEN)
        putValueArgument(0, messageExpr)
        putValueArgument(2, levelExpr)

        // Argument mapping (used by IR annotation processing and metadata serialization)
        argumentMapping = mapOf(
            Name.identifier("message") to messageExpr,
            Name.identifier("level") to levelExpr
        )
    }
}
