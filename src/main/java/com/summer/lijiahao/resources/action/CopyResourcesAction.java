package com.summer.lijiahao.resources.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.FsRoot;
import com.summer.lijiahao.abs.AbstractAnAction;
import com.summer.lijiahao.resources.util.CopyResourcesUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CopyResourcesAction extends AbstractAnAction {

    @Override
    public void doAction(AnActionEvent event) {
        try {
            VirtualFile selectFile = getSelectFile(event);
            CopyResourcesUtil util = new CopyResourcesUtil(selectFile.getPath());
            util.copy(event);
            Messages.showInfoMessage("success", "Tips");
        } catch (Exception e) {
            Messages.showInfoMessage(e.getMessage(), "Error");
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        VirtualFile selectFile = getSelectFile(e);
        boolean flag;

        if (selectFile == null || selectFile instanceof FsRoot) {
            flag = false;
        } else {
            File file = new File(selectFile.getPath());
            if (file.isFile()) {
                flag = file.getName().endsWith(".properties");
            } else {
                flag = false;
            }
        }
        e.getPresentation().setEnabledAndVisible(flag);
    }
}
