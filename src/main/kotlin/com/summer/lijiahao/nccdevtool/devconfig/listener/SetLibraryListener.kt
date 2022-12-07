package com.summer.lijiahao.nccdevtool.devconfig.listener

import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.devconfig.listener.base.AbstractListener
import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import com.summer.lijiahao.nccdevtool.devconfig.util.libraries.LibrariesUtil
import java.awt.event.ActionEvent

class SetLibraryListener(dialog: NCCloudDevConfigDialog) : AbstractListener(dialog) {

    override fun actionPerformed(e: ActionEvent) {
        val homePath: String = NCCloudEnvSettingService.getInstance(dialog.event).ncHomePath

        LibrariesUtil.setLibraries(dialog.event, homePath)
        Messages.showInfoMessage("设置完成！", "提示")
    }
}