package com.bennyhuo.kotlin.valuedef.common.utils

import com.bennyhuo.kotlin.valuedef.common.value.valueDefGetters
import com.bennyhuo.kotlin.valuedef.common.value.valueDefTypeMap
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.constants.ConstantValue
import org.jetbrains.kotlin.resolve.descriptorUtil.annotationClass
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameOrNull
import org.jetbrains.kotlin.types.AbbreviatedType
import org.jetbrains.kotlin.types.KotlinType

/**
 * Created by benny at 2022/1/15 8:44 PM.
 */
val KotlinType.fqName: FqName?
    get() = when (this) {
        is AbbreviatedType -> abbreviation.fqName
        else -> constructor.declarationDescriptor?.fqNameOrNull()
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