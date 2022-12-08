package com.summer.lijiahao.nccdevtool.opanapi.action

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.summer.lijiahao.nccdevtool.opanapi.ui.OpenApiTool

class FormatRequestJsonAction(openApiTool: OpenApiTool) :
    DumbAwareAction("格式化", "格式化", AllIcons.Actions.PrettyPrint) {
    private val openApiTool: OpenApiTool

    init {
        this.openApiTool = openApiTool
    }

    override fun actionPerformed(e: AnActionEvent) {
        val requestJson: String? = openApiTool.sendText?.text
        if (requestJson.isNullOrBlank()) {
            return
        }
        val jsonObject = JSONObject.parseObject(requestJson)
        val responseJson = JSON.toJSONString(
            jsonObject, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteDateUseDateFormat
        )
        openApiTool.sendText?.text = responseJson
    }
}