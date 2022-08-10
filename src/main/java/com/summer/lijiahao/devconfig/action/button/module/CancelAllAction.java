package com.summer.lijiahao.devconfig.action.button.module;

import com.summer.lijiahao.abs.AbstractButtonAction;
import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.devconfig.util.TableModelUtil;

import javax.swing.JTable;
import java.awt.event.ActionEvent;

/**
 * 全消按钮
 */
public class CancelAllAction extends AbstractButtonAction {

    private int type;

    public CancelAllAction(AbstractDialog dialog, int type) {
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
            TableModelUtil.setAllCheckState(table, false);
        }
    }
}
