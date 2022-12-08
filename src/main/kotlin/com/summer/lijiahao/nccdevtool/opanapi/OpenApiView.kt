package com.summer.lijiahao.nccdevtool.opanapi

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.summer.lijiahao.nccdevtool.opanapi.action.FormatRequestJsonAction
import com.summer.lijiahao.nccdevtool.opanapi.action.SendRequestAction
import com.summer.lijiahao.nccdevtool.opanapi.action.SettingAction
import com.summer.lijiahao.nccdevtool.opanapi.ui.OpenApiTool

class OpenApiView : SimpleToolWindowPanel(false, true) {
    private val openApiTool: OpenApiTool = OpenApiTool()

    init {
        val barGroup = DefaultActionGroup()
        barGroup.add(SendRequestAction(openApiTool))
        barGroup.add(SettingAction())
        barGroup.add(FormatRequestJsonAction(openApiTool))
        val toolbar = ActionManager.getInstance().createActionToolbar("toolbar", barGroup, true)
        toolbar.targetComponent = this
        setToolbar(toolbar.component)
        openApiTool.contentPane?.let { setContent(it) }
    }
}