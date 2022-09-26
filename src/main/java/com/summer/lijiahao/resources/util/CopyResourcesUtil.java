package com.summer.lijiahao.resources.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.summer.lijiahao.base.BaseUtil;
import com.summer.lijiahao.base.NccEnvSettingService;

import java.io.File;
import java.io.IOException;

public class CopyResourcesUtil {
    private String selectFilePach;

    public CopyResourcesUtil(String selectFilePach) {
        this.selectFilePach = selectFilePach;
    }

    public String getSelectFilePach() {
        return selectFilePach;
    }

    public void setSelectFilePach(String selectFilePach) {
        this.selectFilePach = selectFilePach;
    }

    public void copy(AnActionEvent event) throws IOException {
        String homePath = NccEnvSettingService.getInstance().getNcHomePath();
        String resourcesPath = homePath + File.separator + "resources" + File.separator;

        Module module = BaseUtil.getModule(event);
        VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
        if (contentRoots.length == 0) {
            return;
        }

        File file = new File(this.selectFilePach);
        try {
            // 逐个拷贝到home
            FileUtil.copy(file, new File(resourcesPath + file.getName()));
        } catch (IOException ignored) {
            throw new IOException("文件" + file.getName() + "拷贝出错");
        }
    }

}
