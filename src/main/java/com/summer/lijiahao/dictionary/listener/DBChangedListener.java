package com.summer.lijiahao.dictionary.listener;

import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.abs.AbstractItemListener;
import com.summer.lijiahao.base.NccEnvSettingService;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class DBChangedListener extends AbstractItemListener {
    public DBChangedListener(AbstractDialog dialog) {
        super(dialog);
    }

    @Override
    public void afterSelect(ItemEvent e) {
        String dsname = (String) getDialog().getComponent(JComboBox.class, "dbBox").getSelectedItem();
        if (StringUtils.isNotBlank(dsname)) {
            NccEnvSettingService.getInstance().setConnected_data_source(dsname);
        }
    }
}
