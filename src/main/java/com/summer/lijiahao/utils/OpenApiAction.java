package com.summer.lijiahao.utils;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.summer.lijiahao.abs.AbstractAnAction;

public class OpenApiAction extends AbstractAnAction {
    private final static String URL = "https://nccdev.yonyou.com/openapi/doc";

    @Override
    public void doAction(AnActionEvent event) {
        BrowserUtil.open(URL);
    }
}
