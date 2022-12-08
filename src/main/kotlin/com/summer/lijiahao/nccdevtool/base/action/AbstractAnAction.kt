package com.summer.lijiahao.nccdevtool.base.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

abstract class AbstractAnAction : AnAction() {

    override fun actionPerformed(anActionEvent: AnActionEvent) {
//        ProjectManager.getInstance().setProject(anActionEvent.project)
        doAction(anActionEvent)
    }

    abstract fun doAction(event: AnActionEvent)

    /**
     * 获取选中文件
     *
     * @param event
     * @return
     */
    open fun getSelectFile(event: AnActionEvent): VirtualFile? {
        return event.getData(CommonDataKeys.VIRTUAL_FILE)
    }

    open fun getSelectFileArr(event: AnActionEvent): Array<VirtualFile>? {
        return event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
    }

    /**
     * 是否nc module
     *
     * @param event
     * @return
     */
    open fun isNCModule(event: AnActionEvent): Boolean {
        val selectFile = getSelectFile(event) ?: return false
        val module = ModuleUtil.findModuleForFile(selectFile, event.project!!)
            ?: return false
        return module.name == selectFile.name && File(selectFile.path + File.separator + "META-INF" + File.separator + "module.xml").exists()
    }

    open fun isModuleChild(file: VirtualFile?, event: AnActionEvent): Boolean {
        val flag: Boolean
        if (file == null) {
            return false
        }
        val module = ModuleUtil.findModuleForFile(file, event.project!!)
            ?: return false
        val contentRoots = ModuleRootManager.getInstance(module).contentRoots
        if (contentRoots.isEmpty()) {
            return false
        }
        flag = File(contentRoots[0].path + File.separator + "META-INF" + File.separator + "module.xml").exists()
        return flag
    }

    open fun isMavenModuleChild(file: VirtualFile?, event: AnActionEvent): Boolean {
        val flag: Boolean
        if (file == null) {
            return false
        }
        val module = ModuleUtil.findModuleForFile(file, event.project!!)
            ?: return false
        val contentRoots = ModuleRootManager.getInstance(module).contentRoots
        if (contentRoots.isEmpty()) {
            return false
        }
        flag = File(contentRoots[0].path + File.separator + "pom.xml").exists()
        return flag
    }

    /**
     * 获取选中模块
     *
     * @param event
     * @return
     */
    open fun getSelectModule(event: AnActionEvent): Module? {
        return event.getData(LangDataKeys.MODULE)
    }
}