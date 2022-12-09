package com.summer.lijiahao.nccdevtool.devconfig.util.datasource

import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import nc.uap.plugin.studio.ui.preference.rsa.Encode
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.Node
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.FileOutputStream
import javax.swing.DefaultComboBoxModel

class DataSourceUtil {

    companion object {

        var dataSources: LinkedHashMap<String, DataSource> = linkedMapOf()

        /**
         * 初始化数据源
         */
        fun initDataSource(dialog: NCCloudDevConfigDialog) {
            try {


                val homePath = dialog.homeText?.text

                if (!homePath.isNullOrEmpty()) {
                    dataSources = linkedMapOf()
                    //数据源列表
                    val filename = "$homePath/ierp/bin/prop.xml"
                    //解析数据源
                    val reader = SAXReader()
                    // 解析xml得到Document
                    val doc: Document = reader.read(File(filename))
                    val dataSourceNodes: MutableList<Node> = doc.selectNodes("//dataSource")
                    for (dataSourceNode in dataSourceNodes) {
                        val dataSourceName = dataSourceNode.selectSingleNode("./dataSourceName").text
                        val oidMark = dataSourceNode.selectSingleNode("./oidMark").text
                        val databaseUrl = dataSourceNode.selectSingleNode("./databaseUrl").text
                        val user = dataSourceNode.selectSingleNode("./user").text


                        var password = dataSourceNode.selectSingleNode("./password").text
                        password = DataSourcePwdAESEncodeUtil.decrypt(dialog, password)
                        val driverClassName = dataSourceNode.selectSingleNode("./driverClassName").text
                        val databaseType = dataSourceNode.selectSingleNode("./databaseType").text
                        val isBase = if (dataSourceNode.selectSingleNode("./isBase") != null) {
                            dataSourceNode.selectSingleNode("./isBase").text
                        } else {
                            "false"
                        }
                        val maxCon = dataSourceNode.selectSingleNode("./maxCon").text
                        val minCon = dataSourceNode.selectSingleNode("./minCon").text
                        val dataSourceClassName = dataSourceNode.selectSingleNode("./dataSourceClassName").text
                        val xaDataSourceClassName = dataSourceNode.selectSingleNode("./xaDataSourceClassName").text
                        val conIncrement = dataSourceNode.selectSingleNode("./conIncrement").text
                        val conInUse = dataSourceNode.selectSingleNode("./conInUse").text
                        val conIdle = dataSourceNode.selectSingleNode("./conIdle").text

                        dataSources[dataSourceName] = DataSource(
                            dataSourceName,
                            oidMark,
                            databaseUrl,
                            user,
                            password,
                            driverClassName,
                            databaseType,
                            isBase,
                            maxCon,
                            minCon,
                            dataSourceClassName,
                            xaDataSourceClassName,
                            conIncrement,
                            conInUse,
                            conIdle
                        )
                    }
                    //填充数据源信息
                    if (dataSources.isNotEmpty()) {
                        val items = dataSources.keys.toTypedArray()
                        val dataSource = if (dataSources.containsKey("design")) {
                            dataSources["design"]
                        } else {
                            dataSources[items[0]]
                        }
                        dialog.dbBox?.model = DefaultComboBoxModel(items)
                        dataSource?.let {
                            dialog.dbBox?.selectedItem = it.dataSourceName
                            dialog.hostText?.text = it.databaseUrl
                            dialog.oidText?.text = it.oidMark
                            dialog.userText?.text = it.user
                            dialog.dbNameText?.text = it.dbName
                            dialog.hostText?.text = it.url
                            dialog.portText?.text = it.port

                            dialog.devChx?.isSelected = it.dataSourceName == "design"
                            dialog.baseChx?.isSelected = it.isBase == "true"

                            dialog.pwdText?.text = it.password
                            DatabaseDriverUtil.getAllInitDatabaseType(dialog, it.databaseType)
                            DatabaseDriverUtil.getAllInitDatabaseDriverType(dialog, it.databaseType)
                        }
                    }
                }
            } catch (_: Exception) {

            }
        }

        /**
         * 将数据源更新到对应的配置文件
         * <dataSource>
         * 			<dataSourceName>design</dataSourceName>
         * 			<oidMark>OA</oidMark>
         * 			<databaseUrl>jdbc:oracle:thin:@127.0.0.1:1521/orcl</databaseUrl>
         * 			<user>cetc292</user>
         * 			<password>mlhgpdlcnofmeinl</password>
         * 			<driverClassName>oracle.jdbc.OracleDriver</driverClassName>
         * 			<databaseType>ORACLE11G</databaseType>
         * 			<maxCon>50</maxCon>
         * 			<minCon>1</minCon>
         * 			<dataSourceClassName>nc.bs.mw.ejb.xares.IerpDataSource</dataSourceClassName>
         * 			<xaDataSourceClassName>nc.bs.mw.ejb.xares.IerpXADataSource</xaDataSourceClassName>
         * 			<conIncrement>0</conIncrement>
         * 			<conInUse>0</conInUse>
         * 			<conIdle>0</conIdle>
         * 			<isBase>false</isBase>
         * 	</dataSource>
         */
        fun writeDataSource(dialog: NCCloudDevConfigDialog) {
            try {


                val homePath = dialog.homeText!!.text

                if (homePath.isNotBlank()) {
                    //数据源列表
                    val filename = "$homePath/ierp/bin/prop.xml"
                    //解析数据源
                    val reader = SAXReader()
                    // 解析xml得到Document
                    val doc: Document = reader.read(File(filename))

                    //将设置面板上的数据更新到数据源列表中
                    dialog.dbBox?.let {
                        val currentDataSourceName = it.selectedItem
                        val url = dialog.hostText?.text
                        val port = dialog.portText?.text
                        val databaseName = dialog.dbNameText?.text
                        val oidMark = dialog.oidText?.text
                        val user = dialog.userText?.text
                        val password = dialog.pwdText?.password?.let { it1 -> String(it1) }
                        val databaseType = dialog.dbTypeBox?.selectedItem
                        val driverBox = dialog.driverBox?.selectedItem

                        val configField: MutableList<String> = mutableListOf()
                        if (url.isNullOrBlank()) {
                            configField.add("主机名")
                        }
                        if (port.isNullOrBlank()) {
                            configField.add("端口")
                        }
                        if (databaseName.isNullOrBlank()) {
                            configField.add("DB/ODBC名称")
                        }
                        if (oidMark.isNullOrBlank()) {
                            configField.add("OID标识")
                        }
                        if (user.isNullOrBlank()) {
                            configField.add("用户名")
                        }
                        if (password.isNullOrBlank()) {
                            configField.add("密码")
                        }
                        if (databaseType == null || databaseType == "") {
                            configField.add("数据库类型")
                        }
                        if (driverBox == null || driverBox == "") {
                            configField.add("驱动类型")
                        }
                        if (configField.isNotEmpty()) {
                            Messages.showMessageDialog(
                                configField.toString() + "不能为空",
                                "Error",
                                Messages.getErrorIcon()
                            )
                            return
                        }
                        val currentDataSource = dataSources[currentDataSourceName]
                        currentDataSource ?: let {
                            Messages.showMessageDialog(
                                "未找到当前数据源",
                                "Error",
                                Messages.getErrorIcon()
                            )
                            return
                        }

                        currentDataSource.url = url!!
                        currentDataSource.port = port!!
                        currentDataSource.dbName = databaseName!!
                        currentDataSource.oidMark = oidMark!!
                        currentDataSource.user = user!!
                        currentDataSource.password = password!!

                        val databaseInfo = DatabaseDriverUtil.getInitDataSourceType(dialog, databaseType.toString())
                        databaseInfo ?: let {
                            Messages.showMessageDialog(
                                "未找到当前数据源的驱动类型",
                                "Error",
                                Messages.getErrorIcon()
                            )
                            return
                        }

                        val example: String = databaseInfo.driver[driverBox.toString()]?.driverUrl ?: ""
                        val databaseUrl = if (CreateDataUrlUtil.isJDBCUrl(example)) {
                            CreateDataUrlUtil.getJDBCUrl(example, databaseName, url, port)
                        } else {
                            CreateDataUrlUtil.getODBCUrl(example, databaseName)
                        }
                        currentDataSource.databaseUrl = databaseUrl
                    }


                    //将历史的数据源删掉，写入新的数据源
                    val rootElement: Element = doc.rootElement
                    val dataSourceElements: MutableList<Element> = rootElement.elements("dataSource")
                    for (dataSourceElement in dataSourceElements) {
                        rootElement.remove(dataSourceElement)
                    }
                    for (dataSource in dataSources.values) {
                        val dataSourceElement: Element = DocumentHelper.createElement("dataSource")
                        dataSourceElement.addElement("dataSourceName").text = dataSource.dataSourceName
                        dataSourceElement.addElement("oidMark").text = dataSource.oidMark
                        dataSourceElement.addElement("databaseUrl").text = dataSource.databaseUrl
                        dataSourceElement.addElement("user").text = dataSource.user
                        val password = Encode().encode(dataSource.password)
                        dataSourceElement.addElement("password").text = password
                        dataSourceElement.addElement("driverClassName").text = dataSource.driverClassName
                        dataSourceElement.addElement("databaseType").text = dataSource.databaseType
                        dataSourceElement.addElement("maxCon").text = dataSource.maxCon
                        dataSourceElement.addElement("minCon").text = dataSource.minCon
                        dataSourceElement.addElement("dataSourceClassName").text = dataSource.dataSourceClassName
                        dataSourceElement.addElement("xaDataSourceClassName").text = dataSource.xaDataSourceClassName
                        dataSourceElement.addElement("conIncrement").text = dataSource.conIncrement
                        dataSourceElement.addElement("conInUse").text = dataSource.conInUse
                        dataSourceElement.addElement("conIdle").text = dataSource.conIdle
                        dataSourceElement.addElement("isBase").text = dataSource.isBase
                        rootElement.add(dataSourceElement)
                    }

                    val format: OutputFormat = OutputFormat.createPrettyPrint()
                    val xmlWriter = XMLWriter(FileOutputStream(filename), format)
                    xmlWriter.write(doc)
                    xmlWriter.close()
                }
            } catch (_: Exception) {

            }
        }

        /**
         * 将选中的数据源显示在弹窗中
         */
        fun fillDataSource(dialog: NCCloudDevConfigDialog, currentDataSource: DataSource) {
            dialog.hostText?.text = currentDataSource.databaseUrl
            dialog.oidText?.text = currentDataSource.oidMark
            dialog.userText?.text = currentDataSource.user
            dialog.dbNameText?.text = currentDataSource.dbName
            dialog.hostText?.text = currentDataSource.url
            dialog.portText?.text = currentDataSource.port

            dialog.devChx?.isSelected = currentDataSource.dataSourceName == "design"
            dialog.baseChx?.isSelected = currentDataSource.isBase == "true"

            val password: String = currentDataSource.password
            dialog.pwdText?.text = password
        }
    }
}