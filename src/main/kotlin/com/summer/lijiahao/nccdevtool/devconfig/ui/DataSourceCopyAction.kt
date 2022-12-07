package com.summer.lijiahao.nccdevtool.devconfig.ui

class DataSourceCopyAction {
    companion object {
        fun show(dialog: NCCloudDevConfigDialog) {
            val copyDialog = DataSourceCopyDialog(dialog)
            copyDialog.show()
        }
    }
}