package com.summer.lijiahao.utils;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.summer.lijiahao.abs.AbstractAnAction;
import com.summer.lijiahao.base.NccEnvSettingService;

import java.awt.*;
import java.io.File;

public class OpenNCHomeAction extends AbstractAnAction {
    @Override
    public void doAction(AnActionEvent event) {
        try {
            String ncHome = NccEnvSettingService.getInstance().getNcHomePath();
            if (ncHome == null || ncHome.equals("")) {
                throw new Exception("未配置NC home");
            }

            Desktop desktop = Desktop.getDesktop();
            File dirToOpen = new File(ncHome);
            desktop.open(dirToOpen);
        } catch (Exception e) {
            String message = e.getMessage();
            Messages.showInfoMessage(message, "Error");
        }
    }
}
