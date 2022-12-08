package com.summer.lijiahao.nccdevtool.extend.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.newvfs.impl.FsRoot
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.extend.util.CopyResourcesUtil
import java.io.File

class CopyResourcesFileToHomeAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        try {
            val selectFile = getSelectFile(event)
            val util = CopyResourcesUtil(selectFile!!.path)
            util.copy(event)
            Messages.showInfoMessage("success", "Tips")
        } catch (e: Exception) {
            Messages.showInfoMessage(e.message, "Error")
        }
    }

    override fun update(e: AnActionEvent) {
        val selectFile = getSelectFile(e)
        val flag: Boolean = if (selectFile == null || selectFile is FsRoot) {
            false
        } else {
            val file = File(selectFile.path)
            if (file.isFile) {
                file.name.endsWith(".properties")
            } else {
                false
            }
        }
        e.presentation.isEnabledAndVisible = flag
    }
}