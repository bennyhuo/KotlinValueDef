package com.bennyhuo.kotlin.valuedef.common.core

import com.bennyhuo.kotlin.valuedef.common.error.ErrorKeys
import com.bennyhuo.kotlin.valuedef.common.error.ErrorReporter
import com.bennyhuo.kotlin.valuedef.common.utils.BindingContextScope
import com.bennyhuo.kotlin.valuedef.common.utils.definedConstantValueOrNull
import com.bennyhuo.kotlin.valuedef.common.utils.returningExpression
import com.bennyhuo.kotlin.valuedef.common.utils.safeAs
import com.bennyhuo.kotlin.valuedef.common.value.ConstantValueHolder
import com.bennyhuo.kotlin.valuedef.common.value.contains
import com.bennyhuo.kotlin.valuedef.common.value.valueDefTypeMap
import com.bennyhuo.kotlin.valuedef.common.value.valueDefs
import com.bennyhuo.kotlin.valuedef.common.value.values
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.psi.psiUtil.referenceExpression
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.constants.ConstantValue

/**
 * Created by benny at 2022/1/16 9:28 AM.
 */
abstract class ValueDefCommonVisitor(
    private val reporter: ErrorReporter,
    private val isRecursive: Boolean = false
) : KtVisitorVoid(), BindingContextScope {

    override fun visitElement(element: PsiElement) {
        super.visitElement(element)
        if (isRecursive) {
            element.acceptChildren(this)
        }
    }

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (klass.isAnnotation()) {
            val valueDefAnnotations = klass.annotationEntries.filter {
                it.getQualifiedName() in valueDefs
            }
            if (valueDefAnnotations.size > 1) {
                reporter.report1(
                    klass,
                    ErrorKeys.KEY_TYPE_CONFLICTS,
                    valueDefAnnotations.map { it.text }
                )
            }
        }
    }

    override fun visitTypeReference(typeReference: KtTypeReference) {
        super.visitTypeReference(typeReference)

        val declaredTypeFqName = typeReference.declaredTypeFqName() ?: return

        val valueTypeAnnotations = typeReference.annotationEntries.map {
            it to it.findFirstValueDefAnnotation()
        }.filter {
            it.second != null
        }

        if (valueTypeAnnotations.size > 1) {
            reporter.report1(
                typeReference,
                ErrorKeys.KEY_DEF_CONFLICTS,
                valueTypeAnnotations.map { it.first.text }
            )
            return
        }

        if (valueTypeAnnotations.size == 1) {
            val valueTypeAnnotation = valueTypeAnnotations.first().second!!
            val valueTypeAnnotationFqName = valueTypeAnnotation.fqName?.asString()
            if (valueTypeAnnotationFqName !in valueDefTypeMap[declaredTypeFqName]!!) {
                reporter.report2(
                    typeReference,
                    ErrorKeys.KEY_TYPE_MISMATCH,
                    valueTypeAnnotations.first().first.text,
                    declaredTypeFqName
                )
            }
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        val returnTypeValues = function.type().definedConstantValueOrNull() ?: return
        val returningExpression = function.returningExpression ?: return

        if (function.isReturningUnsafeValueType()) return
        val possibleReturnValues = function.possibleReturnValuesOrNull()
        checkValue(returningExpression, possibleReturnValues, returnTypeValues)
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
        // val x = 2
        val constantValues = property.type().definedConstantValueOrNull() ?: return
        val initializer = property.initializer ?: return

        if (initializer.isUnsafeValueType()) return
        val rhsValue = initializer.possibleConstantValuesOrNull()
        checkValue(initializer, rhsValue, constantValues)
    }

    override fun visitBinaryExpression(expression: KtBinaryExpression) {
        super.visitBinaryExpression(expression)
        if (expression.operationToken == KtTokens.EQ) {
            val values = expression.left.safeAs<KtNameReferenceExpression>()
                ?.definedConstantValueOrNull() ?: return
            val right = expression.right ?: return
            if (right.isUnsafeValueType()) return

            val value = right.possibleConstantValuesOrNull()
            checkValue(right, value, values)
        }
    }

    override fun visitCallExpression(callExpression: KtCallExpression) {
        super.visitCallExpression(callExpression)

        // 1. find value parameters type
        val functionDescriptor = bindingContext.get(
            BindingContext.REFERENCE_TARGET,
            callExpression.referenceExpression()
        ).safeAs<FunctionDescriptor>() ?: return

        val constParameters = functionDescriptor.valueParameters.mapIndexedNotNull { index, parameter ->
            parameter.type.definedConstantValueOrNull()?.let {
                ConstantValueHolder(index, parameter.name.asString(), it)
            }
        }

        if (constParameters.isEmpty()) return

        // 2. iterate arguments and check their values.
        callExpression.valueArguments.forEachIndexed { index, argument ->
            val constantValues = if (argument.isNamed()) {
                val name = argument.getArgumentName()?.asName?.identifier
                constParameters.firstOrNull { it.argumentName == name }
            } else {
                constParameters.firstOrNull { it.argumentIndex == index }
            }

            val argumentExpression = argument.getArgumentExpression()
            if (argumentExpression.isUnsafeValueType()) {
                return
            }

            if (constantValues != null) {
                val argumentValue = argument.getArgumentExpression()?.possibleConstantValuesOrNull()
                checkValue(argument, argumentValue, constantValues.constantValue)
            }
        }
    }

    private fun checkValue(
        element: PsiElement,
        expressionValue: Any?,
        valuesInType: ConstantValue<*>
    ) {
        if (expressionValue !in valuesInType) {
            reporter.report1(
                element,
                ErrorKeys.KEY_VALUE_MISMATCH,
                valuesInType.values()
            )
        }
    }

}