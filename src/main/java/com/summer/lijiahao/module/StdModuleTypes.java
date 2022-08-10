package com.summer.lijiahao.module;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.openapi.module.ModuleType;

public class StdModuleTypes {
    public static final ModuleType<JavaModuleBuilder> JAVA;

    public StdModuleTypes() {
    }

    static {
        try {
            JAVA = (ModuleType) Class.forName("com.summer.lijiahao.module.NCCModuleType").newInstance();
        } catch (Exception var1) {
            throw new IllegalArgumentException(var1);
        }
    }
}
