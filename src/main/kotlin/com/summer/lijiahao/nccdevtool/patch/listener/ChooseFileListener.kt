package com.summer.lijiahao.nccdevtool.patch.listener

import com.summer.lijiahao.nccdevtool.patch.listener.base.AbstractListener
import com.summer.lijiahao.nccdevtool.patch.ui.PatcherDialog
import java.awt.event.ActionEvent
import javax.swing.JFileChooser

class ChooseFileListener(dialog: PatcherDialog) : AbstractListener(dialog) {
    override fun actionPerformed(e: ActionEvent) {
        val userDir = System.getProperty("user.home")
        val fileChooser = JFileChooser(userDir)
        fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        val flag = fileChooser.showOpenDialog(null)
        if (flag == JFileChooser.APPROVE_OPTION) {
            dialog.savePath?.setText(fileChooser.selectedFile.absolutePath)
        }
    }
}