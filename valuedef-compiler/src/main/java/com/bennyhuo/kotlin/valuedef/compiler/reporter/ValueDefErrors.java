package com.bennyhuo.kotlin.valuedef.compiler.reporter;

import com.intellij.psi.PsiElement;

import org.jetbrains.kotlin.diagnostics.DiagnosticFactory1;
import org.jetbrains.kotlin.diagnostics.DiagnosticFactory2;
import org.jetbrains.kotlin.diagnostics.Errors;

import static org.jetbrains.kotlin.diagnostics.Severity.ERROR;

/**
 * Created by benny at 2022/1/16 10:24 AM.
 */
public interface ValueDefErrors {
    DiagnosticFactory1<PsiElement, String> VALUE_MISMATCH = DiagnosticFactory1.create(ERROR);
    DiagnosticFactory1<PsiElement, String> DEF_CONFLICTS = DiagnosticFactory1.create(ERROR);
    DiagnosticFactory1<PsiElement, String> TYPE_CONFLICTS = DiagnosticFactory1.create(ERROR);
    DiagnosticFactory2<PsiElement, String, String> TYPE_MISMATCH = DiagnosticFactory2.create(ERROR);

    @SuppressWarnings("UnusedDeclaration")
    Object _initializer = new Object() {
        {
            Errors.Initializer.initializeFactoryNamesAndDefaultErrorMessages(ValueDefErrors.class, ValueDefErrorMessages.INSTANCE);
        }
    };
}
