package com.summer.lijiahao.nccdevtool.devconfig.listener

import com.summer.lijiahao.nccdevtool.devconfig.listener.base.AbstractItemListener
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSource
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSourceUtil
import java.awt.event.ItemEvent

class SelectDataSourceListener(dialog: NCCloudDevConfigDialog) : AbstractItemListener(dialog) {
    override fun itemStateChanged(e: ItemEvent) {
        val currentDataSourceName = dialog.dbBox?.selectedItem
        currentDataSourceName?.let {
            val currentDataSource: DataSource = DataSourceUtil.dataSources[currentDataSourceName]!!
            DataSourceUtil.fillDataSource(dialog, currentDataSource)
        }
    }
}