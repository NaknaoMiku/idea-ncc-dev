package com.summer.lijiahao.nccdevtool.module.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.summer.lijiahao.nccdevtool.base.action.AbstractAnAction
import com.summer.lijiahao.nccdevtool.base.util.ConfigureFileUtil
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.File
import java.text.MessageFormat

class ConvertEclipseProjectAction : AbstractAnAction() {
    override fun doAction(event: AnActionEvent) {
        val file = getSelectFileArr(event)
        file?.let {
            for (f in it) {
                try {
                    createProjectXml(f)
                    createClassPathXml(f)
                } catch (e: Exception) {
                    Messages.showErrorDialog(e.message, "wrong")
                    break
                }
            }
        }
        Messages.showInfoMessage("finish", "success")
    }


    private fun createProjectXml(file: VirtualFile) {
        val util = ConfigureFileUtil()
        val template: String = util.readTemplate(".project")

        val moduleXml = file.path + File.separator + "META-INF/module.xml"
        val reader = SAXReader()
        // 解析xml得到Document
        val doc: Document = reader.read(File(moduleXml))
        val moduleName = doc.rootElement.attribute("name").text
        if (moduleName.isNullOrBlank()) {
            throw Exception("未获取到模块编码")
        }

        val content = MessageFormat.format(template, moduleName)

        util.outFile(File(file.path + File.separator + ".project"), content, "utf-8", false)
    }

    private fun createClassPathXml(file: VirtualFile) {
        val util = ConfigureFileUtil()
        val template: String = util.readTemplate(".classpath")
        val classPathXml = DocumentHelper.parseText(template)
        val classpathElement = classPathXml.rootElement

        val srcDirs = arrayOf("src/client", "src/private", "src/public", "src/test", "resources")
        val modulePath = file.path + File.separator
        val componentXml = file.path + File.separator + "manifest.xml"
        val reader = SAXReader()
        val componentDoc: Document = reader.read(File(componentXml))
        val root = componentDoc.rootElement
        val businessComponets = root.elements("BusinessComponet")

        for (businessComponet in businessComponets) {
            val componetName = businessComponet.attribute("name").text

            for (srcDir in srcDirs) {
                if (File(modulePath + componetName + File.separator + srcDir).exists()) {
                    val classpathentry: Element = classpathElement.addElement("classpathentry")
                    classpathentry.addAttribute("kind", "src")
                    classpathentry.addAttribute("output", "$componetName/classes")
                    classpathentry.addAttribute("path", "$componetName/$srcDir")
                }
            }
        }

        val mustDirs = arrayOf(
            "org.eclipse.jdt.launching.JRE_CONTAINER",
            "com.yonyou.studio.udt.core.container/Ant_Library",
            "com.yonyou.studio.udt.core.container/Product_Common_Library",
            "com.yonyou.studio.udt.core.container/Middleware_Library",
            "com.yonyou.studio.udt.core.container/Framework_Library",
            "com.yonyou.studio.udt.core.container/Module_Public_Library",
            "com.yonyou.studio.udt.core.container/Module_Client_Library",
            "com.yonyou.studio.udt.core.container/Module_Private_Library",
            "com.yonyou.studio.udt.core.container/Module_Lang_Library",
            "com.yonyou.studio.udt.core.container/Generated_EJB",
            "com.yonyou.studio.udt.core.container/NCCloud_Library"
        )
        for (mustDir in mustDirs) {
            val classpathentry: Element = classpathElement.addElement("classpathentry")
            classpathentry.addAttribute("kind", "con")
            classpathentry.addAttribute("path", mustDir)
        }
        val classpathentry: Element = classpathElement.addElement("classpathentry")
        classpathentry.addAttribute("kind", "output")
        classpathentry.addAttribute("path", "bin")

        val content = MessageFormat.format(classpathElement.asXML())

        util.outFile(File(file.path + File.separator + ".classpath"), content, "utf-8", false)
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