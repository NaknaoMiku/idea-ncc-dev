package com.summer.lijiahao.nccdevtool.devconfig.listener

import com.summer.lijiahao.nccdevtool.devconfig.listener.base.AbstractListener
import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSourceUtil
import java.awt.event.ActionEvent
import javax.swing.JComponent
import javax.swing.JFileChooser

class SelectHomeListener(component: JComponent, dialog: NCCloudDevConfigDialog) : AbstractListener(dialog) {
    private var component: JComponent

    init {
        this.component = component
    }

    override fun actionPerformed(e: ActionEvent) {
        val ncHome = NCCloudEnvSettingService.getInstance(dialog.event).ncHomePath
        val chooser = JFileChooser(ncHome)
        chooser.isMultiSelectionEnabled = false
        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        val ret: Int = chooser.showOpenDialog(component)
        if (JFileChooser.APPROVE_OPTION != ret) {
            return
        }

        dialog.homeText?.let { it.text = chooser.selectedFile.absolutePath }
        DataSourceUtil.initDataSource(dialog)
    }
}