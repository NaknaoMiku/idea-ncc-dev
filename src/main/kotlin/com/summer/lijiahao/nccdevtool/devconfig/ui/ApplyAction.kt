package com.summer.lijiahao.nccdevtool.devconfig.ui

import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSourceUtil
import com.summer.lijiahao.nccdevtool.devconfig.util.libraries.LibrariesUtil
import com.summer.lijiahao.nccdevtool.devconfig.util.modules.ModulesUtil
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ApplyAction(dialog: NCCloudDevConfigDialog) : AbstractAction("Apply") {

    val dialog: NCCloudDevConfigDialog

    init {
        this.dialog = dialog
    }


    override fun actionPerformed(e: ActionEvent) {
        val homePath = dialog.homeText?.text
        if (!homePath.isNullOrEmpty()) {
            //判断当前的home路径是否和环境的一致
            val homeChanged = NCCloudEnvSettingService.getInstance(dialog.event).ncHomePath != homePath
            if (homeChanged) {
                NCCloudEnvSettingService.getInstance(dialog.event).ncHomePath = homePath
                LibrariesUtil.setLibrariesAndTip(dialog.event, homePath)
            }
            DataSourceUtil.writeDataSource(dialog)
            ModulesUtil.writeModuleToConfig(dialog)

        }
    }
}