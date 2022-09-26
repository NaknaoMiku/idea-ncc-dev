package com.summer.lijiahao.devconfig.action.item;

import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.abs.AbstractItemListener;
import com.summer.lijiahao.devconfig.DevConfigDialog;
import com.summer.lijiahao.devconfig.util.DataSourceUtil;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ItemEvent;

/**
 * 数据源列表下拉监听
 */
public class DBBoxListener extends AbstractItemListener {
    public DBBoxListener(AbstractDialog dialog) {
        super(dialog);
    }

    @Override
    public void afterSelect(ItemEvent e) {
        String dsname = (String) getDialog().getComponent(JComboBox.class, "dbBox").getSelectedItem();
        if (StringUtils.isNotBlank(dsname)) {
            DevConfigDialog dialog = (DevConfigDialog) getDialog();
            dialog.setCurrMeta(dialog.getDataSourceMetaMap().get(dsname));
            DataSourceUtil.fillDataSourceMeta(dialog);
        }
    }
}
