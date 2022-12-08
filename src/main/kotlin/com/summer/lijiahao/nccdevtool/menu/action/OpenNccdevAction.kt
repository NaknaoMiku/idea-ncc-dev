package com.summer.lijiahao.nccdevtool.menu.action

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction

class OpenNccdevAction : AbstractAnAction() {
    private val DEVURL = "https://nccdev.yonyou.com/index"
    override fun doAction(event: AnActionEvent) {
        BrowserUtil.open(DEVURL)
    }
}
