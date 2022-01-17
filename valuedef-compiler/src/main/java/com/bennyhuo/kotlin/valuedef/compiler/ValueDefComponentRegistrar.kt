package com.bennyhuo.kotlin.valuedef.compiler

import com.google.auto.service.AutoService
import com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor

@AutoService(ComponentRegistrar::class)
class ValueDefComponentRegistrar : ComponentRegistrar {

    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration
    ) {
        StorageComponentContainerContributor.registerExtension(
            project,
            ValueDefComponentContainerContributor()
        )
    }
}


