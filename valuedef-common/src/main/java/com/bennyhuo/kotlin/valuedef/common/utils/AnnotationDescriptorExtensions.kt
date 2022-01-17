package com.bennyhuo.kotlin.valuedef.common.utils

import com.bennyhuo.kotlin.valuedef.common.value.IntRangeConstantValue
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.constants.ConstantValue

/**
 * Created by benny.
 */
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