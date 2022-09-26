package com.summer.lijiahao.devconfig.action.item;

import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.abs.AbstractItemListener;
import com.summer.lijiahao.devconfig.DevConfigDialog;
import com.summer.lijiahao.devconfig.util.DataSourceUtil;
import com.summer.lijiahao.script.studio.ui.preference.dbdriver.DriverInfo;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ItemEvent;

/**
 * 驱动列表下拉监听
 */
public class DBTypeBoxListener extends AbstractItemListener {
    public DBTypeBoxListener(AbstractDialog dialog) {
        super(dialog);
    }

    @Override
    public void afterSelect(ItemEvent e) {

        DevConfigDialog dialog = (DevConfigDialog) getDialog();
        String selected = (String) dialog.getComponent(JComboBox.class, "dbTypeBox").getSelectedItem();

        if (StringUtils.isNotBlank(selected)) {
            DriverInfo[] infos = dialog.getDatabaseDriverInfoMap().get(selected).getDatabase();
            DataSourceUtil.fillCombo(dialog.getComponent(JComboBox.class, "driverBox"), infos, dialog);
        }
    }
}
