package com.bennyhuo.kotlin.valuedef.idea

import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.Key
import com.intellij.openapi.externalSystem.model.ProjectKeys
import com.intellij.openapi.externalSystem.model.project.AbstractExternalEntityData
import com.intellij.openapi.externalSystem.model.project.ModuleData
import com.intellij.openapi.externalSystem.service.project.manage.AbstractProjectDataService
import com.intellij.serialization.PropertyMapping
import org.gradle.tooling.model.idea.IdeaModule
import org.jetbrains.plugins.gradle.service.project.AbstractProjectResolverExtension
import org.jetbrains.plugins.gradle.util.GradleConstants

/**
 * Created by benny at 2022/1/13 8:13 AM.
 */
class ValueDefIdeModel @PropertyMapping("isEnabled") constructor(
    val isEnabled: Boolean
) : AbstractExternalEntityData(GradleConstants.SYSTEM_ID) {
    companion object {
        val KEY = Key.create(ValueDefIdeModel::class.java, ProjectKeys.CONTENT_ROOT.processingWeight + 1)
    }
}

class ValueDefIdeModelDataService : AbstractProjectDataService<ValueDefIdeModel, Void>() {
    override fun getTargetDataKey() = ValueDefIdeModel.KEY
}

class ValueDefProjectResolverExtension : AbstractProjectResolverExtension() {

    override fun getExtraProjectModelClasses() = setOf(ValueDefGradleModel::class.java)
    override fun getToolingExtensionsClasses() = setOf(ValueDefModelBuilder::class.java, Unit::class.java)

    override fun populateModuleExtraModels(gradleModule: IdeaModule, ideModule: DataNode<ModuleData>) {
        val gradleModel = resolverCtx.getExtraProject(gradleModule, ValueDefGradleModel::class.java)

        if (gradleModel != null && gradleModel.isEnabled) {
            ideModule.createChild(ValueDefIdeModel.KEY, ValueDefIdeModel(isEnabled = gradleModel.isEnabled))
        }

        super.populateModuleExtraModels(gradleModule, ideModule)
    }
}