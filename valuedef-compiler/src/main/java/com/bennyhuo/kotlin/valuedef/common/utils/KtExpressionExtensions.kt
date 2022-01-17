package com.bennyhuo.kotlin.valuedef.common.utils

import com.bennyhuo.kotlin.valuedef.common.value.UNSAFE_VALUE_TYPE_NAME
import org.jetbrains.kotlin.psi.KtAnnotatedExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getType
import org.jetbrains.kotlin.resolve.constants.ConstantValue
import org.jetbrains.kotlin.resolve.constants.evaluate.ConstantExpressionEvaluator
import org.jetbrains.kotlin.types.KotlinType

/**
 * Created by benny at 2022/1/17 10:49 AM.
 */
fun KtExpression.constantValueOrNull(context: BindingContext): ConstantValue<Any?>? {
    val constant = ConstantExpressionEvaluator.getConstant(this, context) ?: return null
    return constant.toConstantValue(getType(context) ?: return null)
}

fun KtExpression.possibleConstantValuesOrNull(bindingContext: BindingContext): Any? {
    return constantValueOrNull(bindingContext)?.value ?: bindingContext.getType(this).definedConstantValueOrNull()
}

fun KtExpression.type(bindingContext: BindingContext): KotlinType? {
    return bindingContext.getType(this)
}

fun KtExpression?.isUnsafeValueType(bindingContext: BindingContext): Boolean {
    return this != null
            && this is KtAnnotatedExpression
            && annotationEntries.any { it.fqNameMatches(UNSAFE_VALUE_TYPE_NAME, bindingContext) }
}