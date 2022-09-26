package com.summer.lijiahao.devconfig.action.button.datasource;

import com.intellij.openapi.ui.Messages;
import com.summer.lijiahao.abs.AbstractButtonAction;
import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.devconfig.DevConfigDialog;
import com.summer.lijiahao.script.studio.connection.ConnectionService;
import com.summer.lijiahao.script.studio.connection.model.DataSourceMetaInfo;
import com.summer.lijiahao.script.studio.ui.preference.prop.DataSourceMeta;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

/**
 * 测试数据源连接
 */
public class TestConnectionAction extends AbstractButtonAction {
    public TestConnectionAction(AbstractDialog dialog) {
        super(dialog);
    }

    @Override
    public void doAction(ActionEvent event) {

        DataSourceMeta currMeta = ((DevConfigDialog) getDialog()).getCurrMeta();
        if (currMeta == null)
            return;
        DataSourceMetaInfo meta = new DataSourceMetaInfo();
        meta.setUser(currMeta.getUser());
        meta.setPwd(currMeta.getPassword());
        meta.setUrl(currMeta.getDatabaseUrl());
        meta.setDriver(currMeta.getDriverClassName());
        boolean flag;
        try {
            flag = ConnectionService.testConnection(meta);
        } catch (Exception e1) {
            String msg = MessageFormat.format("connection failed.(jdbcurl:{0}; user:{1}; pwd:{2}; driver:{3})", currMeta.getDatabaseUrl(), currMeta.getUser(),
                    currMeta.getPassword(), currMeta.getDriverClassName());
            Messages.showMessageDialog("Test failed,Please check input or cfg!" + '\n' + msg, "tips", Messages.getInformationIcon());
            return;
        }
        if (flag) {
            Messages.showMessageDialog("Test Succeed!", "tips", Messages.getInformationIcon());
        }
    }
}
