package com.bennyhuo.kotlin.valuedef.compiler

import com.bennyhuo.kotlin.valuedef.compiler.reporter.CompilerCheckReporter
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.resolve.checkers.DeclarationChecker
import org.jetbrains.kotlin.resolve.checkers.DeclarationCheckerContext

/**
 * Created by benny at 2022/1/14 3:49 PM.
 */
class ValueDefChecker : DeclarationChecker {

    override fun check(
        declaration: KtDeclaration,
        descriptor: DeclarationDescriptor,
        context: DeclarationCheckerContext
    ) {
        val visitor = ValueDefCompilerVisitor(
            context.trace.bindingContext,
            CompilerCheckReporter(context.trace))
        declaration.accept(visitor)
    }
}