package com.summer.lijiahao.nccdevtool.module.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.module.ui.CreateNewModuleDialog

class CreateNewNCModuleAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        val dialog = CreateNewModuleDialog(event)
        dialog.setSize(900, 300)
        dialog.show()
    }
}