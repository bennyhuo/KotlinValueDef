package com.bennyhuo.kotlin.valuedef.idea

import com.bennyhuo.kotlin.valuedef.compiler.ValueDefComponentContainerContributor
import org.jetbrains.kotlin.analyzer.moduleInfo
import org.jetbrains.kotlin.container.StorageComponentContainer
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.idea.caches.project.ModuleSourceInfo
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

/**
 * Created by benny at 2022/1/17 4:28 PM.
 */
class IdeValueDefComponentContainerContributor: ValueDefComponentContainerContributor() {

    override fun registerModuleComponents(
        container: StorageComponentContainer,
        platform: TargetPlatform,
        moduleDescriptor: ModuleDescriptor
    ) {
        val module = moduleDescriptor.moduleInfo.safeAs<ModuleSourceInfo>()?.module ?: return
        if (ValueDefAvailability.isAvailable(module)) {
            super.registerModuleComponents(container, platform, moduleDescriptor)
        }
    }

}