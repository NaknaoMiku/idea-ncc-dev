package com.summer.lijiahao.nccdevtool.devconfig.listener

import com.summer.lijiahao.nccdevtool.devconfig.listener.base.AbstractListener
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import com.summer.lijiahao.nccdevtool.devconfig.util.modules.ModulesUtil
import java.awt.event.ActionEvent

class SelectMustModuleListener(dialog: NCCloudDevConfigDialog) : AbstractListener(dialog) {
    override fun actionPerformed(e: ActionEvent) {

        ModulesUtil.setMustModules(dialog)
    }

}