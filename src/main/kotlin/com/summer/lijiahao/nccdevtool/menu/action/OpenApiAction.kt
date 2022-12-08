package com.summer.lijiahao.nccdevtool.menu.action

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction

class OpenApiAction : AbstractAnAction() {

    private val URL = "https://nccdev.yonyou.com/openapi/doc"

    override fun doAction(event: AnActionEvent) {
        BrowserUtil.open(URL)
    }
}
