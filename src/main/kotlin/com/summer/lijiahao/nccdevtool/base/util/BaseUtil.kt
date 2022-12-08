package com.summer.lijiahao.nccdevtool.base.util

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class BaseUtil {

    companion object {
        fun getProject(event: AnActionEvent): Project {

            val project = event.project
            if (project == null) {
                Messages.showMessageDialog(
                    "无法获取当前项目",
                    "Error",
                    Messages.getErrorIcon()
                )
                throw RuntimeException("无法获取当前项目")
            }
            return project

        }

        fun getModule(event: AnActionEvent): Module {

            val module = event.getData(LangDataKeys.MODULE)
            if (module == null) {
                Messages.showMessageDialog(
                    "无法获取当前模块",
                    "Error",
                    Messages.getErrorIcon()
                )
                throw RuntimeException("无法获取当前模块")
            }
            return module
        }
    }
}