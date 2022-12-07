package com.summer.lijiahao.nccdevtool.devconfig.listener

import com.summer.lijiahao.nccdevtool.devconfig.listener.base.AbstractListener
import com.summer.lijiahao.nccdevtool.devconfig.ui.DataSourceCopyAction
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import java.awt.event.ActionEvent

class CopyDataSourceListener(dialog: NCCloudDevConfigDialog) : AbstractListener(dialog) {
    override fun actionPerformed(e: ActionEvent) {
        DataSourceCopyAction.show(dialog)
    }
}