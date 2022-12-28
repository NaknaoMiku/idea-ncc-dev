package com.summer.lijiahao.nccdevtool.devconfig.util.datasource

import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import org.dom4j.Document
import org.dom4j.Node
import org.dom4j.io.SAXReader
import java.io.File
import javax.swing.DefaultComboBoxModel

class DatabaseDriverUtil {
    companion object {
        private const val PATH = "/ierp/bin/dbdriverset.xml"
        var databaseTypeMap: LinkedHashMap<String, LinkedHashSet<String>> = linkedMapOf()

        fun getInitDataSourceType(dialog: NCCloudDevConfigDialog, type: String): DatabaseType? {
            val homePath = dialog.homeText!!.text

            if (homePath.isNotBlank()) {
                val filename = homePath + PATH
                val databaseNodes: MutableList<Node> = analysisXML(filename)
                val databases: LinkedHashMap<String, DatabaseType> = linkedMapOf()

                for (databaseNode in databaseNodes) {
                    val databaseType = databaseNode.selectSingleNode("./databaseType").text

                    val driverNodes: MutableList<Node> = databaseNode.selectNodes("./driver")
                    val drivers: LinkedHashMap<String, DatabaseDriver> = linkedMapOf()
                    for (driverNode in driverNodes) {
                        val driverType = databaseNode.selectSingleNode("./driver/driverType").text
                        val driverLib = databaseNode.selectSingleNode("./driver/driverLib").text
                        val driverClass = databaseNode.selectSingleNode("./driver/driverClass").text
                        val driverUrl = databaseNode.selectSingleNode("./driver/driverUrl").text
                        val maxCon = databaseNode.selectSingleNode("./driver/maxCon").text
                        val minCon = databaseNode.selectSingleNode("./driver/minCon").text
                        val providerClass = databaseNode.selectSingleNode("./driver/providerClass").text
                        drivers[driverType] =
                            DatabaseDriver(driverType, driverLib, driverClass, driverUrl, maxCon, minCon, providerClass)
                    }
                    databases[databaseType] = DatabaseType(databaseType, drivers)
                }

                return databases[type]
            } else {
                return null
            }


        }

        /**
         * 获取所有支持的数据库类型
         */
        fun getAllInitDatabaseType(dialog: NCCloudDevConfigDialog, defaultValue: String) {
            val homePath = dialog.homeText!!.text
            if (homePath.isNotBlank()) {
                val filename = homePath + PATH
                val databaseNodes: MutableList<Node> = analysisXML(filename)
                val databaseTypes: LinkedHashSet<String> = linkedSetOf()

                for (databaseNode in databaseNodes) {
                    val databaseType = databaseNode.selectSingleNode("./databaseType").text
                    databaseTypes.add(databaseType)
                }

                val items = databaseTypes.toTypedArray()
                dialog.dbTypeBox?.model = DefaultComboBoxModel(items)
                dialog.dbTypeBox?.selectedItem = defaultValue
            }
        }

        /**
         * 获取所有支持的数据库的驱动类型
         */
        fun getAllInitDatabaseDriverType(dialog: NCCloudDevConfigDialog, databaseType: String) {
            if (databaseTypeMap.isEmpty()) {
                val homePath = dialog.homeText!!.text
                if (homePath.isNotBlank()) {
                    val filename = homePath + PATH
                    val databaseNodes: MutableList<Node> = analysisXML(filename)

                    for (databaseNode in databaseNodes) {
                        val databaseType = databaseNode.selectSingleNode("./databaseType").text
                        val driverNodes: MutableList<Node> = databaseNode.selectNodes("./driver")
                        val databaseDriverTypes: LinkedHashSet<String> = linkedSetOf()
                        for (driverNode in driverNodes) {
                            val driverType = driverNode.selectSingleNode("./driverType").text
                            databaseDriverTypes.add(driverType)
                        }

                        databaseTypeMap[databaseType] = databaseDriverTypes
                    }


                }
            }

            val drivers: LinkedHashSet<String>? = databaseTypeMap[databaseType]
            drivers?.let {
                val items = drivers.toTypedArray()
                dialog.driverBox?.model = DefaultComboBoxModel(items)
            }
        }

        private fun analysisXML(filePath: String): MutableList<Node> {
            //解析数据源
            val reader = SAXReader()
            // 解析xml得到Document
            val doc: Document = reader.read(File(filePath))
            return doc.selectNodes("//database")
        }
    }
}