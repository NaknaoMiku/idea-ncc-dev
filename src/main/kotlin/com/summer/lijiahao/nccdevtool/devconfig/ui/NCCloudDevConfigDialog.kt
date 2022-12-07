package com.summer.lijiahao.nccdevtool.devconfig.ui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogWrapper
import com.summer.lijiahao.nccdevtool.devconfig.listener.*
import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSourceUtil
import com.summer.lijiahao.nccdevtool.devconfig.util.modules.DefaultTableModelUtil
import com.summer.lijiahao.nccdevtool.devconfig.util.modules.ModulesUtil
import java.awt.Toolkit
import javax.swing.*
import javax.swing.table.DefaultTableModel

/**
 * @Description 开发配置弹窗
 * @Author summer
 * @Email lijiahaosummer@gmail.com
 * @Date 2022/12/5 10:34
 * @Version 1.0
 **/
class NCCloudDevConfigDialog(event: AnActionEvent) : DialogWrapper(true) {

    var event: AnActionEvent
    var tabbedPane: JTabbedPane? = null
    var homeText: JTextField? = null
    var hostText: JTextField? = null
    var portText: JTextField? = null
    var dbNameText: JTextField? = null
    var oidText: JTextField? = null
    var userText: JTextField? = null
    var baseChx: JCheckBox? = null
    var devChx: JCheckBox? = null
    var dbTypeBox: JComboBox<*>? = null
    var driverBox: JComboBox<*>? = null
    var dbBox: JComboBox<*>? = null
    var plaintext: JCheckBox? = null
    var pwdText: JPasswordField? = null
    var homeSelBtn: JButton? = null
    var testBtn: JButton? = null
    var setDevBtn: JButton? = null
    var setBaseBtn: JButton? = null
    var copyBtn: JButton? = null
    var delBtn: JButton? = null
    var dsTab: JPanel? = null
    var moduleTab: JPanel? = null
    var defaultBtn: JButton? = null
    var selAllLBtn: JButton? = null
    var cancelAllLBtn: JButton? = null
    var mustBtn: JButton? = null
    var selAllRBtn: JButton? = null
    var cancelRBtn: JButton? = null
    var selTable: JTable? = null
    var setLibBtn: JButton? = null

    private var contentPane: JPanel? = null

    init {
        this.event = event
        isModal = true
        title = "NCCloud Config"
        init()
    }


    override fun createCenterPanel(): JComponent {

        initData()
        DataSourceUtil.initDataSource(this)
        initModule()
        initListener()

        //获取显示屏尺寸，使界面居中
        val width = Toolkit.getDefaultToolkit().screenSize.width
        val height = Toolkit.getDefaultToolkit().screenSize.height
        contentPane?.setBounds((width - 800) / 2, (height - 600) / 2, 800, 600)
        return contentPane!!
    }

    override fun createActions(): Array<Action> {
        val helpAction = helpAction
        return if (helpAction === myHelpAction && helpId == null) arrayOf(
            okAction,
            cancelAction,
            ApplyAction()
        ) else arrayOf(
            okAction, cancelAction, ApplyAction(), helpAction
        )
    }

    private fun initData() {
        //每次打开都去读取数据源配置文件，更新数据源列表
        DataSourceUtil.dataSources = linkedMapOf()
        val service = NCCloudEnvSettingService.getInstance(event)
        homeText?.let { it.text = service.ncHomePath }
    }

    private fun initListener() {
        //home路径选择按钮
        homeSelBtn!!.addActionListener(SelectHomeListener(homeSelBtn!!, this))
        setLibBtn!!.addActionListener(SetLibraryListener(this))

        //数据源相关按钮
        testBtn!!.addActionListener(TestConnectionListener(this))
        setDevBtn!!.addActionListener(SetDevDataSourceListener(this))
        setBaseBtn!!.addActionListener(SetBaseDataSourceListener(this))
        copyBtn!!.addActionListener(CopyDataSourceListener(this))
        delBtn!!.addActionListener(DeleteDataSourceListener(this))

        //数据源监听
        dbBox!!.addItemListener(SelectDataSourceListener(this))
        dbTypeBox!!.addItemListener(SelectDataTypeListener(this))
//        driverBox!!.addItemListener(DriverBoxListener(this))
    }

    private fun initModule() {
        val selectedModel =  DefaultTableModelUtil.selectedModel;

        ModulesUtil.initAllModules(this, selectedModel);

        selTable?.model = selectedModel
    }

}
