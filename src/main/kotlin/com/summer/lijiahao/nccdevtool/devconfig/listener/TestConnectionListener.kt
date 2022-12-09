package com.summer.lijiahao.nccdevtool.devconfig.listener

import com.intellij.openapi.ui.Messages
import com.jolbox.bonecp.BoneCP
import com.jolbox.bonecp.BoneCPConfig
import com.summer.lijiahao.nccdevtool.devconfig.listener.base.AbstractListener
import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSource
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSourceUtil
import java.awt.event.ActionEvent
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

class TestConnectionListener(dialog: NCCloudDevConfigDialog) : AbstractListener(dialog) {
    override fun actionPerformed(e: ActionEvent) {

        var isSuccess = false
        var msg: String? = ""
        try {
            if (DataSourceUtil.dataSources.isNotEmpty()) {
                val dataSourceName = dialog.dbBox?.selectedItem
                val currentDataSource: DataSource? = DataSourceUtil.dataSources[dataSourceName]
                currentDataSource?.let {
                    Class.forName(it.driverClassName, true, BoneCPConfig::class.java.classLoader)
                    val config = BoneCPConfig()
                    config.jdbcUrl = it.databaseUrl
                    config.username = it.user
                    config.password = dialog.pwdText?.password?.let { it1 -> String(it1) }
                    val connPool = BoneCP(config)
                    val connection: Connection = connPool.connection
                    val statement: Statement = connection.createStatement()
                    connPool.close()
                    connection.close()
                    statement.close()

                    isSuccess = true
                }
            }
        } catch (e: SQLException) {
            isSuccess = false
            msg = e.cause?.message
        } catch (e: Exception) {
            isSuccess = false
            msg = e.message
        }

        if (isSuccess) {
            Messages.showMessageDialog("Test Succeed!", "tips", Messages.getInformationIcon())
        } else {
            Messages.showMessageDialog(
                "Test failed,Please check input or cfg!$msg",
                "tips",
                Messages.getInformationIcon()
            )
        }
    }
}