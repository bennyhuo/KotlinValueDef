package com.bennyhuo.kotlin.valuedef.common.utils

import com.bennyhuo.kotlin.valuedef.common.value.IntRangeConstantValue
import com.bennyhuo.kotlin.valuedef.common.value.valueDefs
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.constants.ConstantValue
import org.jetbrains.kotlin.resolve.descriptorUtil.annotationClass

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

fun AnnotationDescriptor.value(): ConstantValue<*>? {
    return allValueArguments[Name.identifier("value")]
}

fun AnnotationDescriptor.getValue(): ConstantValue<*>? {
    return allValueArguments.get(Name.identifier("value"))
}

fun AnnotationDescriptor.getRange(): ConstantValue<*>? {
    val min = allValueArguments.get(Name.identifier("min"))?.value?.safeAs<Int>() ?: return null
    val max = allValueArguments.get(Name.identifier("max"))?.value?.safeAs<Int>() ?: return null
    return IntRangeConstantValue(min .. max)
}