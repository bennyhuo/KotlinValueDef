package com.bennyhuo.kotlin.valuedef

import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.descriptors.annotations.AnnotationArgumentVisitor
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.constants.ConstantValue
import org.jetbrains.kotlin.types.KotlinType

/**
 * Created by benny.
 */
const val STRING_TYPE_NAME = "kotlin.String"
const val INT_TYPE_NAME = "kotlin.Int"

/**
 * @Target(AnnotationTarget.CLASS)
 * annotation class StringDef(vararg val value: String)
 */
const val STRING_VALUE_DEF_NAME = "com.bennyhuo.kotlin.valuedef.StringDef"

/**
 * @Target(AnnotationTarget.CLASS)
 * annotation class IntDef(vararg val value: Int)
 */
const val INT_VALUE_DEF_NAME = "com.bennyhuo.kotlin.valuedef.IntDef"

/**
 * @Target(AnnotationTarget.CLASS)
 * annotation class IntRangeDef(val min: Int, val max: Int)
 */
const val INT_RANGE_VALUE_DEF_NAME = "com.bennyhuo.kotlin.valuedef.IntRangeDef"

const val UNSAFE_VALUE_TYPE_NAME = "com.bennyhuo.kotlin.valuedef.UnsafeDef"

val valueDefs = mapOf(
    INT_TYPE_NAME to INT_VALUE_DEF_NAME,
    INT_TYPE_NAME to INT_RANGE_VALUE_DEF_NAME,
    STRING_TYPE_NAME to STRING_VALUE_DEF_NAME
)

val valueDefGetters = mapOf(
    INT_VALUE_DEF_NAME to AnnotationDescriptor::getValue,
    INT_RANGE_VALUE_DEF_NAME to AnnotationDescriptor::getRange,
    STRING_VALUE_DEF_NAME to AnnotationDescriptor::getValue
)

fun AnnotationDescriptor.getValue(): ConstantValue<*>? {
    return allValueArguments.get(Name.identifier("value"))
}

fun AnnotationDescriptor.getRange(): ConstantValue<*>? {
    val min = allValueArguments.get(Name.identifier("min"))?.value?.safeAs<Int>() ?: return null
    val max = allValueArguments.get(Name.identifier("max"))?.value?.safeAs<Int>() ?: return null
    return IntRangeConstantValue(min .. max)
}

class IntRangeConstantValue(value: IntRange) : ConstantValue<IntRange>(value) {
    override fun <R, D> accept(visitor: AnnotationArgumentVisitor<R, D>, data: D): R = TODO()

    override fun getType(module: ModuleDescriptor): KotlinType = TODO()
}