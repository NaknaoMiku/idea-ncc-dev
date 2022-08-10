package com.summer.lijiahao.reset.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.summer.lijiahao.reset.helper.AppHelper;
import com.summer.lijiahao.reset.helper.NotificationHelper;
import org.jetbrains.annotations.NotNull;

public class RestartAction extends AnAction implements DumbAware {
    public RestartAction() {
        super("Restart IDE", "Restart my IDE", AllIcons.Actions.Restart);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        NotificationHelper.checkAndExpire(e);

        AppHelper.restart();
    }
}
