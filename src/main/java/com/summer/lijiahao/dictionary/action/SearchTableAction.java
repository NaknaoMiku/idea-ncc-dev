package com.summer.lijiahao.dictionary.action;

import com.intellij.ui.table.JBTable;
import com.summer.lijiahao.abs.AbstractButtonAction;
import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.base.NccEnvSettingService;
import com.summer.lijiahao.dictionary.DataDictionaryDialog;
import com.summer.lijiahao.dictionary.listener.TableListClickedListener;
import com.summer.lijiahao.dictionary.util.SearchTableUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SearchTableAction extends AbstractButtonAction {
    public SearchTableAction(AbstractDialog dialog) {
        super(dialog);
    }

    @Override
    public void doAction(ActionEvent event) {


        String dsName = NccEnvSettingService.getInstance().getConnected_data_source();
        DataDictionaryDialog dlg = (DataDictionaryDialog) getDialog();
        //索引字段
        String searchKey = dlg.getComponent(JTextField.class, "searchField").getText();

        //查询工具
        SearchTableUtil util = SearchTableUtil.getInstance(dsName);

        //查询数据

        //构建table
        JBTable table = util.getTableList(searchKey);
        table.addMouseListener(new TableListClickedListener(dlg));

        //显示数据
        dlg.getComponent(JScrollPane.class, "tableListScrollPane").setViewportView(table);

        String classId = null;
        String tableId = null;
        if (table.getRowCount() > 0) {
            classId = (String) table.getValueAt(0, 0);
            tableId = (String) table.getValueAt(0, 1);
        }
        JBTable tableInfo = SearchTableUtil.getInstance(dsName).getTableInfo(classId, tableId);
        dlg.getComponent(JScrollPane.class, "tableInfoScrollPane").setViewportView(tableInfo);
    }
}
