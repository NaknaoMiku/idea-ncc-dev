package com.summer.lijiahao.utils;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.summer.lijiahao.abs.AbstractAnAction;
import com.summer.lijiahao.base.NccEnvSettingService;

import java.io.File;

public class OpenNCConfigAction extends AbstractAnAction {
    @Override
    public void doAction(AnActionEvent event) {
        try {
            String ncHome = NccEnvSettingService.getInstance().getNcHomePath();
            if (ncHome == null || ncHome.equals("")) {
                throw new Exception("未配置NC home");
            }

            String configDirPath = ncHome + File.separatorChar + "bin" + File.separatorChar;
            if (isWindows()) {
                configDirPath = configDirPath + "sysConfig.bat";
            } else {
                configDirPath = configDirPath + "sysConfig.sh";
            }

            File configFile = new File(configDirPath);
            if (!configFile.isFile()) {
                throw new Exception("配置文件不存在");
            }

            Runtime.getRuntime().exec(configDirPath);
        } catch (Exception e) {
            String message = e.getMessage();
            Messages.showInfoMessage(message, "Error");
        }
    }

    public boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
