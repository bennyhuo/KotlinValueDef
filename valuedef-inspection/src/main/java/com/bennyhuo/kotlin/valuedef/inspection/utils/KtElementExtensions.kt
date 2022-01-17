package com.bennyhuo.kotlin.valuedef.inspection.utils

import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.idea.caches.resolve.getResolutionFacade
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode

/**
 * Created by benny at 2022/1/17 12:46 PM.
 */
val KtElement.bindingContext: BindingContext
    get() = analyze(BodyResolveMode.PARTIAL)
