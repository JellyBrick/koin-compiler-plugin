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
import org.koin.compiler.adapter.KotlinAdapterLoader

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
    // The annotations list type is version-split in Kotlin 2.4.0 — assignment
    // goes through the adapter matching the running compiler.
    KotlinAdapterLoader.current.setAnnotations(this, annotations + annotation)
}

/**
 * Resolved symbols + types needed to construct `@Deprecated(..., level = HIDDEN)` annotations.
 *
 * Symbol-table lookups (`referenceClass`) and `declarations.filterIsInstance<...>().firstOrNull`
 * walks are not free — `kotlin.Deprecated` has many constructors and `DeprecationLevel` enumerates
 * 4 entries. Callers that emit many hint functions in one batch amortize by calling
 * [buildDeprecatedHiddenAnnotation] once and reusing the result (the `sharedDeprecated` pattern
 * in every hint generator), so template resolution runs a handful of times per compilation.
 * `IrConstructorCall` is an `IrExpression` with no `parent` field, so reusing one instance across
 * multiple functions' annotation lists is safe (only `IrDeclaration` subclasses care about parent
 * uniqueness).
 *
 * Deliberately NOT cached in a process-global map keyed by `IrPluginContext`: the resolved
 * symbols strongly reference the context's symbol table, so a `WeakHashMap<IrPluginContext, _>`
 * whose values reach back to the key never expires — every compilation's full IR world stays
 * pinned for the daemon's lifetime (observed as suite-wide OOM in the in-process test runner),
 * and an unsynchronized global map is also unsafe under parallel daemon compilations (KTZ-4414).
 */
private class DeprecatedHiddenAnnotationTemplate(
    val deprecatedClassSymbol: IrClassSymbol,
    val deprecatedType: IrType,
    val constructorSymbol: IrConstructorSymbol,
    val deprecationLevelType: IrType,
    val hiddenEntrySymbol: IrEnumEntrySymbol,
    val messageType: IrType,
)

@OptIn(DeprecatedForRemovalCompilerApi::class)
private fun resolveTemplate(context: IrPluginContext): DeprecatedHiddenAnnotationTemplate? {
    val deprecatedClassSymbol = context.referenceClass(StandardClassIds.Annotations.Deprecated)
        ?: return null
    val constructor = deprecatedClassSymbol.owner.declarations
        .filterIsInstance<IrConstructor>()
        .firstOrNull { it.isPrimary }
        ?: return null

    val deprecationLevelClassId = ClassId(FqName("kotlin"), Name.identifier("DeprecationLevel"))
    val deprecationLevelClassSymbol = context.referenceClass(deprecationLevelClassId)
        ?: return null
    val hiddenEntry = deprecationLevelClassSymbol.owner.declarations
        .filterIsInstance<IrEnumEntry>()
        .firstOrNull { it.name.asString() == "HIDDEN" }
        ?: return null

    return DeprecatedHiddenAnnotationTemplate(
        deprecatedClassSymbol = deprecatedClassSymbol,
        deprecatedType = deprecatedClassSymbol.defaultType,
        constructorSymbol = constructor.symbol,
        deprecationLevelType = deprecationLevelClassSymbol.defaultType,
        hiddenEntrySymbol = hiddenEntry.symbol,
        messageType = context.irBuiltIns.stringType,
    )
}

/**
 * Build an `IrConstructorCall` representing `@Deprecated(message = "...", level = DeprecationLevel.HIDDEN)`.
 * Returns null if the Deprecated class cannot be resolved.
 *
 * Callers that emit many hint functions in one batch should call this once and append the same
 * `IrConstructorCall` to every function's annotation list — `IrConstructorCall` is an
 * `IrExpression` (no `parent` field) so sharing across annotation lists is safe. That per-batch
 * sharing is the load-bearing optimization; see [DeprecatedHiddenAnnotationTemplate] for why the
 * symbol resolution is intentionally not cached process-globally.
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
        // Set positional value arguments (used by codegen)
        // arg 0: message (String)
        putRegularArgument(0, messageExpr)
        // arg 1: replaceWith — leave as default (null)
        // arg 2: level (DeprecationLevel.HIDDEN)
        putRegularArgument(2, levelExpr)

        // Argument mapping (used by IR annotation processing and metadata serialization)
        argumentMapping = mapOf(
            Name.identifier("message") to messageExpr,
            Name.identifier("level") to levelExpr
        )
    }
}
