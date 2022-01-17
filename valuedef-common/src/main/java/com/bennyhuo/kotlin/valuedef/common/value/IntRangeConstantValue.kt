package com.bennyhuo.kotlin.valuedef.common.value

import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.descriptors.annotations.AnnotationArgumentVisitor
import org.jetbrains.kotlin.resolve.constants.ConstantValue
import org.jetbrains.kotlin.types.KotlinType

class IntRangeConstantValue(value: IntRange) : ConstantValue<IntRange>(value) {
    override fun <R, D> accept(visitor: AnnotationArgumentVisitor<R, D>, data: D): R = TODO()

    override fun getType(module: ModuleDescriptor): KotlinType = TODO()
}