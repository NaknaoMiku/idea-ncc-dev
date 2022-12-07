package com.summer.lijiahao.nccdevtool.devconfig.listener.base

import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import java.awt.event.ItemListener

abstract class AbstractItemListener(dialog: NCCloudDevConfigDialog) : ItemListener {
    var dialog: NCCloudDevConfigDialog

    init {
        this.dialog = dialog
    }
}