package com.bennyhuo.kotlin.valuedef.common.error

import org.jetbrains.kotlin.com.intellij.psi.PsiElement

/**
 * Created by benny at 2022/1/16 8:34 AM.
 */
interface ErrorReporter {
    fun report0(element: PsiElement, messageKey: String)
    fun report1(element: PsiElement, messageKey: String, arg: Any?)
    fun report2(element: PsiElement, messageKey: String, arg0: Any?, arg1: Any?)
}