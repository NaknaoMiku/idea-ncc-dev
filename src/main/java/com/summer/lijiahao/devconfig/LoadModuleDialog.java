package com.summer.lijiahao.devconfig;

import com.summer.lijiahao.abs.AbstractDialog;

import javax.swing.*;
import java.awt.*;

public class LoadModuleDialog extends AbstractDialog {
    private JPanel contentPane;
    private JProgressBar progressBar;
    private String msg;


    public LoadModuleDialog() {
        setContentPane(contentPane);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setBounds((width - 600) / 2, (height - 200) / 2, 600, 200);

        progressBar.setValue(0);
        // 绘制百分比文本（进度条中间显示的百分数）
        progressBar.setStringPainted(true);
        progressBar.addChangeListener(e -> {
            Dimension d = progressBar.getSize();
            Rectangle rect = new Rectangle(0, 0, d.width, d.height);
            progressBar.paintImmediately(rect);
        });
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
