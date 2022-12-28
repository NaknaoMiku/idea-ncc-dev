package com.summer.lijiahao.nccdevtool.opanapi.ui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogWrapper
import com.summer.lijiahao.nccdevtool.opanapi.service.OpenApiConfig
import com.summer.lijiahao.nccdevtool.opanapi.service.OpenApiConfigService
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JTextField

class OpenapiSettingDialog(event: AnActionEvent) : DialogWrapper(true) {
    private var event: AnActionEvent
    var serverip: JTextField? = null
    var serverPort: JTextField? = null
    var busicode: JTextField? = null
    var appid: JTextField? = null
    var appSecret: JTextField? = null
    var usercode: JTextField? = null
    var pubSecret: JTextArea? = null
    var contentPane: JPanel? = null

    init {
        this.event = event
        title = "Openapi Config"
        init()
    }

    override fun createCenterPanel(): JComponent {
        isResizable = false
        isModal = true

        initData()
        return contentPane!!
    }

    /**
     * 初始化数据源
     */
    private fun initData() {
        val service: OpenApiConfigService = OpenApiConfigService.getInstance(event.project!!)
        val config: OpenApiConfig = service.state
        serverip?.text = config.serverip
        serverPort?.text = config.serverPort
        busicode?.text = config.busicode
        appSecret?.text = config.appSecret
        usercode?.text = config.usercode
        appid?.text = config.appid
        pubSecret?.text = config.pubSecret
    }

    override fun doOKAction() {
        val service: OpenApiConfigService = OpenApiConfigService.getInstance(event.project!!)
        val config: OpenApiConfig = service.state
        config.serverip = serverip?.text
        config.serverPort = serverPort?.text
        config.busicode = busicode?.text
        config.appSecret = appSecret?.text
        config.usercode = usercode?.text
        config.appid = appid?.text
        config.pubSecret = pubSecret?.text
        super.doOKAction()
    }
}