package com.summer.lijiahao.nccdevtool.patch.ui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.summer.lijiahao.nccdevtool.patch.listener.ChooseFileListener
import javax.swing.*

class PatcherDialog(event: AnActionEvent) : DialogWrapper(true) {

    var event: AnActionEvent? = null
    var contentPane: JPanel? = null
    var savePath: JTextField? = null
    var fileChooseBtn: JButton? = null
    var filePanel: JPanel? = null
    var patcherName: JTextField? = null
    var serverName: JTextField? = null
    var srcFlagCheckBox: JCheckBox? = null
    var progressBar: JProgressBar? = null
    var logPanel: JPanel? = null
    var cloudFlagCheckBox: JCheckBox? = null
    var deployCheck: JCheckBox? = null
    var fieldList: JBList<VirtualFile>? = null

    init {
        this.event = event
        title = "补丁导出"
        init()
    }


    override fun createCenterPanel(): JComponent {
        logPanel!!.isVisible = false
        patcherName!!.isEditable = true
        isModal = true
        initListener()

        return contentPane!!
    }

    private fun initListener() {
        fileChooseBtn?.addActionListener(ChooseFileListener(this))
    }

    private fun createUIComponents() {
        val data = event!!.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)!!
        fieldList = JBList<VirtualFile>(*data)
        fieldList?.let {
            it.setEmptyText("No file selected!")
            val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(it)
            filePanel = decorator.createPanel()

        }
    }
}