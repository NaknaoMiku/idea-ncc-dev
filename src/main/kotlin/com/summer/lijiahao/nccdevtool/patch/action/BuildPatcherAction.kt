package com.summer.lijiahao.nccdevtool.patch.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.newvfs.impl.FsRoot
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.patch.ui.PatcherDialog

class BuildPatcherAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        val dialog = PatcherDialog(event)
        dialog.setSize(900, 600)
        dialog.show()
    }

    override fun update(e: AnActionEvent) {
        val selectFileArr = getSelectFileArr(e)
        var flag = true
        if (selectFileArr.isNullOrEmpty()) {
            flag = false
        } else {
            for (virtualFile in selectFileArr) {
                if (virtualFile is FsRoot) {
                    flag = false
                    break
                }
                flag = isModuleChild(virtualFile, e) || isMavenModuleChild(virtualFile, e)
            }
        }
        e.presentation.isEnabledAndVisible = flag
    }
}