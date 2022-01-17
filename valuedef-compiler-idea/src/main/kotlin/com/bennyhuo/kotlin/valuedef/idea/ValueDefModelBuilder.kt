package com.bennyhuo.kotlin.valuedef.idea

import com.bennyhuo.kotlin.valuedef.BuildConfig
import org.gradle.api.Project
import org.jetbrains.kotlin.idea.gradleTooling.AbstractKotlinGradleModelBuilder
import org.jetbrains.plugins.gradle.tooling.ErrorMessageBuilder
import java.io.Serializable
import java.lang.Exception

/**
 * Created by benny at 2022/1/13 8:14 AM.
 */
class ValueDefModelBuilder : AbstractKotlinGradleModelBuilder() {
    override fun buildAll(modelName: String?, project: Project): Any {
        val plugin = project.plugins.findPlugin(BuildConfig.KOTLIN_PLUGIN_ID)
        return ValueDefGradleModelImpl(plugin != null)
    }

    override fun canBuild(modelName: String?): Boolean {
        return modelName == ValueDefGradleModel::class.java.name
    }

    override fun getErrorMessageBuilder(project: Project, e: Exception): ErrorMessageBuilder {
        return ErrorMessageBuilder.create(project, e, "Gradle import errors")
            .withDescription("Unable to build ${BuildConfig.KOTLIN_PLUGIN_ID} plugin configuration")
    }
}

interface ValueDefGradleModel : Serializable {
    val isEnabled: Boolean
}

class ValueDefGradleModelImpl(override val isEnabled: Boolean) : ValueDefGradleModel
