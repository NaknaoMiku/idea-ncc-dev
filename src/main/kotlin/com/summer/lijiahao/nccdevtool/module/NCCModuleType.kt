package com.summer.lijiahao.nccdevtool.module

import com.intellij.icons.AllIcons
import com.intellij.openapi.module.ModuleType
import javax.swing.Icon

class NCCModuleType(id: String = "JAVA_MODULE") : ModuleType<NCCModuleBuilder>(id) {
    override fun createModuleBuilder(): NCCModuleBuilder {
        return NCCModuleBuilder()
    }

    override fun getName(): String {
        return "ncc-dev-module"
    }

    override fun getDescription(): String {
        return "ncc-dev-module"
    }

    override fun getNodeIcon(isOpened: Boolean): Icon {
        return AllIcons.Nodes.Module
    }


}