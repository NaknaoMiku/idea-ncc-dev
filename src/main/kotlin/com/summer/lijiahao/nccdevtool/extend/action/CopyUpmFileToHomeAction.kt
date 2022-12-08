package com.summer.lijiahao.nccdevtool.extend.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.newvfs.impl.FsRoot
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.extend.util.ExtendFileCopyUtil
import java.io.File

class CopyUpmFileToHomeAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        ExtendFileCopyUtil.copyUpmFile(event)
        Messages.showInfoMessage("success", "Tips")
    }

    override fun update(e: AnActionEvent) {
        val selectFile = getSelectFile(e)
        var flag: Boolean
        if (selectFile == null || selectFile is FsRoot) {
            flag = false
        } else {
            val file = File(selectFile.path)
            if (file.isFile) {
                flag = file.path.contains("META-INF") && (file.name.endsWith(".upm") || file.name.endsWith(".rest"))
            } else {
                flag = isModuleChild(selectFile, e)
                if (flag) {
                    val module = getSelectModule(e)
                    module?.let {
                        val contentRoots = ModuleRootManager.getInstance(it).contentRoots
                        if (contentRoots.isNotEmpty() && selectFile.parent == contentRoots[0]) {
                            flag = File(selectFile.path + File.separator + "component.xml").exists()
                        }
                    }
                }
            }
        }
        e.presentation.isEnabledAndVisible = flag
    }
}