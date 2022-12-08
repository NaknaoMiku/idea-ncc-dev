package com.summer.lijiahao.nccdevtool.patch.listener.base

import com.summer.lijiahao.nccdevtool.patch.ui.PatcherDialog
import java.awt.event.ActionListener

abstract class AbstractListener(dialog: PatcherDialog) : ActionListener {
    var dialog: PatcherDialog

    init {
        this.dialog = dialog
    }
}