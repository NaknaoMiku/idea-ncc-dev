package com.summer.lijiahao.utils.openapi.ui;

import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.lang.reflect.Field;

public class OpenApiTool {
    private JTextField requestText;
    private JRadioButton newRadio;
    private JRadioButton oldRadio;
    private JPanel sendLab;
    private JPanel mainPanel;
    private JLabel toolVesrion;
    private JLabel requestLab;
    private JPanel receiveLab;
    private JTextArea sendText;
    private JTextArea receiveText;

    private ButtonGroup buttonGroup;

    public OpenApiTool() {
        this.buttonGroup = new ButtonGroup();
        this.buttonGroup.add(this.newRadio);
        this.buttonGroup.add(this.oldRadio);
    }

    public JComponent getComponent() {
        return this.mainPanel;
    }

    public <T> T getComponent(String componentName, Class<T> clazz) {
        try {
            Field declaredField = getClass().getDeclaredField(componentName);
            declaredField.setAccessible(true);
            return (T) declaredField.get(this);
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(), "Error");
            return null;
        }
    }
}
