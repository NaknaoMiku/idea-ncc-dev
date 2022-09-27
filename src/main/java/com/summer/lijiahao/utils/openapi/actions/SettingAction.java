package com.summer.lijiahao.utils.openapi.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.summer.lijiahao.utils.openapi.ui.SettingDialog;
import org.jetbrains.annotations.NotNull;

public class SettingAction
        extends DumbAwareAction {
    public SettingAction() {
        super("OpenAPI配置", "OpenAPI配置内容", AllIcons.Actions.InlayGear);

    }


    public void actionPerformed(@NotNull AnActionEvent e) {
        (new SettingDialog(e.getProject())).setVisible(true);
    }
}