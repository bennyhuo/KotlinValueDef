package com.bennyhuo.kotlin.valuedef.compiler

import com.bennyhuo.kotlin.valuedef.common.core.ValueDefCommonVisitor
import com.bennyhuo.kotlin.valuedef.common.error.ErrorReporter
import org.jetbrains.kotlin.resolve.BindingContext

/**
 * Created by benny at 2022/1/17 1:20 PM.
 */
class ValueDefCompilerVisitor(
    override val bindingContext: BindingContext,
    reporter: ErrorReporter
) : ValueDefCommonVisitor(reporter, true)