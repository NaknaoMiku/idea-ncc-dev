package com.summer.lijiahao.nccdevtool.component.ui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.summer.lijiahao.nccdevtool.base.util.ConfigureFileUtil
import com.summer.lijiahao.nccdevtool.base.util.ProjectManager
import org.apache.commons.lang.StringUtils
import java.io.File
import java.text.MessageFormat
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class CreateNewComponentDialog(event: AnActionEvent) : DialogWrapper(true) {
    private var event: AnActionEvent
    private var contentPane: JPanel? = null
    private var displayText: JTextField? = null
    private var nameText: JTextField? = null

    init {
        this.event = event
        title = "创建新组件"
        init()
    }

    override fun createCenterPanel(): JComponent {
        isModal = true
        return contentPane!!
    }

    override fun doOKAction() {
        try {
            val name = nameText!!.text
            if (StringUtils.isBlank(name)) {
                Messages.showErrorDialog("Please set componet name!", "Error")
                return
            }
            val display = displayText!!.text
            if (StringUtils.isBlank(display)) {
                Messages.showErrorDialog("Please set componet display!", "Error")
                return
            }
            if (!name.matches("[a-zA-Z]+".toRegex())) {
                Messages.showErrorDialog("The name must be using letter only!", "Error")
                return
            }
            if (!display.matches("[a-zA-Z]+".toRegex())) {
                Messages.showErrorDialog("The display must be using letter only!", "Error")
                return
            }
            val modulePath = event!!.getData(CommonDataKeys.VIRTUAL_FILE)!!.path
            val file = File(modulePath + File.separator + name)
            if (file.exists()) {
                Messages.showErrorDialog("Componet is exists! please replace name !", "Error")
            }
            //创建目录
            val dirs = arrayOf(
                "META-INF", "METADATA", "resources", "src/public", "src/private", "src/client",
                "script/conf", "config"
            )
            for (dir in dirs) {
                val path = file.path + File.separator + dir
                File(path).mkdirs()
            }

            //创建配置文件
            val util = ConfigureFileUtil()

            //创建compinent文件
            var template: String = util.readTemplate("component.xml")
            var content = MessageFormat.format(template, name, display)
            util.outFile(File(file.path + File.separator + "component.xml"), content, "utf-8", false)
            //创建manifset文件
            val manifest = File(modulePath + File.separator + "manifest.xml")
            var newManifest: String? = null
            if (manifest.exists()) {
                val oldManifest: String = util.readTemplate(manifest)
                template = util.readTemplate("BusinessComponet.xml")
                content = MessageFormat.format(template, name, display).replace("<Manifest>", "")
                newManifest = oldManifest.replace("</Manifest>", content)
            } else {
                template = util.readTemplate("manifest.xml")
                content = MessageFormat.format(template, name, display)
                newManifest = content
            }
            util.outFile(manifest, newManifest, "utf-8", false)

            //添加source目录
            val module: Module = ProjectManager().getModule(event.project, file.parentFile.name)
            val modifiableModel = ModuleRootManager.getInstance(module).modifiableModel
            val contentEntry = modifiableModel.contentEntries[0]
            for (str in dirs) {
                if (str.contains("src")) {
                    val sourceRoot = LocalFileSystem.getInstance()
                        .refreshAndFindFileByPath(FileUtil.toSystemIndependentName(file.path + File.separator + str))
                    contentEntry.addSourceFolder(sourceRoot!!, false)
                }
            }
            val applicationManager = ApplicationManager.getApplication()
            applicationManager.runWriteAction { modifiableModel.commit() }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.doOKAction()
    }
}