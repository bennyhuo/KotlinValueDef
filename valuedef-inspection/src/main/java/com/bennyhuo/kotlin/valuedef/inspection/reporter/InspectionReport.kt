package com.bennyhuo.kotlin.valuedef.inspection.reporter

import com.bennyhuo.kotlin.valuedef.common.error.ErrorReporter
import com.bennyhuo.kotlin.valuedef.inspection.ValueDefBundle
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement

/**
 * Created by benny at 2022/1/17 1:19 PM.
 */
class InspectionReport(private val problemsHolder: ProblemsHolder): ErrorReporter {

    private fun ProblemsHolder.registerProblem(element: PsiElement, messageKey: String, vararg values: Any?) {
        registerProblem(
            element,
            ValueDefBundle.message(messageKey, *values),
            ProblemHighlightType.GENERIC_ERROR_OR_WARNING
        )
    }

    override fun report0(element: PsiElement, messageKey: String) {
        problemsHolder.registerProblem(element, messageKey)
    }

    override fun report1(element: PsiElement, messageKey: String, arg: Any?) {
        problemsHolder.registerProblem(element, messageKey, arg)
    }

    override fun report2(element: PsiElement, messageKey: String, arg0: Any?, arg1: Any?) {
        problemsHolder.registerProblem(element, messageKey, arg0, arg1)
    }
}