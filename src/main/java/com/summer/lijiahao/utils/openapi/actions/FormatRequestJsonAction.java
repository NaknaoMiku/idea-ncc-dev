package com.summer.lijiahao.utils.openapi.actions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.summer.lijiahao.utils.openapi.ui.OpenApiTool;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class FormatRequestJsonAction extends DumbAwareAction {
    private final OpenApiTool openApiTool;

    public FormatRequestJsonAction(OpenApiTool openApiTool) {
        super("格式化", "格式化", AllIcons.Actions.PrettyPrint);
        this.openApiTool = openApiTool;
    }

    public void actionPerformed(@NotNull AnActionEvent e) {
        String requestJson = this.openApiTool.getComponent("sendText", JTextArea.class).getText();
        if(StringUtils.isBlank(requestJson)) {
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(requestJson);
        String responseJson =
                JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                        SerializerFeature.WriteDateUseDateFormat);
        this.openApiTool.getComponent("sendText", JTextArea.class).setText(responseJson);
    }
}