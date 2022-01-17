package com.bennyhuo.kotlin.valuedef.common.utils

import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtReturnExpression
import org.jetbrains.kotlin.psi.psiUtil.lastBlockStatementOrThis

/**
 * Created by benny at 2022/1/17 10:37 AM.
 */
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
