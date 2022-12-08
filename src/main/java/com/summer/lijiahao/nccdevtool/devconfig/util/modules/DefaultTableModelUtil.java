package com.summer.lijiahao.nccdevtool.devconfig.util.modules;

import javax.swing.table.DefaultTableModel;

/**
 * @Description DefaultTableModel表格不支持kotlin复写getColumnClass方法，不能识别到正确的类型
 * @Author summer
 * @Email lijiahaosummer@gmail.com
 * @Date 2022/12/7 16:15
 * @Version 1.0
 **/
public class DefaultTableModelUtil {

    public static DefaultTableModel selectedModel = new DefaultTableModel(null, new String[]{
            "NO.", "Checked", "module", "moduleName"}) {
        public Class<?> getColumnClass(int c) {
            return switch (c) {
                case 0 -> Integer.class;
                case 1 -> Boolean.class;
                default -> String.class;
            };
        }


        //第二列不允许编辑
        public boolean isCellEditable(int row, int column) {
            return column == 1;
        }
    };


}
