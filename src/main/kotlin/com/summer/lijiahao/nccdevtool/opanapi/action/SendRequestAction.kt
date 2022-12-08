package com.summer.lijiahao.nccdevtool.opanapi.action

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.opanapi.service.OpenApiConfig
import com.summer.lijiahao.nccdevtool.opanapi.service.OpenApiConfigService
import com.summer.lijiahao.nccdevtool.opanapi.ui.OpenApiTool
import com.summer.lijiahao.nccdevtool.openapi.util.APICurUtils
import com.summer.lijiahao.nccdevtool.openapi.util.APIOldUtils
import com.summer.lijiahao.nccdevtool.openapi.util.IAPIUtils
import org.apache.commons.lang.StringUtils

class SendRequestAction(openApiTool: OpenApiTool) : DumbAwareAction("发送", "发送请求", AllIcons.Actions.Execute) {
    private val openApiTool: OpenApiTool

    init {
        this.openApiTool = openApiTool
    }

    override fun actionPerformed(e: AnActionEvent) {
        val openApiConfig: OpenApiConfig = OpenApiConfigService.getInstance(e.project!!).state

        val requestUrl: String? = openApiTool.requestText?.text
        val requestJson: String? = openApiTool.sendText?.text
        val isNewVersion: Boolean? = openApiTool.newRadio?.isSelected


        if (openApiConfig.busicode.isNullOrBlank()) {
            Messages.showErrorDialog("请先对OpenAPI进行配置", "错误")
            return
        }
        if (requestUrl.isNullOrBlank()) {
            Messages.showErrorDialog("请求路径不能为空", "错误")
            return
        }
        if (requestUrl.indexOf("nccloud") != 0) {
            Messages.showErrorDialog("请求路径以nccloud开头", "错误")
            return
        }
        val util: IAPIUtils= if (isNewVersion!!) {
            APICurUtils()
        } else {
            APIOldUtils()
        }
        util.init(
            openApiConfig.serverip, openApiConfig.serverPort, openApiConfig.busicode,
            openApiConfig.appid, openApiConfig.appSecret, openApiConfig.pubSecret,
            openApiConfig.usercode, null
        )
        try {
            val token: String = util.getToken()
            util.setApiUrl(requestUrl)
            val result: String = util.getAPIRetrun(token, requestJson)
            if (StringUtils.isNotBlank(result)) {
                val jsonObject = JSONObject.parseObject(result)
                val responseJson = JSON.toJSONString(
                    jsonObject, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat
                )
                openApiTool.receiveText?.text = responseJson
            } else {
                openApiTool.receiveText?.text = "无返回数据！"
            }
        } catch (exception: Exception) {
            val message = exception.message
            Messages.showInfoMessage(message, "Error")
        }
    }
}