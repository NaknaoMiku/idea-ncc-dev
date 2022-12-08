package com.summer.lijiahao.nccdevtool.debug.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.newvfs.impl.FsRoot
import java.io.File

class AppGroupAction : DefaultActionGroup() {
    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val module = e.getData(LangDataKeys.MODULE)
        var flag = module != null && file != null && file !is FsRoot && module.name == file.name
        if (flag) {
            val contentRoots = ModuleRootManager.getInstance(module!!).contentRoots
            flag = File(contentRoots[0].path + File.separator + "META-INF" + File.separator + "module.xml").exists()
        }
        e.presentation.isEnabledAndVisible = flag
    }
}