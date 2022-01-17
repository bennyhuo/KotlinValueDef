package com.bennyhuo.kotlin.valuedef.common.utils

import com.bennyhuo.kotlin.valuedef.common.value.UNSAFE_VALUE_TYPE_NAME
import com.bennyhuo.kotlin.valuedef.common.value.valueDefs
import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtAnnotatedExpression
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getType
import org.jetbrains.kotlin.resolve.constants.ConstantValue
import org.jetbrains.kotlin.resolve.constants.evaluate.ConstantExpressionEvaluator
import org.jetbrains.kotlin.resolve.descriptorUtil.annotationClass
import org.jetbrains.kotlin.types.KotlinType

/**
 * Created by benny at 2022/1/17 1:09 PM.
 */
interface BindingContextScope {

    val bindingContext: BindingContext

    //region KtAnnotationEntry
    /**
     * Computes the qualified name of this [KtAnnotationEntry].
     * Prefer to use [fqNameMatches], which checks the short name first and thus has better performance.
     */
    fun KtAnnotationEntry.getQualifiedName(): String? {
        return bindingContext.get(BindingContext.ANNOTATION, this)?.fqName?.asString()
    }

    /**
     * Determines whether this [KtAnnotationEntry] has the specified qualified name.
     * Careful: this does *not* currently take into account Kotlin type aliases (https://kotlinlang.org/docs/reference/type-aliases.html).
     *   Fortunately, type aliases are extremely uncommon for simple annotation types.
     */
    fun KtAnnotationEntry.fqNameMatches(fqName: String): Boolean {
        // For inspiration, see IDELightClassGenerationSupport.KtUltraLightSupportImpl.findAnnotation in the Kotlin plugin.
        val shortName = shortName?.asString() ?: return false
        return fqName.endsWith(shortName) && fqName == getQualifiedName()
    }

    fun KtAnnotationEntry.findAllValueDefAnnotations(): List<AnnotationDescriptor> {
        return bindingContext.get(BindingContext.ANNOTATION, this)
            ?.annotationClass?.annotations?.let { annotations ->
                valueDefs.mapNotNull { annotations.findAnnotation(FqName(it)) }
            } ?: emptyList()
    }

    fun KtAnnotationEntry.findFirstValueDefAnnotation(): AnnotationDescriptor? {
        return bindingContext.get(BindingContext.ANNOTATION, this)
            ?.annotationClass?.annotations?.let { annotations ->
                valueDefs.asSequence().mapNotNull {
                    annotations.findAnnotation(FqName(it))
                }.firstOrNull()
            }
    }
    //endregion

    //region KtExpression
    fun KtExpression.constantValueOrNull(): ConstantValue<Any?>? {
        val constant = ConstantExpressionEvaluator.getConstant(this, bindingContext) ?: return null
        return constant.toConstantValue(getType(bindingContext) ?: return null)
    }

    fun KtExpression.possibleConstantValuesOrNull(): Any? {
        return constantValueOrNull()?.value ?: bindingContext.getType(this).definedConstantValueOrNull()
    }

    fun KtExpression.type(): KotlinType? {
        return bindingContext.getType(this)
    }

    fun KtExpression?.isUnsafeValueType(): Boolean {
        return this != null
                && this is KtAnnotatedExpression
                && annotationEntries.any { it.fqNameMatches(UNSAFE_VALUE_TYPE_NAME) }
    }
    //endregion

    //region KtNamedFunction
    fun KtNamedFunction.returnType(): KotlinType? {
        return bindingContext.getType(this)
    }

    fun KtNamedFunction.isReturningUnsafeValueType(): Boolean {
        return returningExpression?.isUnsafeValueType() ?: false
    }

    fun KtNamedFunction.possibleReturnValuesOrNull(): Any? {
        return returningExpression?.possibleConstantValuesOrNull()
    }
    //endregion

    //region KtProperty
    fun KtProperty.type(): KotlinType? {
        return bindingContext.get(BindingContext.DECLARATION_TO_DESCRIPTOR, this)
            .safeAs<CallableDescriptor>()?.returnType
    }
    //endregion

    //region KtNameReferenceExpression
    fun KtNameReferenceExpression.definedConstantValueOrNull(): ConstantValue<*>? {
        return bindingContext.getType(this).definedConstantValueOrNull()
    }
    //endregion

    //region KtTypeReference
    fun KtTypeReference.declaredTypeFqName(): String? {
        return bindingContext.get(BindingContext.TYPE, this)?.fqName?.asString()
    }
    //endregion
}