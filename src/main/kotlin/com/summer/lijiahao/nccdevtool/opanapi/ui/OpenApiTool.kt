package com.summer.lijiahao.nccdevtool.opanapi.ui

import javax.swing.*

class OpenApiTool {
    var requestText: JTextField? = null
    var newRadio: JRadioButton? = null
    var oldRadio: JRadioButton? = null
    var sendLab: JPanel? = null
    var contentPane: JPanel? = null
    var toolVesrion: JLabel? = null
    var requestLab: JLabel? = null
    var receiveLab: JPanel? = null
    var sendText: JTextArea? = null
    var receiveText: JTextArea? = null

    var buttonGroup: ButtonGroup


    init {
        buttonGroup = ButtonGroup()
        buttonGroup.add(newRadio)
        buttonGroup.add(oldRadio)
    }


    fun getComponent(): JComponent {
        return contentPane!!
    }
}