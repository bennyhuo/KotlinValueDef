package com.bennyhuo.kotlin.valuedef.idea

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.module.Module
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.util.module

/**
 * Created by benny at 2022/1/13 1:04 PM.
 */
object ValueDefAvailability {
    fun isAvailable(element: PsiElement): Boolean {
        if (ApplicationManager.getApplication().isUnitTestMode) {
            return true
        }

        val module = element.module ?: return false
        return isAvailable(module)
    }

    fun isAvailable(module: Module): Boolean {
        if (ApplicationManager.getApplication().isUnitTestMode) {
            return true
        }

        return ValueDefAvailabilityProvider.PROVIDER_EP.getExtensions(module.project).any { it.isAvailable(module) }
    }
}

interface ValueDefAvailabilityProvider {
    companion object {
        val PROVIDER_EP: ExtensionPointName<ValueDefAvailabilityProvider> =
            ExtensionPointName("com.bennyhuo.kotlin.valuedef.idea.availabilityProvider")
    }

    fun isAvailable(module: Module): Boolean
}