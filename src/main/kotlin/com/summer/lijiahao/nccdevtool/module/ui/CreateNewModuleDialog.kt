package com.summer.lijiahao.nccdevtool.module.ui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.base.util.ConfigureFileUtil
import com.summer.lijiahao.nccdevtool.devconfig.util.libraries.LibrariesUtil
import com.summer.lijiahao.nccdevtool.module.NCCModuleType
import org.apache.commons.lang.StringUtils
import java.io.File
import java.text.MessageFormat
import javax.swing.*

class CreateNewModuleDialog(event: AnActionEvent) : DialogWrapper(true) {

    var event: AnActionEvent? = null
    var modulePath: String? = null
    var contentPane: JPanel? = null
    var location: JTextField? = null
    var locationFileChooseBtn: JButton? = null
    var moduleNameField: JTextField? = null
    var ncModuleNameField: JTextField? = null

    init {
        this.event = event
        modulePath = if (event.getData(CommonDataKeys.VIRTUAL_FILE) == null) {
            event.project!!.basePath
        } else {
            event.getData(CommonDataKeys.VIRTUAL_FILE)!!.path
        }
        location!!.text = modulePath
        title = "创建新模块"
        init()
    }

    override fun createCenterPanel(): JComponent {

        // 保存路径按钮事件
        locationFileChooseBtn!!.addActionListener {
            val fileChooser = JFileChooser(modulePath)
            fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            val flag = fileChooser.showOpenDialog(null)
            if (flag == JFileChooser.APPROVE_OPTION) {
                location!!.text = fileChooser.selectedFile.absolutePath
            }
        }

        isModal = true
        return contentPane!!
    }

    override fun doOKAction() {
        val moduleNameText = moduleNameField!!.text
        val ncModuleNameText = ncModuleNameField!!.text
        if (StringUtils.isBlank(moduleNameText)) {
            Messages.showErrorDialog("Please set module name!", "Error")
            return
        }

        if (StringUtils.isBlank(ncModuleNameText)) {
            Messages.showErrorDialog("Please set NC Module name!", "Error")
            return
        }
        val locationText = location!!.text
        if (StringUtils.isBlank(locationText)) {
            Messages.showErrorDialog("Please set Module file location !", "Error")
            return
        }

        val project = event!!.project

        try {
            //创建module
            val builder = NCCModuleType().createModuleBuilder()
            val modulePath = locationText + File.separator + moduleNameText
            builder.moduleFilePath = modulePath + File.separator + moduleNameText + ".iml"
            builder.contentEntryPath = modulePath
            builder.name = moduleNameText
            val module = builder.commitModule(project!!, null)

            //输出配置文件
            val util = ConfigureFileUtil()
            val meatPath = modulePath + File.separator + "META-INF"
            File(meatPath).mkdirs()
            val file = File(meatPath + File.separator + "module.xml")
            val template = util.readTemplate("module.xml")
            val content = MessageFormat.format(template, ncModuleNameText)
            util.outFile(file, content, "gb2312", false)

            //设置类路径
            LibrariesUtil.setModuleLibrary(project, module!!)
        } catch (e: Exception) {
            Messages.showErrorDialog(e.message, "Error")
        }

        super.doOKAction()
    }
}