package com.bennyhuo.kotlin.valuedef.common.utils

import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.constants.ConstantValue

/**
 * Created by benny at 2022/1/17 10:50 AM.
 */
fun KtNameReferenceExpression.definedConstantValueOrNull(bindingContext: BindingContext): ConstantValue<*>? {
    return bindingContext.getType(this).definedConstantValueOrNull()
}

fun KtTypeReference.declaredTypeFqName(bindingContext: BindingContext): String? {
    return bindingContext.get(BindingContext.TYPE, this)?.fqName?.asString()
}