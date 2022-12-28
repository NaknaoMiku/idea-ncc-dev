package com.summer.lijiahao.nccdevtool.devconfig.ui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogWrapper
import com.summer.lijiahao.nccdevtool.devconfig.listener.*
import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import com.summer.lijiahao.nccdevtool.devconfig.util.datasource.DataSourceUtil
import com.summer.lijiahao.nccdevtool.devconfig.util.libraries.LibrariesUtil
import com.summer.lijiahao.nccdevtool.devconfig.util.modules.DefaultTableModelUtil
import com.summer.lijiahao.nccdevtool.devconfig.util.modules.ModulesUtil
import java.awt.Toolkit
import java.awt.event.ItemEvent
import javax.swing.*
import javax.swing.table.TableColumn
import javax.swing.table.TableColumnModel

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
    var mustBtn: JButton? = null
    var selAllRBtn: JButton? = null
    var cancelRBtn: JButton? = null
    var selTable: JTable? = null
    var setLibBtn: JButton? = null

    private var contentPane: JPanel? = null

    init {
        this.event = event
        isModal = true
        title = "开发配置"
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

    /**
     * 重写按钮列表
     */
    override fun createActions(): Array<Action> {
        val helpAction = helpAction
        return if (helpAction === myHelpAction && helpId == null) arrayOf(
            okAction,
            cancelAction,
            ApplyAction(this)
        ) else arrayOf(
            okAction, cancelAction, ApplyAction(this), helpAction
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
        homeSelBtn?.addActionListener(SelectHomeListener(homeSelBtn!!, this))
        setLibBtn?.addActionListener(SetLibraryListener(this))

        //数据源相关按钮
        testBtn?.addActionListener(TestConnectionListener(this))
        setDevBtn?.addActionListener(SetDevDataSourceListener(this))
        setBaseBtn?.addActionListener(SetBaseDataSourceListener(this))
        copyBtn?.addActionListener(CopyDataSourceListener(this))
        delBtn?.addActionListener(DeleteDataSourceListener(this))

        //数据源监听
        dbBox?.addItemListener(SelectDataSourceListener(this))
        dbTypeBox?.addItemListener(SelectDataTypeListener(this))
//        driverBox!!.addItemListener(DriverBoxListener(this))

        mustBtn?.addActionListener(SelectMustModuleListener(this))
        selAllRBtn?.addActionListener(SelectAllModuleListener(this))
        cancelRBtn?.addActionListener(SelectAllCancelModuleListener(this))

        plaintext?.addItemListener { e: ItemEvent ->
            if (e.stateChange == ItemEvent.SELECTED) { //被选中
                pwdText!!.echoChar = 0.toChar()
            } else {
                pwdText!!.echoChar = '•'
            }
        }
    }

    private fun initModule() {
        val selectedModel = DefaultTableModelUtil.selectedModel

        ModulesUtil.initAllModules(this, selectedModel)

        selTable?.model = selectedModel

        this.selTable?.let {
            val tableMode: TableColumnModel = it.columnModel
            val noColumn: TableColumn = tableMode.getColumn(0)
            noColumn.preferredWidth = 5
            noColumn.width = 5
        }
    }


    override fun doOKAction() {
        val homePath = this.homeText?.text
        if (!homePath.isNullOrEmpty()) {
            //判断当前的home路径是否和环境的一致
            val homeChanged = NCCloudEnvSettingService.getInstance(this.event).ncHomePath != homePath
            if (homeChanged) {
                NCCloudEnvSettingService.getInstance(this.event).ncHomePath = homePath
                LibrariesUtil.setLibraries(this.event, homePath)
            }
            DataSourceUtil.writeDataSource(this)
            ModulesUtil.writeModuleToConfig(this)

        }

        super.doOKAction()
    }
}
