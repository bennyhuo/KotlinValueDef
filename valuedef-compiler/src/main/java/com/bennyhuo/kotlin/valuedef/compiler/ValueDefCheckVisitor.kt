package com.bennyhuo.kotlin.valuedef.compiler

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
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
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.constants.ConstantValue

/**
 * Created by benny at 2022/1/16 9:28 AM.
 */
class ValueDefCheckVisitor(bindingTrace: BindingTrace, private val isRecursive: Boolean = false) : KtVisitorVoid() {

    private val bindingContext: BindingContext = bindingTrace.bindingContext
    private val reporter = CompilerCheckReporter(bindingTrace)

    override fun visitElement(element: PsiElement) {
        if (isRecursive) {
            element.acceptChildren(this)
        } else {
            super.visitElement(element)
        }
    }

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (klass.isAnnotation()) {
            val valueDefAnnotations = klass.annotationEntries.filter {
                it.getQualifiedName(bindingContext) in valueDefs
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

        val declaredTypeFqName = typeReference.declaredTypeFqName(bindingContext) ?: return

        val valueTypeAnnotations = typeReference.annotationEntries.map {
            it to it.findFirstValueDefAnnotation(bindingContext)
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
        val returnTypeValues = function.type(bindingContext).definedConstantValueOrNull() ?: return
        val returningExpression = function.returningExpression ?: return

        if (function.isReturningUnsafeValueType(bindingContext)) return
        val possibleReturnValues = function.possibleReturnValuesOrNull(bindingContext)
        checkValue(returningExpression, possibleReturnValues, returnTypeValues)
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
        // val x = 2
        val constantValues = property.type(bindingContext).definedConstantValueOrNull() ?: return
        val initializer = property.initializer ?: return

        if (initializer.isUnsafeValueType(bindingContext)) return
        val rhsValue = initializer.possibleConstantValuesOrNull(bindingContext)
        checkValue(initializer, rhsValue, constantValues)
    }

    override fun visitBinaryExpression(expression: KtBinaryExpression) {
        super.visitBinaryExpression(expression)
        if (expression.operationToken == KtTokens.EQ) {
            val values = expression.left.safeAs<KtNameReferenceExpression>()
                ?.definedConstantValueOrNull(bindingContext) ?: return
            val right = expression.right ?: return
            if (right.isUnsafeValueType(bindingContext)) return

            val value = right.possibleConstantValuesOrNull(bindingContext)
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
            if (argumentExpression.isUnsafeValueType(bindingContext)) {
                return
            }

            if (constantValues != null) {
                val argumentValue = argument.getArgumentExpression()?.possibleConstantValuesOrNull(bindingContext)
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