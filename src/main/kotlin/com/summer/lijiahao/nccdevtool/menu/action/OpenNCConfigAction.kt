package com.summer.lijiahao.nccdevtool.menu.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import java.io.File
import java.util.*

class OpenNCConfigAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        try {
            val ncHome: String = NCCloudEnvSettingService.getInstance(event).ncHomePath
            if (ncHome.isEmpty()) {
                throw Exception("未配置NC home")
            }
            var configDirPath = ncHome + File.separatorChar + "bin" + File.separatorChar
            configDirPath = if (isWindows) {
                configDirPath + "sysConfig.bat"
            } else {
                configDirPath + "sysConfig.sh"
            }
            val configFile = File(configDirPath)
            if (!configFile.isFile) {
                throw Exception("配置文件不存在")
            }
            Runtime.getRuntime().exec(configDirPath)
        } catch (e: Exception) {
            val message = e.message
            Messages.showInfoMessage(message, "Error")
        }
    }

    private val isWindows: Boolean
        get() = System.getProperty("os.name").lowercase(Locale.getDefault()).contains("windows")
}
