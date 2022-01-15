package com.bennyhuo.kotlin.valudedef.compiler

import com.bennyhuo.kotlin.compiletesting.extensions.module.KotlinModule
import com.bennyhuo.kotlin.compiletesting.extensions.module.compileAll
import com.bennyhuo.kotlin.compiletesting.extensions.module.resolveAllDependencies
import com.bennyhuo.kotlin.compiletesting.extensions.result.ResultCollector
import com.bennyhuo.kotlin.compiletesting.extensions.source.SingleFileModuleInfoLoader
import com.bennyhuo.kotlin.valuedef.compiler.ValueDefCopyComponentRegistrar
import org.junit.Test

/**
 * Created by benny at 2022/1/15 8:55 PM.
 */
class ValueDefTest {

    @Test
    fun basic() {
        testBase("basic.kt")
    }

    private fun testBase(fileName: String) {
        val loader = SingleFileModuleInfoLoader("testData/$fileName")
        val sourceModuleInfos = loader.loadSourceModuleInfos()

        val modules = sourceModuleInfos.map {
            KotlinModule(it, componentRegistrars = listOf(ValueDefCopyComponentRegistrar()))
        }

        modules.resolveAllDependencies()
        modules.compileAll()

        val resultMap = modules.associate {
            it.name to it.runJvm()
        }

        loader.loadExpectModuleInfos().fold(ResultCollector()) { collector, expectModuleInfo ->
            collector.collectModule(expectModuleInfo.name)
            expectModuleInfo.sourceFileInfos.forEach {
                collector.collectFile(it.fileName)
                collector.collectLine(it.sourceBuilder, resultMap[expectModuleInfo.name]?.get(it.fileName))
            }
            collector
        }.apply()
    }

}