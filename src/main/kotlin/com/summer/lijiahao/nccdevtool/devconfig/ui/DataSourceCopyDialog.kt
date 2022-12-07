package com.summer.lijiahao.nccdevtool.devconfig.ui

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSource
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSourceUtil
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class DataSourceCopyDialog(parentDlg: NCCloudDevConfigDialog) : DialogWrapper(true) {
    var contentPane: JPanel? = null
    var newNameText: JTextField? = null
    var parentDlg: NCCloudDevConfigDialog

    init {
        this.parentDlg = parentDlg
        isModal = true
        title = "请输入数据源名称"
        isResizable = false
        init()
    }


    override fun createCenterPanel(): JComponent {
        return contentPane!!
    }

    override fun doOKAction() {

        val currentDataSourceName = parentDlg.dbBox?.selectedItem

        val newDataSourceName = newNameText?.text
        if (newDataSourceName.isNullOrEmpty()) {
            Messages.showErrorDialog("数据源名称不能为空！", "出错了")
            return
        }

        if (DataSourceUtil.dataSources.containsKey(newDataSourceName)) {
            Messages.showErrorDialog("该数据源名称已存在！请更换一个", "出错了")
            return
        }

        //复制一个数据源对象
        val currentDataSource: DataSource = DataSourceUtil.dataSources[currentDataSourceName]!!
        val copyDataSource = DataSource(
            newDataSourceName,
            currentDataSource.oidMark,
            currentDataSource.databaseUrl,
            currentDataSource.user,
            currentDataSource.password,
            currentDataSource.driverClassName,
            currentDataSource.databaseType,
            "false"
        )

        DataSourceUtil.dataSources[newDataSourceName] = copyDataSource
        parentDlg.dbBox?.let {
            val items = DataSourceUtil.dataSources.keys.toTypedArray()
            it.model = DefaultComboBoxModel(items)
            it.selectedItem = newDataSourceName
            parentDlg.devChx?.isSelected = false
            parentDlg.baseChx?.isSelected = false
        }

        val opt = Messages.showYesNoDialog("复制成功", "提示", Messages.getQuestionIcon())
        if (opt == Messages.OK) {
            DataSourceUtil.writeDataSource(parentDlg)
            super.doOKAction()
        }
    }
}