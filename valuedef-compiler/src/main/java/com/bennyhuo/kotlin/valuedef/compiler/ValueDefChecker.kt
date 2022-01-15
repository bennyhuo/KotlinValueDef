package com.bennyhuo.kotlin.valuedef.compiler

import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.resolve.BindingContext
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
        val bindingContext = context.trace.bindingContext
        when  {
            declaration is KtClass && descriptor is ClassDescriptor -> {
                checkClass(declaration, descriptor, bindingContext)
            }
            declaration is KtNamedFunction && descriptor is FunctionDescriptor -> {
                checkFunction(declaration, descriptor, bindingContext)
            }
            declaration is KtProperty && descriptor is PropertyDescriptor -> {
                checkProperty(declaration, descriptor, bindingContext)
            }
            declaration is KtBinaryExpression -> {
                checkBinaryExpression(declaration, bindingContext)
            }
            declaration is KtCallExpression && descriptor is CallableDescriptor -> {

            }
        }
    }

    private fun checkClass(ktClass: KtClass, descriptor: ClassDescriptor, bindingContext: BindingContext) {
        if (ktClass.isAnnotation()) {
            val valueDefAnnotations = ktClass.annotationEntries.filter {
                it.getQualifiedName(bindingContext) in valueDefs
            }
            if (valueDefAnnotations.size > 1) {
//                holder.registerProblem(
//                    klass,
//                    "inspection.valuedef.error.type.conflicts",
//                    valueDefAnnotations.map { it.text }
//                )
            }
        }
    }

    private fun checkFunction(ktNamedFunction: KtNamedFunction, descriptor: FunctionDescriptor, bindingContext: BindingContext) {
        val returnTypeValues = descriptor.returnType?.definedConstantValueOrNull() ?: return
        val returningExpression = ktNamedFunction.returningExpression ?: return

        if (ktNamedFunction.isReturningUnsafeValueType(bindingContext)) return
        val possibleReturnValues = ktNamedFunction.possibleReturnValuesOrNull(bindingContext)
        // checkValue(holder, returningExpression, possibleReturnValues, returnTypeValues)
    }

    private fun checkProperty(ktProperty: KtProperty, descriptor: PropertyDescriptor, bindingContext: BindingContext) {
        // val x = 2
        val constantValues = descriptor.type.definedConstantValueOrNull() ?: return
        val initializer = ktProperty.initializer ?: return

        if (initializer.isUnsafeValueType(bindingContext)) return
        val rhsValue = initializer.possibleConstantValuesOrNull(bindingContext)
        // checkValue(holder, initializer, rhsValue, constantValues)
    }

    private fun checkBinaryExpression(expression: KtBinaryExpression, bindingContext: BindingContext) {
        if (expression.operationToken == KtTokens.EQ) {
            val values = expression.left.safeAs<KtNameReferenceExpression>()
                ?.definedConstantValueOrNull(bindingContext) ?: return
            val right = expression.right ?: return
            if (right.isUnsafeValueType(bindingContext)) return

            val value = right.possibleConstantValuesOrNull(bindingContext)
            // checkValue(holder, right, value, values)
        }
    }

    private fun checkCallExpression(callExpression: KtCallExpression, descriptor: CallableDescriptor) {
        // 1. find value parameters type
//        val ktNamedFunction = callExpression.referenceExpression()?.mainReference?.resolve() as? KtNamedFunction ?: return
//        val constParameters = ktNamedFunction.valueParameters.mapIndexedNotNull { index, parameter ->
//            parameter.type().definedConstantValueOrNull()?.let {
//                ConstantValueHolder(index, parameter.name, it)
//            }
//        }
//
//        if (constParameters.isEmpty()) return
//
//        // 2. iterate arguments and check their values.
//        callExpression.valueArguments.forEachIndexed { index, argument ->
//            val constantValues = if (argument.isNamed()) {
//                val name = argument.getArgumentName()?.asName?.identifier
//                constParameters.firstOrNull { it.argumentName == name }
//            } else {
//                constParameters.firstOrNull { it.argumentIndex == index }
//            }
//
//            val argumentExpression = argument.getArgumentExpression()
//            if (argumentExpression.isUnsafeValueType()) {
//                return
//            }
//
//            if (constantValues != null) {
//                val argumentValue = argument.getArgumentExpression()?.possibleConstantValuesOrNull()
//                // checkValue(holder, argument, argumentValue, constantValues.constantValue)
//            }
//        }
    }
}