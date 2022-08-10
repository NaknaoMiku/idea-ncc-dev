package com.summer.lijiahao.devconfig.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.summer.lijiahao.devconfig.DevConfigDialog;
import com.summer.lijiahao.abs.AbstractAnAction;

/**
 * 集成环境配置按钮
 */
public class DevConfigAction extends AbstractAnAction {
    @Override
    public void doAction(AnActionEvent event) {
        DevConfigDialog dialog = new DevConfigDialog();
        dialog.setVisible(true);
    }
}
