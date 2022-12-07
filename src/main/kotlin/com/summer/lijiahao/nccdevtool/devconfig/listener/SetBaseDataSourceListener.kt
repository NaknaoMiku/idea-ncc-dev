package com.summer.lijiahao.nccdevtool.devconfig.listener

import com.summer.lijiahao.nccdevtool.devconfig.listener.base.AbstractListener
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSourceUtil
import java.awt.event.ActionEvent

class SetBaseDataSourceListener(dialog: NCCloudDevConfigDialog) : AbstractListener(dialog) {
    override fun actionPerformed(e: ActionEvent) {
        val currentSourceName = dialog.dbBox?.selectedItem

        for (dataSource in DataSourceUtil.dataSources.values) {
            if (dataSource.dataSourceName == currentSourceName) {
                dataSource.isBase = "true"
            } else {
                dataSource.isBase = "false"
            }
        }

        dialog.baseChx?.isSelected = true
        dialog.devChx?.isSelected = false

        //将新增的数据源保存到数据源的配置文件中（home/ierp/bin/prop.xml）
        DataSourceUtil.writeDataSource(dialog)
    }
}