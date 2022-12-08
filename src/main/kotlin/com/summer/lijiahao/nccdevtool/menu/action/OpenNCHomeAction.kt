package com.summer.lijiahao.nccdevtool.menu.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import java.awt.Desktop
import java.io.File

class OpenNCHomeAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        try {
            val ncHome: String = NCCloudEnvSettingService.getInstance(event).ncHomePath
            if (ncHome.isEmpty()) {
                throw Exception("未配置NC home")
            }
            val desktop = Desktop.getDesktop()
            val dirToOpen = File(ncHome)
            desktop.open(dirToOpen)
        } catch (e: Exception) {
            val message = e.message
            Messages.showInfoMessage(message, "Error")
        }
    }
}
