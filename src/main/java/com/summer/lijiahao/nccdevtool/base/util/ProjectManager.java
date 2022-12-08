package com.summer.lijiahao.nccdevtool.base.util;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;

/**
 * @escription 采用kotlin的话ModuleManager.getInstance(project)检验不过插件仓库
 * @Author summer
 * @Email lijiahaosummer@gmail.com
 * @Date 2022/12/8 17:35
 * @Version 1.0
 **/
public class ProjectManager {
    /**
     * 根据moduleName获得module
     *
     * @param moduleName
     * @return
     */
    public Module getModule(Project project, String moduleName) {
        return ModuleManager.getInstance(project).findModuleByName(moduleName);
    }


    public Module[] getAllModules(Project project) {
        return ModuleManager.getInstance(project).getModules();
    }

}
