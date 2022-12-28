package com.summer.lijiahao.nccdevtool.extend.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.devconfig.util.libraries.LibrariesUtil

class LibrariesSetAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        try {
            val flag = isNCModule(event)
            if (flag) {
                //设置类路径
                LibrariesUtil.setModuleLibrary(event.project, event.getData(LangDataKeys.MODULE)!!)
                Messages.showInfoMessage("success", "Tips")
            }
        } catch (e: Exception) {
            Messages.showInfoMessage(e.message, "Error")
        }
    }

    override fun update(e: AnActionEvent) {
        val file = getSelectFile(e)
        val flag = isModuleChild(file, e)
        e.presentation.isEnabledAndVisible = flag
    }
}