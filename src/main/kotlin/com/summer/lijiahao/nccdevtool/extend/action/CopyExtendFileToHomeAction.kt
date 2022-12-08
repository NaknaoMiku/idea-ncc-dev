package com.summer.lijiahao.nccdevtool.extend.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.newvfs.impl.FsRoot
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.extend.util.ExtendFileCopyUtil
import java.io.File

class CopyExtendFileToHomeAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        try {
            ExtendFileCopyUtil.copyExtendFile(event)
            Messages.showInfoMessage("success", "Tips")
        } catch (e: Exception) {
            Messages.showInfoMessage(e.message, "Error")
        }
    }

    override fun update(e: AnActionEvent) {
        val selectFile = getSelectFile(e)
        var flag: Boolean
        if (selectFile == null || selectFile is FsRoot) {
            flag = false
        } else {
            val file = File(selectFile.path)
            if (file.isFile) {
                flag =
                    file.name.endsWith(".xml") && file.path.contains("yyconfig/modules") && (file.parent.endsWith("action") || file.parent.endsWith(
                        "authorize"
                    ))
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