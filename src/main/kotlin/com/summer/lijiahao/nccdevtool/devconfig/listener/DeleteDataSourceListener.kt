package com.summer.lijiahao.nccdevtool.devconfig.listener

import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.devconfig.listener.base.AbstractListener
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSourceUtil
import java.awt.event.ActionEvent
import javax.swing.DefaultComboBoxModel

class DeleteDataSourceListener(dialog: NCCloudDevConfigDialog) : AbstractListener(dialog) {
    override fun actionPerformed(e: ActionEvent) {
        if (DataSourceUtil.dataSources.size == 1) {
            Messages.showMessageDialog("当前环境只剩一下一个数据源，不能删除！", "tips", Messages.getInformationIcon())
            return
        }

        val currentDataSourceName = dialog.dbBox?.selectedItem

        //移除当前数据源
        DataSourceUtil.dataSources.remove(currentDataSourceName)

        val dataSource = DataSourceUtil.dataSources.values.first()
        val items = DataSourceUtil.dataSources.keys.toTypedArray()
        dialog.dbBox?.model = DefaultComboBoxModel(items)
        dialog.dbBox?.selectedItem = dataSource.dataSourceName
        dialog.hostText?.text = dataSource.databaseUrl
        dialog.oidText?.text = dataSource.oidMark
        dialog.userText?.text = dataSource.user
        dialog.dbNameText?.text = dataSource.dbName
        dialog.hostText?.text = dataSource.url
        dialog.portText?.text = dataSource.port

        if (dataSource.dataSourceName == "design") {
            dialog.devChx?.isSelected = true
        }
        if (dataSource.isBase == "true") {
            dialog.baseChx?.isSelected = true
        }
        dialog.pwdText?.text = dataSource.password

        val opt = Messages.showYesNoDialog("删除成功", "提示", Messages.getQuestionIcon())
        if (opt == Messages.OK) {
            DataSourceUtil.writeDataSource(dialog)
        }
    }
}