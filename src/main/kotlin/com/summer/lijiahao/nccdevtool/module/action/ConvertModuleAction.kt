package com.summer.lijiahao.nccdevtool.module.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.impl.FsRoot
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.module.util.ModuleUtil
import java.io.File

class ConvertModuleAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        val file = getSelectFileArr(event)
        var flag = true
        file?.let {
            for (f in it) {
                try {
                    ModuleUtil().coverToModule(event.project!!, f.path)
                } catch (e: Exception) {
                    Messages.showErrorDialog(e.message, "wrong")
                    flag = false
                    break
                }
            }
        }

        if (flag) {
            Messages.showInfoMessage("finish", "success")
        }
    }

    override fun update(e: AnActionEvent) {
        var flag = true
        val selectFileArr: Array<VirtualFile>? = getSelectFileArr(e)
        if (selectFileArr.isNullOrEmpty()) {
            flag = false
        } else {
            for (virtualFile in selectFileArr) {
                if (virtualFile is FsRoot) {
                    flag = false
                    break
                }
                val module = com.intellij.openapi.module.ModuleUtil.findModuleForFile(
                    virtualFile,
                    e.project!!
                )
                //这里当前目录如果没有转module，会找到上曾module，所以永远不是空
                flag =
                    module != null && module.name != virtualFile.name && File(virtualFile.path + File.separator + "META-INF" + File.separator + "module.xml").exists()
            }
        }
        e.presentation.isEnabledAndVisible = flag
    }
}