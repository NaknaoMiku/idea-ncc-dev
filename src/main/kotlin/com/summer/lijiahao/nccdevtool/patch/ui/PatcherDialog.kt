package com.summer.lijiahao.nccdevtool.patch.ui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import com.summer.lijiahao.nccdevtool.patch.listener.ChooseFileListener
import com.summer.lijiahao.nccdevtool.patch.util.ExportPatcherUtil
import java.awt.Rectangle
import java.io.File
import javax.swing.*

class PatcherDialog(event: AnActionEvent) : DialogWrapper(true) {

    var event: AnActionEvent
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
        val data = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)!!
        fieldList = JBList<VirtualFile>(*data)
        fieldList?.let {
            it.setEmptyText("No file selected!")
            val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(it)
            filePanel = decorator.createPanel()

        }
    }

    override fun doOKAction() {

        // 条件校验
        if (patcherName!!.text.isNullOrEmpty()) {
            Messages.showErrorDialog("Please set patcher name!", "Error")
            return
        }
        if (savePath!!.text.isNullOrEmpty()) {
            Messages.showErrorDialog("Please select save path!", "Error")
            return
        }
        val model = fieldList!!.model
        if (model.size == 0) {
            Messages.showErrorDialog("Please select export file!", "Error")
            return
        }

        val exportPath = savePath!!.text
        val envSettingService: NCCloudEnvSettingService = NCCloudEnvSettingService.getInstance(event)
        envSettingService.lastPatcherPath = exportPath

        val srcFlag = srcFlagCheckBox!!.isSelected
        val cloudFlag = cloudFlagCheckBox!!.isSelected
        val deployFlag = deployCheck!!.isSelected

        // 设置当前进度值
        logPanel!!.isVisible = true
        progressBar!!.value = 0
        // 绘制百分比文本（进度条中间显示的百分数）
        progressBar!!.isStringPainted = true
        progressBar!!.addChangeListener {
            val dimension = progressBar!!.size
            val rect = Rectangle(0, 0, dimension.width, dimension.height)
            progressBar!!.paintImmediately(rect)
        }

        SwingUtilities.invokeLater {
            val util = ExportPatcherUtil(
                patcherName!!.text,
                serverName!!.text,
                exportPath,
                srcFlag,
                cloudFlag,
                deployFlag,
                event
            )
            try {
                util.exportPatcher(progressBar)
                var zipName: String = util.zipName
                zipName = if (zipName.isBlank()) {
                    "no files export , please build project , or select src retry !"
                } else {
                    "outFile : $zipName"
                }
                Messages.showInfoMessage("Success!\n$zipName", "Tips")
                super.doOKAction()
            } catch (e: Exception) {
                Messages.showErrorDialog(e.message, "Error")
            } finally {
                util.delete(File(util.exportPath))
                super.doOKAction()
            }
        }
    }
}