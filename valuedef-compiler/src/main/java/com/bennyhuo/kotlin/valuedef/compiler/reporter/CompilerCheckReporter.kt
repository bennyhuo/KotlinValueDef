package com.bennyhuo.kotlin.valuedef.compiler.reporter

import com.bennyhuo.kotlin.valuedef.common.error.ErrorKeys
import com.bennyhuo.kotlin.valuedef.common.error.ErrorReporter
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.diagnostics.DiagnosticFactory0
import org.jetbrains.kotlin.diagnostics.DiagnosticFactory1
import org.jetbrains.kotlin.diagnostics.DiagnosticFactory2
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.utils.addToStdlib.cast

/**
 * Created by benny at 2022/1/16 8:37 AM.
 */
class CompilerCheckReporter(private val bindingTrace: BindingTrace): ErrorReporter {

    private val errorMap = mapOf(
        ErrorKeys.KEY_VALUE_MISMATCH to ValueDefErrors.VALUE_MISMATCH,
        ErrorKeys.KEY_DEF_CONFLICTS to ValueDefErrors.DEF_CONFLICTS,
        ErrorKeys.KEY_TYPE_CONFLICTS to ValueDefErrors.TYPE_CONFLICTS,
        ErrorKeys.KEY_TYPE_MISMATCH to ValueDefErrors.TYPE_MISMATCH
    )

    override fun report0(element: PsiElement, messageKey: String) {
        bindingTrace.report(errorMap[messageKey].cast<DiagnosticFactory0<PsiElement>>().on(element))
    }

    override fun report1(element: PsiElement, messageKey: String, arg: Any?) {
        bindingTrace.report(errorMap[messageKey].cast<DiagnosticFactory1<PsiElement, String>>().on(element, arg.toString()))
    }

    override fun report2(element: PsiElement, messageKey: String, arg0: Any?, arg1: Any?) {
        bindingTrace.report(errorMap[messageKey].cast<DiagnosticFactory2<PsiElement, String, String>>().on(element, arg0.toString(), arg1.toString()))
    }

}