package com.bennyhuo.kotlin.valuetype

import org.jetbrains.kotlin.name.FqName

/**
 * Created by benny.
 */
class ValueTypeDefinition(
    val annotationName: String,
    val typeName: String,
) {
    
}

const val STRING_TYPE_NAME = "kotlin.String"
const val INT_TYPE_NAME = "kotlin.Int"

const val STRING_VALUE_TYPE_NAME = "com.bennyhuo.kotlin.valuetype.StringValueType"
const val INT_VALUE_TYPE_NAME = "com.bennyhuo.kotlin.valuetype.IntValueType"

val stringValueType = ValueTypeDefinition(
    STRING_VALUE_TYPE_NAME,
    "kotlin.String"
)

val intValueType = ValueTypeDefinition(
    INT_VALUE_TYPE_NAME,
    "kotlin.Int"
)

val valueTypes = mapOf(
    INT_TYPE_NAME to INT_VALUE_TYPE_NAME,
    STRING_TYPE_NAME to STRING_VALUE_TYPE_NAME
)