package com.summer.lijiahao.nccdevtool.devconfig.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class NCCloudDevConfigAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val dialog = NCCloudDevConfigDialog(e)

        dialog.setSize(780, 500)
        dialog.show()
    }
}