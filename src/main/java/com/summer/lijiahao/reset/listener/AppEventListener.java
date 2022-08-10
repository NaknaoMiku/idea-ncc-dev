package com.summer.lijiahao.reset.listener;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.summer.lijiahao.reset.common.Resetter;
import com.summer.lijiahao.reset.helper.BrokenPlugins;
import com.summer.lijiahao.reset.helper.ResetTimeHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AppEventListener implements AppLifecycleListener {
    public void appFrameCreated(String[] commandLineArgs, @NotNull Ref<Boolean> willOpenProject) {

    }

    public void appStarting(@Nullable Project projectFromCommandLine) {

    }

    public void projectFrameClosed() {

    }

    public void projectOpenFailed() {

    }

    public void welcomeScreenDisplayed() {

    }

    public void appClosing() {
        BrokenPlugins.fix();
        Resetter.touchLicenses();

        if (!Resetter.isAutoReset()) {
            return;
        }

        Resetter.reset(Resetter.getEvalRecords());
        ResetTimeHelper.resetLastResetTime();
    }
}
