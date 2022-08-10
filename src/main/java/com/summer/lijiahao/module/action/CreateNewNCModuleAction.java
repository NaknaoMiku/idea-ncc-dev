package com.summer.lijiahao.module.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.summer.lijiahao.module.NewModuleDialog;
import com.summer.lijiahao.abs.AbstractAnAction;

/**
 * 新建nc模块
 */
public class CreateNewNCModuleAction extends AbstractAnAction {
    @Override
    public void doAction(AnActionEvent event) {
        NewModuleDialog dialog = new NewModuleDialog(event);
        dialog.setSize(900, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.requestFocus();
    }
}
