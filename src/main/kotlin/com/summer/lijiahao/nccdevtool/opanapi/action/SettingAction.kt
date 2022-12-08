package com.summer.lijiahao.nccdevtool.opanapi.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.summer.lijiahao.nccdevtool.opanapi.ui.OpenapiSettingDialog

class SettingAction : DumbAwareAction("OpenAPI配置", "OpenAPI配置内容", AllIcons.Actions.InlayGear) {
    override fun actionPerformed(e: AnActionEvent) {
        val dialog = OpenapiSettingDialog(e)
        dialog.setSize(500, 300)
        dialog.show()
    }
}