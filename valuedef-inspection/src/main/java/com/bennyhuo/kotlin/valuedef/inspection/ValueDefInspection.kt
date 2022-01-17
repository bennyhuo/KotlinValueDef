package com.bennyhuo.kotlin.valuedef.inspection

import com.bennyhuo.kotlin.valuedef.inspection.reporter.InspectionReport
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection

/**
 * Created by benny.
 */
class ValueDefInspection : AbstractKotlinInspection() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return ValueDefInspectionVisitor(InspectionReport(holder))
    }

}