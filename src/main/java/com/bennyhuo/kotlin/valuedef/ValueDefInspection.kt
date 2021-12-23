package com.bennyhuo.kotlin.valuedef

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.nj2k.postProcessing.resolve
import org.jetbrains.kotlin.nj2k.postProcessing.type
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.referenceExpression
import org.jetbrains.kotlin.resolve.constants.ConstantValue

/**
 * Created by benny.
 */
class ValueDefInspection : AbstractKotlinInspection() {

    fun checkValueType(
        holder: ProblemsHolder,
        element: PsiElement,
        expressionValue: Any?,
        valuesInType: ConstantValue<*>
    ) {
        if (expressionValue !in valuesInType) {
            holder.registerProblem(
                element,
                ValueDefBundle.message("inspection.valuetype.error.display", valuesInType.values()),
                ProblemHighlightType.GENERIC_ERROR
            )
        }
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : KtVisitorVoid() {

            override fun visitClass(klass: KtClass) {
                super.visitClass(klass)
                if (klass.isAnnotation()) {
                    val valueTypeAnnotations = klass.annotationEntries.filter {
                        it.getQualifiedName() in valueDefs.values
                    }
                    if (valueTypeAnnotations.size > 1) {
                        holder.registerProblem(
                            klass,
                            ValueDefBundle.message(
                                "inspection.valuetype.annotation.define.error.display",
                                valueTypeAnnotations.map { it.text }),
                            ProblemHighlightType.GENERIC_ERROR
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
                    holder.registerProblem(
                        typeReference,
                        ValueDefBundle.message(
                            "inspection.valuetype.annotation.error.display",
                            valueTypeAnnotations.map { it.first.text }),
                        ProblemHighlightType.GENERIC_ERROR
                    )
                    return
                }

                if (valueTypeAnnotations.size == 1) {
                    val valueTypeAnnotation = valueTypeAnnotations.first().second!!
                    val valueTypeAnnotationFqName = valueTypeAnnotation.fqName?.asString()
                    if (valueDefs[declaredTypeFqName] != valueTypeAnnotationFqName) {
                        holder.registerProblem(
                            typeReference,
                            ValueDefBundle.message(
                                "inspection.valuetype.type.error.display",
                                valueTypeAnnotationFqName, declaredTypeFqName
                            ),
                            ProblemHighlightType.GENERIC_ERROR
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
                checkValueType(holder, returningExpression, possibleReturnValues, returnTypeValues)
            }

            override fun visitProperty(property: KtProperty) {
                super.visitProperty(property)
                // val x = 2
                val constantValues = property.type().definedConstantValueOrNull() ?: return
                val initializer = property.initializer ?: return

                if (initializer.isUnsafeValueType()) return
                val rhsValue = initializer.possibleConstantValuesOrNull()
                checkValueType(holder, initializer, rhsValue, constantValues)
            }

            override fun visitBinaryExpression(expression: KtBinaryExpression) {
                super.visitBinaryExpression(expression)
                if (expression.operationToken == KtTokens.EQ) {
                    val values =
                        expression.left.safeAs<KtNameReferenceExpression>()?.definedConstantValueOrNull() ?: return
                    val right = expression.right ?: return
                    if (right.isUnsafeValueType()) return

                    val value = right.possibleConstantValuesOrNull()
                    checkValueType(holder, right, value, values)
                }
            }

            override fun visitCallExpression(callExpression: KtCallExpression) {
                super.visitCallExpression(callExpression)

                // 1. find value parameters type
                val ktNamedFunction = callExpression.referenceExpression()?.resolve() as? KtNamedFunction ?: return
                val constParameters = ktNamedFunction.valueParameters.mapIndexedNotNull { index, parameter ->
                    parameter.type().definedConstantValueOrNull()?.let {
                        ConstantValueHolder(index, parameter.name, it)
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
                        checkValueType(holder, argument, argumentValue, constantValues.constantValue)
                    }
                }
            }
        }
    }

}