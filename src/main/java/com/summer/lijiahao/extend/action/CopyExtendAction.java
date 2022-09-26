package com.summer.lijiahao.extend.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.FsRoot;
import com.summer.lijiahao.abs.AbstractAnAction;
import com.summer.lijiahao.extend.util.ExtendCopyUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CopyExtendAction extends AbstractAnAction {
    @Override
    public void doAction(AnActionEvent event) {
        try {
            ExtendCopyUtil.copyToNCHome(event);
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
                //VirtualFile在getPath以后只有"/"，因此不需要使用File.separator
                flag = file.getName().endsWith(".xml") && file.getPath().contains("yyconfig/modules") && (file.getParent().endsWith("action") || file.getParent().endsWith("authorize"));
            } else {
                flag = isModuleChild(selectFile, e);
                if (flag) {
                    Module module = getSelectModule(e);

                    if (module != null) {
                        VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
                        if (contentRoots.length > 0 && selectFile.getParent().equals(contentRoots[0])) {
                            flag = new File(selectFile.getPath() + File.separator + "component.xml").exists();
                        }
                    }
                }
            }
        }
        e.getPresentation().setEnabledAndVisible(flag);
    }
}
