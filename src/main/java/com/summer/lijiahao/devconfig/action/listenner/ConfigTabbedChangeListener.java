package com.summer.lijiahao.devconfig.action.listenner;

import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.abs.AbstractTabListener;
import com.summer.lijiahao.devconfig.DevConfigDialog;
import com.summer.lijiahao.devconfig.util.DataSourceUtil;
import com.summer.lijiahao.devconfig.util.TableModelUtil;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;

/**
 * 设置页面切换监听
 */
public class ConfigTabbedChangeListener extends AbstractTabListener {

    public ConfigTabbedChangeListener(AbstractDialog dlg) {
        super(dlg);
    }

    @Override
    protected void afterChange(ChangeEvent event, AbstractDialog dlg) {
        JTabbedPane tabbedPane = dlg.getComponent(JTabbedPane.class, "tabbedPane");
        int index = tabbedPane.getSelectedIndex();

        if (index == 0) {
            initDataSource();
        }

        if (index == 1) {
            initModule();
        }
    }

    private void initModule() {

        DefaultTableModel mustModel = TableModelUtil.getMustModel(getDlg());
        DefaultTableModel selModel = TableModelUtil.getSelModel(getDlg());

        TableModelUtil.modelHandle(getDlg(), mustModel, selModel);

        getDlg().getComponent(JTable.class, "mustTable").setModel(mustModel);
        getDlg().getComponent(JTable.class, "selTable").setModel(selModel);
    }

    @Override
    protected void click(MouseEvent event, AbstractDialog dlg) {
    }

    /**
     * 加载数据源
     */
    private void initDataSource() {
        DataSourceUtil.initDataSource((DevConfigDialog) getDlg());
    }
}
