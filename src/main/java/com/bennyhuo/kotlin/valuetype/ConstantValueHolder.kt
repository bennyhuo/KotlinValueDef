package com.bennyhuo.kotlin.valuetype

import org.jetbrains.kotlin.resolve.constants.ArrayValue
import org.jetbrains.kotlin.resolve.constants.ConstantValue

class ConstantValueHolder(
    val argumentIndex: Int,
    val argumentName: String?,
    val constantValue: ConstantValue<*>
) {
     fun values(): List<Any?> {
         return constantValue.values()
     }
}

fun ConstantValue<*>.values() = when (val constantValue = this) {
    is ArrayValue -> {
        constantValue.value.map { it.value }
    }
    else -> {
        listOf(constantValue.value)
    }
}

operator fun ConstantValue<*>.contains(value: Any?): Boolean {
    if (value == null) return false
    if (value is ConstantValue<*>) {
        return if (value is ArrayValue) {
            values().containsAll(value.values())
        } else {
            value.value in values()
        }
    }
    return value in values()
}

