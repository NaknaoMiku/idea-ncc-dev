package com.summer.lijiahao.devconfig.action.button;

import com.summer.lijiahao.abs.AbstractButtonAction;
import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.base.BusinessException;
import com.summer.lijiahao.devconfig.DevConfigDialog;
import com.summer.lijiahao.devconfig.util.DataSourceUtil;
import com.summer.lijiahao.devconfig.util.TableModelUtil;

import java.awt.event.ActionEvent;

/**
 * 数据源保存
 */
public class ApplyAction extends AbstractButtonAction {
    public ApplyAction(AbstractDialog dialog) {
        super(dialog);
    }

    @Override
    public void doAction(ActionEvent event) throws BusinessException {
        DevConfigDialog dialog = (DevConfigDialog) getDialog();

        //数据源保存
        if (dialog.getTabIndex() == 0) {
            DataSourceUtil.syncCurrDataSourceValue(dialog);
        }

        //模块选择保存
        if (dialog.getTabIndex() == 1) {
            TableModelUtil.saveModuleConfig(getDialog());
        }
    }
}
