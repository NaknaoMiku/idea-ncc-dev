package com.summer.lijiahao.utils.openapi.ui;

import com.intellij.openapi.project.Project;
import com.summer.lijiahao.utils.openapi.OpenApiService;
import com.summer.lijiahao.utils.openapi.setting.OpenApiConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SettingDialog extends JDialog {
    private final Project project;
    private JTextField serverip;
    private JTextField serverPort;
    private JTextField busicode;
    private JTextField appid;
    private JTextField appSecret;
    private JTextField usercode;
    private JTextArea pubSecret;
    private JButton buttonCancel;
    private JButton buttonOK;
    private JPanel contentPane;

    public SettingDialog(Project project) {
        this.project = project;
        OpenApiService service = OpenApiService.getInstance(project);
        OpenApiConfig config = service.getState();
        this.serverip.setText(config.getServerip());
        this.serverPort.setText(config.getServerPort());
        this.busicode.setText(config.getBusicode());
        this.appSecret.setText(config.getAppSecret());
        this.usercode.setText(config.getUsercode());
        this.appid.setText(config.getAppid());
        this.pubSecret.setText(config.getPubSecret());

        setContentPane(this.contentPane);
        setModal(true);
        getRootPane().setDefaultButton(this.buttonOK);


        int width = (Toolkit.getDefaultToolkit().getScreenSize()).width;
        int height = (Toolkit.getDefaultToolkit().getScreenSize()).height;
        setBounds((width - 510) / 2, (height - 300) / 2, 510, 300);

        setTitle("OpenApi配置");

        this.buttonOK.addActionListener(e -> SettingDialog.this.onOK());
        this.buttonCancel.addActionListener(e -> SettingDialog.this.onCancel());


        setDefaultCloseOperation(0);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                SettingDialog.this.onCancel();
            }
        });


        this.contentPane.registerKeyboardAction(e -> SettingDialog.this.onCancel(), KeyStroke.getKeyStroke(27, 0), 1);
    }


    private void onOK() {
        OpenApiService service = OpenApiService.getInstance(this.project);
        OpenApiConfig config = service.getState();
        config.setServerip(this.serverip.getText());
        config.setServerPort(this.serverPort.getText());
        config.setBusicode(this.busicode.getText());
        config.setAppSecret(this.appSecret.getText());
        config.setUsercode(this.usercode.getText());
        config.setAppid(this.appid.getText());
        config.setPubSecret(this.pubSecret.getText());

        dispose();
    }


    private void onCancel() {
        dispose();
    }
}
