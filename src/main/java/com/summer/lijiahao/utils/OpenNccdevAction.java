package com.summer.lijiahao.utils;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.summer.lijiahao.abs.AbstractAnAction;

public class OpenNccdevAction extends AbstractAnAction {
    private final static String DEVURL = "https://nccdev.yonyou.com/index";

    @Override
    public void doAction(AnActionEvent event) {
        BrowserUtil.open(DEVURL);
    }
}
