package com.bennyhuo.kotlin.valuedef

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement

/**
 * Created by benny.
 */
fun ProblemsHolder.registerProblem(element: PsiElement, messageKey: String, vararg values: Any?) {
    registerProblem(
        element,
        ValueDefBundle.message(messageKey, *values),
        ProblemHighlightType.GENERIC_ERROR_OR_WARNING
    )
}