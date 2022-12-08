package com.summer.lijiahao.nccdevtool.extend.util

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.io.FileUtil
import com.summer.lijiahao.nccdevtool.base.util.BaseUtil
import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import org.apache.commons.lang.StringUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

class ExtendFileCopyUtil {

    companion object {
        /**
         * 项目的鉴权文件路径
         */
        val PROJECT_CONFIG_FILE_PATH = File.separator + "src" + File.separator + "client" + File.separator + "yyconfig"

        /**
         * 模块的鉴权文件路径
         */
        val HOME_CONFIG_FILE_PATH =
            (File.separator + "hotwebs" + File.separator + "nccloud" + File.separator + "WEB-INF" + File.separator
                    + "extend" + File.separator + "yyconfig")

        /**
         * 拷贝一个模块的鉴权文件到home中，拷贝方法就是将该模块鉴权文件目录下的所有xml文件拷贝到home中的鉴权文件目录下
         *
         * @param event
         * @throws Exception
         */
        fun copyExtendFile(event: AnActionEvent) {
            val module: Module = BaseUtil.getModule(event)
            val homePath: String = NCCloudEnvSettingService.getInstance(event).ncHomePath
            if (StringUtils.isBlank(homePath)) {
                Messages.showMessageDialog("请先设置NC Home", "Error", Messages.getErrorIcon())
                return
            }
            val contentRoots = ModuleRootManager.getInstance(module).contentRoots
            if (contentRoots.isEmpty()) {
                Messages.showMessageDialog("请先设置NC Home", "Error", Messages.getErrorIcon())
                return
            }
            val modulePath = contentRoots[0].path
            val file = File(modulePath)
            if (file.isDirectory) {
                val list = file.list()
                for (com in list) {
                    if (!com.endsWith(".xml") && !com.endsWith(".iml") && com != "META-INF") {
                        copyDir(
                            contentRoots[0].path + File.separator  + com + PROJECT_CONFIG_FILE_PATH,
                            homePath + HOME_CONFIG_FILE_PATH
                        )
                    }
                }
            }
        }

        /**
         * 只拷贝选中的模块的upm文件到home
         *
         * @param event
         */
        fun copyUpmFile(event: AnActionEvent) {
            val homePath: String = NCCloudEnvSettingService.getInstance(event).ncHomePath
            val module = BaseUtil.getModule(event)
            val ncModuleName: String? = getNCModuleName(module)
            val fileUrls: Set<String>
            val contentRoots = ModuleRootManager.getInstance(module).contentRoots
            if (contentRoots.isEmpty()) {
                return
            }
            // 目标路径，但是缺少文件名字
            val toPath = (homePath + File.separator + "modules" + File.separator + ncModuleName + File.separator
                    + "META-INF" + File.separator)
            // 获取某个模块下所有的upm文件
            fileUrls = getFileUrl(contentRoots[0].path)
            val errorList: MutableList<String> = ArrayList()
            for (fileUrl in fileUrls) {
                val file = File(fileUrl)
                try {
                    // 逐个拷贝到home
                    FileUtil.copy(file, File(toPath + file.name))
                } catch (ignored: IOException) {
                    errorList.add(file.name)
                }
            }
            if (!errorList.isEmpty()) {
                Messages.showMessageDialog("文件" + errorList + "拷贝出错", "Error", Messages.getErrorIcon())
            }
        }

        /**
         * 将某个文件下下的所有xml文件拷贝到另一个文件夹
         *
         * @param fromDirPath
         * @param toDirPath
         * @throws IOException
         */
        private fun copyDir(fromDirPath: String, toDirPath: String) {
            val file = File(fromDirPath)
            val subFilePaths = file.list() ?: return
            if (!File(toDirPath).exists()) {
                if (!File(toDirPath).mkdir()) {
                    Messages.showMessageDialog(
                        "Home所在的目录不存在或者，你没有操作home所在目录的权限",
                        "Error",
                        Messages.getErrorIcon()
                    )
                    return
                }
            }
            // 递归拷贝
            for (subFilePath in subFilePaths) {
                val file0 = File(fromDirPath + File.separator + subFilePath)
                if (file0.isDirectory) {
                    copyDir(fromDirPath + File.separator + subFilePath, toDirPath + File.separator + subFilePath)
                } else if (file0.isFile) {
                    copyXmlFile(
                        fromDirPath + File.separator + subFilePath,
                        toDirPath + File.separator + subFilePath
                    )
                }
            }
        }

        /**
         * 复制xml文件
         *
         * @param fromFilePath
         * @param newFilePath
         * @throws IOException
         */
        private fun copyXmlFile(fromFilePath: String, newFilePath: String) {
            if (!fromFilePath.endsWith(".xml")) {
                return
            }
            val fromFile = File(fromFilePath)
            val toFile = File(newFilePath)
            FileUtil.copy(fromFile, toFile)
        }

        /**
         * 获取nc模块名称
         *
         * @param module
         * @return
         */
        private fun getNCModuleName(module: Module): String? {
            var ncModuleName: String? = null
            val contentRoots = ModuleRootManager.getInstance(module).contentRoots
            if (contentRoots.isEmpty()) {
                return null
            }
            val modulePath = contentRoots[0].path
            try {
                val file = File(modulePath + File.separator + "META-INF" + File.separator + "module.xml")
                if (file.exists()) {
                    val `in`: InputStream = FileInputStream(file)
                    val factory = DocumentBuilderFactory.newInstance()
                    val builder = factory.newDocumentBuilder()
                    val doc = builder.parse(`in`)
                    val root = doc.documentElement
                    ncModuleName = root.getAttribute("name")
                }
            } catch (e: Exception) {

            }
            return ncModuleName
        }

        /**
         * 递归路径获取可导出的文件
         *
         * @param filePath
         */
        private fun getFileUrl(filePath: String): Set<String> {
            val fileUrlSet: MutableSet<String> = HashSet()
            val file = File(filePath)
            if (file.isDirectory) {
                val childrenFile = file.listFiles() ?: return fileUrlSet
                for (childFile in childrenFile) {
                    fileUrlSet.addAll(getFileUrl(childFile.path)!!)
                }
            } else {
                if ((filePath.endsWith(".rest") || filePath.endsWith(".upm")) && File(filePath).parent.endsWith("META-INF")) {
                    fileUrlSet.add(filePath)
                }
            }
            return fileUrlSet
        }
    }
}