package com.summer.lijiahao.nccdevtool.menu.action

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * @escription 打开插件使用手册
 * @Author summer
 * @Email lijiahaosummer@gmail.com
 * @Date 2022/11/26 10:43
 * @Version 1.0
 */
class OpenUseHelpAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        BrowserUtil.open(HELPFULNESS)
    }

    companion object {
        private const val HELPFULNESS = "https://gitee.com/nakanonmiku/idea-ncc-plug/blob/master/readme.md"
    }
}
