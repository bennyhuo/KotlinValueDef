package com.bennyhuo.kotlin.valuedef.common.utils

import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.types.KotlinType

/**
 * Created by benny at 2022/1/17 10:37 AM.
 */
fun KtProperty.type(bindingContext: BindingContext): KotlinType? {
    return bindingContext.get(BindingContext.DECLARATION_TO_DESCRIPTOR, this)
        .safeAs<CallableDescriptor>()?.returnType
}