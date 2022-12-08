package com.summer.lijiahao.nccdevtool.component.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.component.ui.CreateNewComponentDialog
import java.io.File

class CreateNCComponentAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        //判断选中的是不是nc module
        val isNcModule: Boolean = isNCModule(event)
        if (!isNcModule) {
            Messages.showErrorDialog("Please select nc module root!", "Error")
            return
        }

        val dialog = CreateNewComponentDialog(event)
        dialog.setSize(900, 300)
        dialog.show()
    }

    override fun update(e: AnActionEvent) {
        val file = getSelectFile(e)
        val module = getSelectModule(e)

        val flag =
            file != null && module != null && module.name == file.name &&
                    File(file.path + File.separator + "META-INF" + File.separator + "module.xml").exists()

        e.presentation.isEnabledAndVisible = flag
    }
}