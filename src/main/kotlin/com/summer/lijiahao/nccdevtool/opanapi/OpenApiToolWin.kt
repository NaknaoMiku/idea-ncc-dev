package com.summer.lijiahao.nccdevtool.opanapi

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class OpenApiToolWin : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val openApiView = OpenApiView()
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(openApiView, "", true)
        toolWindow.contentManager.addContent(content)
    }
}