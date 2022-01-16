package com.bennyhuo.kotlin.valudedef.compiler

import com.bennyhuo.kotlin.compiletesting.extensions.module.KotlinModule
import com.bennyhuo.kotlin.compiletesting.extensions.module.compileAll
import com.bennyhuo.kotlin.compiletesting.extensions.module.resolveAllDependencies
import com.bennyhuo.kotlin.compiletesting.extensions.result.ResultCollector
import com.bennyhuo.kotlin.compiletesting.extensions.source.SingleFileModuleInfoLoader
import com.bennyhuo.kotlin.valuedef.compiler.ValueDefCopyComponentRegistrar
import org.junit.Test
import java.io.File

/**
 * Created by benny at 2022/1/15 8:55 PM.
 */
class ValueDefTest {

    private val compileLogName = "compiles.log"

    class CompileResultInfo(
        val level: String,
        val path: String,
        val offsetStart: Int,
        val offsetEnd: Int,
        val message: String
    ) {
        companion object {
            // e: /var/folders/mh/m7khk5857xx1gcqgg354lzr40000gn/T/Kotlin-Compilation17942368887748986840/sources/Main.kt: (12, 13): More than one ValueDefs are declared: [@Color, @ExtendedColor].
            private val compileLogPattern = Regex("([ew]): .*/(.*): \\((\\d+), (\\d+)\\): (.*)")

            fun parse(value: String): Sequence<CompileResultInfo> {
                return compileLogPattern.findAll(value).map { result ->
                    CompileResultInfo(
                        result.groupValues[1],
                        result.groupValues[2],
                        result.groupValues[3].toIntOrNull() ?: -1,
                        result.groupValues[4].toIntOrNull() ?: -1,
                        result.groupValues[5],
                    )
                }
            }
        }

        fun toShortString() = "$level: ${File(path).name}: ($offsetStart, $offsetEnd): $message"
    }

    @Test
    fun basic() {
        testBase("basic.kt")
    }

    @Test
    fun stringTypeConflicts() {
        testBase("stringTypeConflicts.kt")
    }

    @Test
    fun valueMismatch() {
        testBase("valueMismatch.kt")
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
            val result = it.compileResult?.let {
                mapOf(compileLogName to "${it.exitCode}\n${CompileResultInfo.parse(it.messages).joinToString("\n") { it.toShortString() }}")
            } ?: it.runJvm()
            it.name to result
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