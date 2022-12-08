package com.summer.lijiahao.nccdevtool.module.util

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.util.Pair
import com.summer.lijiahao.nccdevtool.base.util.ProjectManager
import com.summer.lijiahao.nccdevtool.devconfig.util.libraries.LibrariesUtil
import com.summer.lijiahao.nccdevtool.module.NCCModuleType
import java.io.File

class ModuleUtil {

    private val MODULE_TYPE_JAVA = 0
    private val MODULE_TYPE_NC = 1
    private val MODULE_TYPE_MAVEN = 2

    fun coverToModule(project: Project, filePath: String) {
        val file = File(filePath)
        var moduleFileName: String = getModuleFileName(file)

        //0 是常规java模块，1是nc模块，2是maven模块
        var moduleType: Int = MODULE_TYPE_JAVA
        if (moduleFileName.startsWith("nc_")) {
            moduleFileName = moduleFileName.substring(3)
            moduleType = MODULE_TYPE_NC
        } else if (moduleFileName.startsWith("maven_")) {
            moduleFileName = moduleFileName.substring(6)
            moduleType = MODULE_TYPE_MAVEN
        }
        val libraries: Array<Library> = LibrariesUtil.getProjectLibraries(project)
        val module: Module? = ProjectManager().getModule(project, file.name)
        module ?: let {
            //创建module
            val builder = NCCModuleType().createModuleBuilder()
            builder.moduleFilePath = filePath + File.separator + moduleFileName
            builder.contentEntryPath = filePath
            builder.name = file.name
            builder.mySourcePaths = getSourcePathList(moduleType, filePath)
            builder.libraries = libraries
            builder.commitModule(project, null)
        }
    }

    /**
     * 扫描source目录
     *
     * @param moduleType
     * @param modulePath
     * @return
     */
    private fun getSourcePathList(moduleType: Int, modulePath: String): MutableList<Pair<String, String>> {
        var list: MutableList<Pair<String, String>> = mutableListOf()
        when (moduleType) {
            MODULE_TYPE_NC -> list = scanNCSourcePath(modulePath)
            MODULE_TYPE_MAVEN -> list.add(Pair("$modulePath/src/main/java", ""))
            else -> list.add(Pair("$modulePath/src", ""))
        }
        return list
    }


    private fun scanNCSourcePath(modulePath: String): MutableList<Pair<String, String>> {
        val list: MutableList<Pair<String, String>> = mutableListOf()
        val moduleFile = File(modulePath)
        for (componentFile in moduleFile.listFiles()) {
            if (componentFile.isFile) {
                continue
            }
            val file = File(componentFile.path + File.separator + "component.xml")
            //如果模块下边组件文件存在
            if (file.exists()) {
                val srcFile = File(file.parent + File.separator + "src")
                if (srcFile.exists()) {
                    for (f in srcFile.listFiles()) {
                        if (f.name == "client" || f.name == "public" || f.name == "private") {
                            list.add(Pair(f.path, ""))
                        }
                    }
                }
            }
        }
        return list
    }


    private fun getModuleFileName(file: File): String {
        var moduleName = file.name
        val path = file.path
        val ncModulePath = path + File.separator + "META-INF" + File.separator + "module.xml"
        try {
            val ncModuleFile = File(ncModulePath)
            moduleName = "maven_" + file.name
            if (ncModuleFile.exists()) {
                moduleName = "nc_" + file.name
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        moduleName += ".iml"
        return moduleName
    }

}