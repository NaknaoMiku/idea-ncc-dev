package com.summer.lijiahao.nccdevtool.devconfig.listener

import com.summer.lijiahao.nccdevtool.devconfig.listener.base.AbstractListener
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSource
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSourceUtil
import java.awt.event.ActionEvent
import javax.swing.DefaultComboBoxModel

class SetDevDataSourceListener(dialog: NCCloudDevConfigDialog) : AbstractListener(dialog) {
    override fun actionPerformed(e: ActionEvent) {
        val dataSourceName = dialog.dbBox?.selectedItem
        if ("design" != dataSourceName) {
            val currentDataSource: DataSource = DataSourceUtil.dataSources[dataSourceName]!!

            //复制一个数据源对象
            val designDataSource = DataSource(
                "design",
                currentDataSource.oidMark,
                currentDataSource.databaseUrl,
                currentDataSource.user,
                currentDataSource.password,
                currentDataSource.driverClassName,
                currentDataSource.databaseType,
                "false"
            )

            DataSourceUtil.dataSources["design"] = designDataSource
            dialog.dbBox?.let {
                val items = DataSourceUtil.dataSources.keys.toTypedArray()
                it.model = DefaultComboBoxModel(items)
                it.selectedItem = "design"
                dialog.devChx?.isSelected = true
            }

            //将新增的数据源保存到数据源的配置文件中（home/ierp/bin/prop.xml）
            DataSourceUtil.writeDataSource(dialog)
        }
    }
}