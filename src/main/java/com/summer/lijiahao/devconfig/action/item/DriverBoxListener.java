package com.summer.lijiahao.devconfig.action.item;

import com.summer.lijiahao.devconfig.DevConfigDialog;
import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.abs.AbstractItemListener;
import com.summer.lijiahao.devconfig.util.DataSourceUtil;
import com.summer.lijiahao.script.studio.ui.preference.dbdriver.DriverInfo;
import com.summer.lijiahao.script.studio.ui.preference.prop.ToolUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.ItemEvent;

/**
 * 驱动类型下拉监听
 */
public class DriverBoxListener extends AbstractItemListener {
    public DriverBoxListener(AbstractDialog dialog) {
        super(dialog);
    }

    @Override
    public void afterSelect(ItemEvent e) {
        DevConfigDialog dialog = (DevConfigDialog) getDialog();
        String selected = (String) dialog.getComponent(JComboBox.class, "driverBox").getSelectedItem();
        if (StringUtils.isNotBlank(selected)) {
            DriverInfo info = dialog.getDriverInfoMap().get(selected);
            dialog.getComponent(JTextField.class, "hostText").setEnabled(ToolUtils.isJDBCUrl(info
                    .getDriverUrl()));
            dialog.getComponent(JTextField.class, "portText").setEnabled(ToolUtils.isJDBCUrl(info
                    .getDriverUrl()));
            DataSourceUtil.fillDBConnByInfo(dialog, info.getDriverUrl());
        }
    }
}
