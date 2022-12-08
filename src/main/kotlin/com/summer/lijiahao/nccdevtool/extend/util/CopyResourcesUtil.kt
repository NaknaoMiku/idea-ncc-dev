package com.summer.lijiahao.nccdevtool.extend.util

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.util.io.FileUtil
import com.summer.lijiahao.nccdevtool.base.util.BaseUtil
import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import java.io.File
import java.io.IOException

class CopyResourcesUtil(private var selectFilePach: String) {

    fun copy(event: AnActionEvent) {
        val homePath: String = NCCloudEnvSettingService.getInstance(event).ncHomePath
        val resourcesPath = homePath + File.separator + "resources" + File.separator
        val module: Module = BaseUtil.getModule(event)
        val contentRoots = ModuleRootManager.getInstance(module).contentRoots
        if (contentRoots.isEmpty()) {
            return
        }
        val file = File(selectFilePach)
        try {
            // 逐个拷贝到home
            FileUtil.copy(file, File(resourcesPath + file.name))
        } catch (ignored: IOException) {
            throw IOException("文件" + file.name + "拷贝出错")
        }
    }
}