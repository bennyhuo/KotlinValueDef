package com.bennyhuo.kotlin.valuedef.compiler

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.psi.KtCallableDeclaration
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.declarationRecursiveVisitor
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
        val visitor = ValueDefCheckVisitor(context.trace, true)
        declaration.accept(visitor)
        //declaration.acceptChildren(visitor)
//        println("${this.hashCode()}-------------------")
//        println("${declaration.text}@${declaration.hashCode()} - ${declaration.javaClass.name}")
//        if (declaration is KtCallableDeclaration) {
//            declaration.typeReference?.accept(visitor)
//        }
//        if (declaration is KtNamedFunction) {
//            declaration.valueParameters.forEach {
//                it.typeReference?.accept(visitor)
//            }
//        }
    }
}