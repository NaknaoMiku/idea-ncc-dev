package com.summer.lijiahao.nccdevtool.debug.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.debug.util.CreatApplicationConfigurationUtil

class NewClientApplicationAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        try {
            CreatApplicationConfigurationUtil.createApplicationConfiguration(event, false)
            Messages.showInfoMessage("success", "Tips")
        } catch (e: Exception) {
            Messages.showInfoMessage(e.message, "Error")
        }
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val flag = isModuleChild(file, e)
        e.presentation.isEnabledAndVisible = flag
    }
}