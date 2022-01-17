package com.bennyhuo.kotlin.valuedef.common.utils

import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtReturnExpression
import org.jetbrains.kotlin.psi.psiUtil.lastBlockStatementOrThis
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.types.KotlinType

/**
 * Created by benny at 2022/1/17 10:37 AM.
 */
fun KtNamedFunction.returnType(bindingContext: BindingContext): KotlinType? {
    return bindingContext.getType(this)
}

fun KtNamedFunction.isReturningUnsafeValueType(bindingContext: BindingContext): Boolean {
    return returningExpression?.isUnsafeValueType(bindingContext) ?: false
}

val KtNamedFunction.returningExpression: KtExpression?
    get() {
        return if (hasBlockBody()) {
            bodyBlockExpression?.lastBlockStatementOrThis()
                ?.safeAs<KtReturnExpression>()?.returnedExpression
        } else if (hasInitializer()) {
            initializer
        } else {
            null
        }
    }

fun KtNamedFunction.possibleReturnValuesOrNull(bindingContext: BindingContext): Any? {
    return returningExpression?.possibleConstantValuesOrNull(bindingContext)
}