package com.summer.lijiahao.utils.openapi.actions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.Messages;
import com.summer.lijiahao.utils.openapi.OpenApiService;
import com.summer.lijiahao.utils.openapi.openapiutils.APICurUtils;
import com.summer.lijiahao.utils.openapi.openapiutils.APIOldUtils;
import com.summer.lijiahao.utils.openapi.openapiutils.IAPIUtils;
import com.summer.lijiahao.utils.openapi.setting.OpenApiConfig;
import com.summer.lijiahao.utils.openapi.ui.OpenApiTool;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

public class SendRequestAction
        extends DumbAwareAction {
    private final OpenApiTool openApiTool;

    public SendRequestAction(OpenApiTool openApiTool) {
        super("发送", "发送请求", AllIcons.Actions.Execute);
        this.openApiTool = openApiTool;
    }

    public void actionPerformed(@NotNull AnActionEvent e) {

        OpenApiConfig openApiConfig = OpenApiService.getInstance(Objects.requireNonNull(e.getProject())).getState();
        String requestUrl = this.openApiTool.getComponent("requestText", JTextField.class).getText();
        String requestJson = this.openApiTool.getComponent("sendText", JTextArea.class).getText();
        boolean isNewVersion = this.openApiTool.getComponent("newRadio", JRadioButton.class).isSelected();
        if (StringUtils.isBlank(openApiConfig.getBusicode())) {
            Messages.showErrorDialog("请先对OpenAPI进行配置", "错误");
            return;
        }
        if (StringUtils.isBlank(requestUrl)) {
            Messages.showErrorDialog("请求路径不能为空", "错误");
            return;
        }
        if (requestUrl.indexOf("nccloud") != 0) {
            Messages.showErrorDialog("请求路径以nccloud开头", "错误");
            return;
        }
        IAPIUtils util = null;
        if (isNewVersion) {
            util = new APICurUtils();
        } else {
            util = new APIOldUtils();
        }
        util.init(openApiConfig.getServerip(), openApiConfig.getServerPort(), openApiConfig.getBusicode(),
                openApiConfig.getAppid(), openApiConfig.getAppSecret(), openApiConfig.getPubSecret(),
                openApiConfig.getUsercode(), null);
        try {
            String token = util.getToken();
            util.setApiUrl(requestUrl);
            String result = util.getAPIRetrun(token, requestJson);
            if (StringUtils.isNotBlank(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                String responseJson =
                        JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                                SerializerFeature.WriteDateUseDateFormat);
                this.openApiTool.getComponent("receiveText", JTextArea.class).setText(responseJson);
            } else {
                this.openApiTool.getComponent("receiveText", JTextArea.class).setText("无返回数据！");
            }
        } catch (Exception exception) {
            String message = exception.getMessage();
            Messages.showInfoMessage(message, "Error");
        }
    }
}