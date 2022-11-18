package com.summer.lijiahao.abs;

import com.intellij.openapi.ui.Messages;
import com.summer.lijiahao.base.BusinessException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * swing 抽象按钮
 */
public abstract class AbstractButtonAction implements ActionListener {

    private AbstractDialog dialog;

    public AbstractButtonAction(AbstractDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            doAction(event);
        } catch (BusinessException e) {
            Messages.showErrorDialog(e.getMessage(), "Error");
        }
    }

    public abstract void doAction(ActionEvent event) throws BusinessException;

    public AbstractDialog getDialog() {
        return dialog;
    }

    public void setDialog(AbstractDialog dialog) {
        this.dialog = dialog;
    }
}
