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

        fun initAllModules(dialog: NCCloudDevConfigDialog, selectedModel: DefaultTableModel) {
            val homePath = dialog.homeText!!.text

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

                //必选模块
                val mustModuleStr: String = NCCloudEnvSettingService.getInstance(dialog.event).must_modules
                var mustModules: LinkedHashSet<String> = linkedSetOf()
                if (mustModuleStr.isEmpty()) {
                    mustModules = defaultMustModules
                } else {
                    val strings = mustModuleStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    mustModules.addAll(listOf(*strings))
                }

                //已选择模块
                val selectedModuleStr: String = NCCloudEnvSettingService.getInstance(dialog.event).ex_modules
                val selectedModules: LinkedHashSet<String> = linkedSetOf()
                if (mustModuleStr.isEmpty()) {
                    val strings: Array<String> =
                        selectedModuleStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    selectedModules.addAll(listOf(*strings))
                }

                for ((index,module) in moduleList.withIndex()){

                    val isSelected = selectedModules.contains(module)
                    val vm: Vector<Any> = Vector<Any>()
                    vm.add(index)
                    vm.add(isSelected)
                    vm.add(module)
                    selectedModel.addRow(vm)
                }
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