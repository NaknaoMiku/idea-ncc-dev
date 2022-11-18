package com.summer.lijiahao.abs;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.summer.lijiahao.base.ProjectManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

/**
 * idea按钮抽象
 */
public abstract class AbstractAnAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        ProjectManager.getInstance().setProject(anActionEvent.getProject());
        doAction(anActionEvent);
    }

    /**
     * 子类实现
     *
     * @param event
     */
    public abstract void doAction(AnActionEvent event);

    /**
     * 获取选中文件
     *
     * @param event
     * @return
     */
    public VirtualFile getSelectFile(AnActionEvent event) {
        return event.getData(CommonDataKeys.VIRTUAL_FILE);
    }

    public VirtualFile[] getSelectFileArr(AnActionEvent event) {
        return event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
    }

    /**
     * 是否nc module
     *
     * @param event
     * @return
     */
    public boolean isNCModule(@NotNull AnActionEvent event) {
        VirtualFile selectFile = getSelectFile(event);
        if (selectFile == null) {
            return false;
        }
        Module module = ModuleUtil.findModuleForFile(selectFile, Objects.requireNonNull(event.getProject()));
        if (module == null) {
            return false;
        }

        return module.getName().equals(selectFile.getName()) && new File(selectFile.getPath() + File.separator + "META-INF" + File.separator + "module.xml").exists();
    }

    public boolean isModuleChild(VirtualFile file, AnActionEvent event) {
        boolean flag;
        if (file == null) {
            return false;
        }
        Module module = ModuleUtil.findModuleForFile(file, Objects.requireNonNull(event.getProject()));
        if (module == null) {
            return false;
        }


        VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
        if (contentRoots.length == 0) {
            return false;
        }
        flag = new File(contentRoots[0].getPath() + File.separator + "META-INF" + File.separator + "module.xml").exists();
        return flag;
    }

    public boolean isMavenModuleChild(VirtualFile file, AnActionEvent event) {
        boolean flag;
        if (file == null) {
            return false;
        }
        Module module = ModuleUtil.findModuleForFile(file, Objects.requireNonNull(event.getProject()));
        if (module == null) {
            return false;
        }

        VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
        if (contentRoots.length == 0) {
            return false;
        }
        flag = new File(contentRoots[0].getPath() + File.separator + "pom.xml").exists();
        return flag;
    }

    /**
     * 获取选中模块
     *
     * @param event
     * @return
     */
    public Module getSelectModule(AnActionEvent event) {
        return event.getData(LangDataKeys.MODULE);
    }
}
