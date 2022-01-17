package com.bennyhuo.kotlin.valuedef.common.value

import com.bennyhuo.kotlin.valuedef.common.utils.getRange
import com.bennyhuo.kotlin.valuedef.common.utils.getValue
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor

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

val valueDefs = listOf(
    INT_VALUE_DEF_NAME, INT_RANGE_VALUE_DEF_NAME, STRING_VALUE_DEF_NAME
)

val valueDefTypeMap = mapOf(
    INT_TYPE_NAME to listOf(INT_VALUE_DEF_NAME, INT_RANGE_VALUE_DEF_NAME),
    STRING_TYPE_NAME to listOf(STRING_VALUE_DEF_NAME)
)

val valueDefGetters = mapOf(
    INT_VALUE_DEF_NAME to AnnotationDescriptor::getValue,
    INT_RANGE_VALUE_DEF_NAME to AnnotationDescriptor::getRange,
    STRING_VALUE_DEF_NAME to AnnotationDescriptor::getValue
)