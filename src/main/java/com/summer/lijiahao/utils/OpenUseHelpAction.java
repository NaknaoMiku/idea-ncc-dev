package com.summer.lijiahao.utils;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @escription 打开插件使用手册
 * @Author summer
 * @Email lijiahaosummer@gmail.com
 * @Date 2022/11/26 10:43
 * @Version 1.0
 **/
public class OpenUseHelpAction extends AnAction {
    private final static String HELPFULNESS = "https://gitee.com/nakanonmiku/idea-ncc-plug/blob/master/readme.md";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        BrowserUtil.open(HELPFULNESS);
    }
}
