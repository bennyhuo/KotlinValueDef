package com.bennyhuo.kotlin.valuedef.inspection

import com.bennyhuo.kotlin.valuedef.common.core.ValueDefCommonVisitor
import com.bennyhuo.kotlin.valuedef.common.error.ErrorReporter
import com.bennyhuo.kotlin.valuedef.inspection.utils.bindingContext
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.resolve.BindingContext

/**
 * Created by benny at 2022/1/17 1:00 PM.
 */
class ValueDefInspectionVisitor(reporter: ErrorReporter) : ValueDefCommonVisitor(reporter) {

    private var _bindingContext: BindingContext? = null

    override val bindingContext: BindingContext
        get() = _bindingContext!!

    override fun visitKtElement(element: KtElement) {
        _bindingContext = element.bindingContext
        super.visitKtElement(element)
    }
}