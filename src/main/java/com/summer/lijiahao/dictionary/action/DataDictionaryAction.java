package com.summer.lijiahao.dictionary.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.summer.lijiahao.abs.AbstractAnAction;
import com.summer.lijiahao.dictionary.DataDictionaryDialog;

/**
 * 数据字典
 */
public class DataDictionaryAction extends AbstractAnAction {
    @Override
    public void doAction(AnActionEvent event) {
        DataDictionaryDialog dialog = new DataDictionaryDialog();
        dialog.setVisible(true);
    }
}
