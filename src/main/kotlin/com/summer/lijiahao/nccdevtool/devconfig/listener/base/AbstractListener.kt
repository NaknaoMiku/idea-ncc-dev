package com.summer.lijiahao.nccdevtool.devconfig.listener.base

import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import java.awt.event.ActionListener

abstract class AbstractListener(dialog: NCCloudDevConfigDialog) : ActionListener {
    var dialog: NCCloudDevConfigDialog

    init {
        this.dialog = dialog
    }
}