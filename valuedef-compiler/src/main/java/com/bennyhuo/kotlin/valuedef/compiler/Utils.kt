package com.bennyhuo.kotlin.valuedef.compiler

import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtAnnotatedExpression
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtReturnExpression
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.psiUtil.lastBlockStatementOrThis
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getType
import org.jetbrains.kotlin.resolve.constants.ConstantValue
import org.jetbrains.kotlin.resolve.constants.evaluate.ConstantExpressionEvaluator
import org.jetbrains.kotlin.resolve.descriptorUtil.annotationClass
import org.jetbrains.kotlin.types.KotlinType

/**
 * Created by benny.
 */
/**
 * Computes the qualified name of this [KtAnnotationEntry].
 * Prefer to use [fqNameMatches], which checks the short name first and thus has better performance.
 */
fun KtAnnotationEntry.getQualifiedName(bindingContext: BindingContext): String? {
    return bindingContext.get(BindingContext.ANNOTATION, this)?.fqName?.asString()
}

/**
 * Determines whether this [KtAnnotationEntry] has the specified qualified name.
 * Careful: this does *not* currently take into account Kotlin type aliases (https://kotlinlang.org/docs/reference/type-aliases.html).
 *   Fortunately, type aliases are extremely uncommon for simple annotation types.
 */
fun KtAnnotationEntry.fqNameMatches(fqName: String, bindingContext: BindingContext): Boolean {
    // For inspiration, see IDELightClassGenerationSupport.KtUltraLightSupportImpl.findAnnotation in the Kotlin plugin.
    val shortName = shortName?.asString() ?: return false
    return fqName.endsWith(shortName) && fqName == getQualifiedName(bindingContext)
}

fun AnnotationDescriptor.value(): ConstantValue<*>? {
    return allValueArguments[Name.identifier("value")]
}

fun KtAnnotationEntry.findAllValueDefAnnotations(bindingContext: BindingContext): List<AnnotationDescriptor> {
    return bindingContext.get(BindingContext.ANNOTATION, this)
        ?.annotationClass?.annotations?.let { annotations ->
            valueDefs.mapNotNull { annotations.findAnnotation(FqName(it)) }
        } ?: emptyList()
}

fun KtAnnotationEntry.findFirstValueDefAnnotation(bindingContext: BindingContext): AnnotationDescriptor? {
    return bindingContext.get(BindingContext.ANNOTATION, this)
        ?.annotationClass?.annotations?.let { annotations ->
            valueDefs.asSequence().mapNotNull {
                annotations.findAnnotation(FqName(it))
            }.firstOrNull()
        }
}

fun KtNameReferenceExpression.definedConstantValueOrNull(bindingContext: BindingContext): ConstantValue<*>? {
    return bindingContext.getType(this).definedConstantValueOrNull()
}

fun KtTypeReference.declaredTypeFqName(bindingContext: BindingContext): String? {
    return bindingContext.get(BindingContext.TYPE, this)?.fqName?.asString()
}

fun KotlinType?.definedConstantValueOrNull(): ConstantValue<*>? {
    val valueDefNames = valueDefTypeMap[this?.fqName?.asString()] ?: return null
    return this?.annotations?.firstNotNullOfOrNull {
        it.annotationClass?.annotations
            ?.let { annotations ->
                val annotationDescriptor = valueDefNames.asSequence().mapNotNull {
                    annotations.findAnnotation(FqName(it))
                }.firstOrNull()
                if (annotationDescriptor == null) null
                else {
                    valueDefGetters[annotationDescriptor.fqName?.asString()]!!.invoke(annotationDescriptor)
                }
            }
    }
}

fun KtExpression.constantValueOrNull(context: BindingContext): ConstantValue<Any?>? {
    val constant = ConstantExpressionEvaluator.getConstant(this, context) ?: return null
    return constant.toConstantValue(getType(context) ?: return null)
}

fun KtExpression.possibleConstantValuesOrNull(bindingContext: BindingContext): Any? {
    return constantValueOrNull(bindingContext)?.value ?: bindingContext.getType(this).definedConstantValueOrNull()
}

fun KtExpression.type(bindingContext: BindingContext): KotlinType? {
    return bindingContext.getType(this)
}

fun KtExpression?.isUnsafeValueType(bindingContext: BindingContext): Boolean {
    return this != null
            && this is KtAnnotatedExpression
            && annotationEntries.any { it.fqNameMatches(UNSAFE_VALUE_TYPE_NAME, bindingContext) }
}

fun KtProperty.type(bindingContext: BindingContext): KotlinType? {
    return bindingContext.get(BindingContext.DECLARATION_TO_DESCRIPTOR, this)
        .safeAs<CallableDescriptor>()?.returnType
}

fun KtNamedFunction.returnType(bindingContext: BindingContext): KotlinType? {
    return bindingContext.getType(this)
}

fun KtNamedFunction.isReturningUnsafeValueType(bindingContext: BindingContext): Boolean {
    return returningExpression?.isUnsafeValueType(bindingContext) ?: false
}

val KtNamedFunction.returningExpression: KtExpression?
    get() {
        return if (hasBlockBody()) {
            bodyBlockExpression?.lastBlockStatementOrThis()
                ?.safeAs<KtReturnExpression>()?.returnedExpression
        } else if (hasInitializer()) {
            initializer
        } else {
            null
        }
    }

fun KtNamedFunction.possibleReturnValuesOrNull(bindingContext: BindingContext): Any? {
    return returningExpression?.possibleConstantValuesOrNull(bindingContext)
}

inline fun <reified T> Any?.safeAs() = this as? T
inline fun <reified T> Any.caseAs() = this as T