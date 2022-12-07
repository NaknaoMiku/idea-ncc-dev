package com.summer.lijiahao.nccdevtool.devconfig.listener

import com.summer.lijiahao.nccdevtool.devconfig.listener.base.AbstractItemListener
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DatabaseDriverUtil
import java.awt.event.ItemEvent
import javax.swing.DefaultComboBoxModel

class SelectDataTypeListener(dialog: NCCloudDevConfigDialog) : AbstractItemListener(dialog) {
    override fun itemStateChanged(e: ItemEvent) {
        val currentDataType = dialog.dbTypeBox?.selectedItem
        val databaseDriverTypes = DatabaseDriverUtil.databaseTypeMap[currentDataType]!!

        val items = databaseDriverTypes.toTypedArray()
        dialog.driverBox?.model = DefaultComboBoxModel(items)
    }
}