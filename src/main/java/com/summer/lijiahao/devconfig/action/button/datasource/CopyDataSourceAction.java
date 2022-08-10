package com.summer.lijiahao.devconfig.action.button.datasource;

import com.summer.lijiahao.abs.AbstractButtonAction;
import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.devconfig.DataSourceCopyDlg;
import com.summer.lijiahao.devconfig.DevConfigDialog;

import java.awt.event.ActionEvent;

/**
 * 复制数据源
 */
public class CopyDataSourceAction extends AbstractButtonAction {
    public CopyDataSourceAction(AbstractDialog dialog) {
        super(dialog);
    }

    @Override
    public void doAction(ActionEvent event) {
        DevConfigDialog dialog = (DevConfigDialog) getDialog();
        DataSourceCopyDlg dlg = new DataSourceCopyDlg();
        dlg.setParentDlg(dialog);
        dlg.setVisible(true);
    }
}
