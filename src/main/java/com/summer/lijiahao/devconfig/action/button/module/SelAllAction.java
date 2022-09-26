package com.summer.lijiahao.devconfig.action.button.module;

import com.summer.lijiahao.abs.AbstractButtonAction;
import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.devconfig.util.TableModelUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 全选按钮
 */
public class SelAllAction extends AbstractButtonAction {

    private final int type;

    public SelAllAction(AbstractDialog dialog, int type) {
        super(dialog);
        this.type = type;
    }


    @Override
    public void doAction(ActionEvent event) {
        JTable table = null;
        if (type == TableModelUtil.MODULE_TYPE_MUST) {
            table = getDialog().getComponent(JTable.class, "mustTable");
        } else if (type == TableModelUtil.MODULE_TYPE_SEL) {
            table = getDialog().getComponent(JTable.class, "selTable");
        }

        if (table != null) {
            TableModelUtil.setAllCheckState(table, true);
        }
    }
}
