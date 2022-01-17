package com.bennyhuo.kotlin.valuedef.compiler.reporter

import org.jetbrains.kotlin.diagnostics.rendering.DefaultErrorMessages
import org.jetbrains.kotlin.diagnostics.rendering.DiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.rendering.Renderers

/**
 * Created by benny at 2022/1/16 10:25 AM.
 */
object ValueDefErrorMessages: DefaultErrorMessages.Extension {

    private val rendererMap = DiagnosticFactoryToRendererMap("ValueDef")

    override fun getMap() = rendererMap

    init {
        rendererMap.apply {
            put(ValueDefErrors.VALUE_MISMATCH, "Value mismatch. Must be in {0}.", Renderers.TO_STRING)
            put(ValueDefErrors.DEF_CONFLICTS, "More than one ValueDefs are declared: {0}.", Renderers.TO_STRING)
            put(ValueDefErrors.TYPE_CONFLICTS, "Conflict types: {0}.", Renderers.TO_STRING)
            put(ValueDefErrors.TYPE_MISMATCH, "''{0}'' is not compatible with declared type ''{1}''.", Renderers.TO_STRING, Renderers.TO_STRING)
        }
    }
}