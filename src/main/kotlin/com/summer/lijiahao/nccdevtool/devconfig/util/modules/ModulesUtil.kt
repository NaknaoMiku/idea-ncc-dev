package com.summer.lijiahao.nccdevtool.devconfig.util.modules

import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import javax.swing.table.DefaultTableModel
import javax.xml.parsers.DocumentBuilderFactory

class ModulesUtil {
    companion object {
        val defaultMustModules: LinkedHashSet<String> = linkedSetOf(
            "baseapp", "iuap", "opm", "platform", "pubapp", "pubapputil",

            "riaaam", "riaadp", "riaam", "riacc", "riadc", "riamm",
            "riaorg", "riart", "riasm", "riawf",

            "uapbd", "uapbs", "uapec", "uapfw", "uapfwjca", "uapmw",
            "uapportal", "uapsc", "uapss",

            "workbench", "imag", "graphic_report", "sscrp"
        )

        private var allModules: MutableList<String> = mutableListOf()

        fun initAllModules(dialog: NCCloudDevConfigDialog, selectedModel: DefaultTableModel) {
            val homePath = dialog.homeText!!.text

            if (homePath.isNotBlank()) {
                getAllModules(homePath)

                //已选择模块
                val selectedModuleStr: String = NCCloudEnvSettingService.getInstance(dialog.event).ex_modules
                val selectedModules: LinkedHashSet<String> = linkedSetOf()
                val strings: Array<String> =
                    selectedModuleStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                selectedModules.addAll(listOf(*strings))

                //在赋值前先移除之前的元素
                selectedModel.dataVector.removeAllElements()

                val allModuleName: LinkedHashMap<String, String> = ModulesNameUtil().getModuleNameFromXML()
                for ((index, module) in allModules.withIndex()) {

                    val isSelected = selectedModules.contains(module)
                    val vm: Vector<Any> = Vector<Any>()
                    vm.add(index)
                    vm.add(!isSelected)
                    vm.add(module)
                    vm.add(allModuleName[module])
                    selectedModel.addRow(vm)
                }
            }
        }

        /**
         * 设置必选模块
         */
        fun setMustModules(dialog: NCCloudDevConfigDialog) {
            dialog.selTable?.let {
                val rowCount: Int = it.rowCount
                for (i in 0 until rowCount) {
                    val moduleName = it.model.getValueAt(i, 2)
                    if (defaultMustModules.contains(moduleName)) {
                        it.model.setValueAt(true, i, 1)
                    } else {
                        it.model.setValueAt(false, i, 1)
                    }
                }
            }
        }

        /**
         * 设置模块全选
         */
        fun setSelectAllModule(dialog: NCCloudDevConfigDialog) {
            dialog.selTable?.let {
                val rowCount: Int = it.rowCount
                for (i in 0 until rowCount) {
                    it.model.setValueAt(true, i, 1)
                }
            }
        }

        /**
         * 设置模块全消
         */
        fun setCancelAllModule(dialog: NCCloudDevConfigDialog) {
            dialog.selTable?.let {
                val rowCount: Int = it.rowCount
                for (i in 0 until rowCount) {
                    it.model.setValueAt(false, i, 1)
                }
            }
        }

        /**
         * 将勾选的模块写入到配置文件中
         */
        fun writeModuleToConfig(dialog: NCCloudDevConfigDialog) {
            dialog.selTable?.let {
                val rowCount: Int = it.rowCount
                val selectedModuleNames: MutableList<String> = mutableListOf()
                for (i in 0 until rowCount) {
                    val isSelected = if (it.model.getValueAt(i, 1) != null) {
                        it.model.getValueAt(i, 1).toString()
                    } else {
                        "false"
                    }
                    if (isSelected != "true") {
                        val moduleName = it.model.getValueAt(i, 2)
                        moduleName?.let { selectedModuleNames.add(moduleName as String) }
                    }
                }

                var selectedModuleStr = ""
                for (moduleName in selectedModuleNames) {
                    selectedModuleStr = "$selectedModuleStr$moduleName,"
                }
                if (selectedModuleStr.isNotEmpty()) {
                    selectedModuleStr = selectedModuleStr.substring(0, selectedModuleStr.length - 1)
                }

                NCCloudEnvSettingService.getInstance(dialog.event).ex_modules = selectedModuleStr
            }
        }

        private fun getAllModules(homePath: String) {
            allModules = mutableListOf() //先清空，暂时没有用
            if (homePath.isNotBlank()) {
                //扫描所有module
                val moduleFile = File(homePath + File.separator + "modules")
                val moduleList: MutableList<String> = mutableListOf()
                if (moduleFile.exists()) {
                    val moduleArr = moduleFile.listFiles()
                    if (moduleArr != null) {
                        for (module in moduleArr) {
                            val moduleName: String? = getNCModuleName(module)
                            if (!moduleName.isNullOrEmpty()) { //判定是nc模块
                                moduleList.add(module.name)
                            }
                        }
                    }
                }
                //排序
                moduleList.sort()
                allModules = moduleList
            }
        }

        private fun getNCModuleName(module: File): String? {
            var ncModuleName: String? = null
            val moduleFilePath = module.path + File.separator + "META-INF" + File.separator + "module.xml"
            try {
                val file = File(moduleFilePath)
                if (file.exists()) {
                    val inputStream: InputStream = FileInputStream(file)
                    val factory = DocumentBuilderFactory.newInstance()
                    val builder = factory.newDocumentBuilder()
                    val doc = builder.parse(inputStream)
                    val root = doc.documentElement
                    ncModuleName = root.getAttribute("name")
                }
            } catch (e: Exception) {
                //抛错就认为不是nc项目
            }
            return ncModuleName
        }
    }
}