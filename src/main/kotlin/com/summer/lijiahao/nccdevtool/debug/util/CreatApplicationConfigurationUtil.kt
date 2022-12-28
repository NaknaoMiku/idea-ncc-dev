package com.summer.lijiahao.nccdevtool.debug.util

import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.ShortenCommandLine
import com.intellij.execution.application.ApplicationConfiguration
import com.intellij.execution.application.ApplicationConfigurationType
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.summer.lijiahao.nccdevtool.base.util.BaseUtil
import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import com.summer.lijiahao.nccdevtool.devconfig.util.modules.ModulesUtil
import org.apache.commons.lang.StringUtils
import java.io.File

class CreatApplicationConfigurationUtil {
    companion object {
        const val DEFALUT_PORT = 80
        const val DEFALUT_IP = "127.0.0.1"
        private const val serverClass = "ufmiddle.start.tomcat.StartDirectServer"
        private const val clientClass = "nc.starter.test.JStarter"

        /**
         * 设置启动application
         *
         * @param event
         * @param serverFlag
         */
        fun createApplicationConfiguration(event: AnActionEvent, serverFlag: Boolean) {
            var configName = if (serverFlag) " - server" else " - client"
            val project: Project = BaseUtil.getProject(event)
            val runManager = RunManager.getInstance(project) as RunManagerImpl
            val configurationsList = runManager.allConfigurationsList

            //当前选择module
            val selectModule: Module = BaseUtil.getModule(event)
            configName = selectModule.name + configName

            //循环判断当前配置中有没有当前模块的启动配置
            var hasFlag = false
            if (configurationsList.isNotEmpty()) {
                for (configuration in configurationsList) {
                    if (configuration.name == configName) {
                        hasFlag = true
                        val conf = configuration as ApplicationConfiguration
                        setConfiguration(event, selectModule, conf, serverFlag)
                        break
                    }
                }
            }
            //新增config
            if (!hasFlag) {
                val conf = ApplicationConfiguration(configName, project, ApplicationConfigurationType.getInstance())
                setConfiguration(event, selectModule, conf, serverFlag)
                val runnerAndConfigurationSettings: RunnerAndConfigurationSettings =
                    RunnerAndConfigurationSettingsImpl(runManager, conf)
                runManager.addConfiguration(runnerAndConfigurationSettings)
            }
        }


        private fun setConfiguration(
            event: AnActionEvent,
            selectModule: Module,
            conf: ApplicationConfiguration,
            serverFlag: Boolean
        ) {

            //检查并设置nc home
            val homePath: String = NCCloudEnvSettingService.getInstance(event).ncHomePath
            if (StringUtils.isBlank(homePath)) {
                Messages.showMessageDialog("请先设置NC Home", "Error", Messages.getErrorIcon())
                return
            }
            val filename = File(homePath).path + "/ierp/bin/prop.xml"
            val file = File(filename)
            if (!file.exists()) {
                Messages.showMessageDialog("file :prop.xml not exists!", "Error", Messages.getErrorIcon())
                return
            }
            val envs = conf.envs
            if (serverFlag) {
                conf.mainClassName = serverClass
                var exModulesStr: String = NCCloudEnvSettingService.getInstance(event).ex_modules

                val modules = exModulesStr.split(",")
                if (modules.isNotEmpty()) {
                    exModulesStr = ""
                    for (module in modules) {
                        if (ModulesUtil.defaultMustModules.contains(module)) {
                            exModulesStr = "$exModulesStr$module,"
                        }
                    }
                }
                if (exModulesStr.isNotEmpty()) {
                    exModulesStr = exModulesStr.substring(0, exModulesStr.length - 1)
                }
                NCCloudEnvSettingService.getInstance(event).ex_modules = exModulesStr

                envs["FIELD_EX_MODULES"] = exModulesStr
                var hotwebs = envs["FIELD_HOTWEBS"]
                if (StringUtils.isBlank(hotwebs)) {
                    hotwebs = "nccloud,fs"
                }
                envs["FIELD_HOTWEBS"] = hotwebs
                envs["FIELD_ENCODING"] = "UTF-8"
                var timeZone = envs["FIELD_TIMEZONE"]
                if (StringUtils.isBlank(timeZone)) {
                    timeZone = "GMT+8"
                }
                envs["FIELD_TIMEZONE"] = timeZone
                conf.vmParameters = "-Dnc.exclude.modules=\$FIELD_EX_MODULES$\n" +
                        "-Dnc.runMode=develop\n" +
                        "-Dnc.server.location=\$FIELD_NC_HOME$\n" +
                        "-DEJBConfigDir=\$FIELD_NC_HOME$/ejbXMLs\n" +
                        "-DExtServiceConfigDir=\$FIELD_NC_HOME$/ejbXMLs\n" +
                        "-Duap.hotwebs=\$FIELD_HOTWEBS$\n" +
                        "-Duap.disable.codescan=false\n" +
                        "-Xmx1024m\n" +
                        "-XX:MetaspaceSize=128m\n" +
                        "-XX:MaxMetaspaceSize=512m\n" +
                        "-Dorg.owasp.esapi.resources=\$FIELD_NC_HOME$/ierp/bin/esapi\n" +
                        "-Dfile.encoding=\$FIELD_ENCODING$\n" +  // 默认添加时区
                        "-Duser.timezone=\$FIELD_TIMEZONE$\n"
            } else {
                // ip和端口号读取home中的，没有就取默认值127.0.0.1:80
//                var ipAndPort = IpAndPort()
//                ipAndPort.address = DEFALUT_IP
//                ipAndPort.port = DEFALUT_PORT
//                try {
//                    val domainInfo: DomainInfo = propXml.loadPropInfo(file).getDomain()
//                    var serverInfo: SingleServerInfo = domainInfo.getServer()
//                    //如果severInfo拿不到，尝试判断是集群配置，获取主服务配置
//                    if (serverInfo == null) {
//                        val clusterInfo: ClusterInfo = domainInfo.getCluster()
//                        if (clusterInfo != null) {
//                            serverInfo = clusterInfo.getMgr()
//                        }
//                    }
//                    if (serverInfo != null) {
//                        // 优先http，然后是https，然后是ajp
//                        if (!serverInfo.http.isNullOrEmpty()) {
//                            ipAndPort = serverInfo.http!![0]
//                        } else if (!serverInfo.http.isNullOrEmpty()) {
//                            ipAndPort = serverInfo.https!![0]
//                        } else if (!serverInfo.ajp.isNullOrEmpty()) {
//                            ipAndPort = serverInfo.ajp!![0]
//                        }
//                    }
//                } catch (e: Exception) {
//                    throw Exception("please check the file :prop.xml\n" + e.message)
//                }
//
//                //本地调试移动用127.0.0.1
//                ipAndPort.address = DEFALUT_IP
//                envs["FIELD_CLINET_IP"] =
//                    if (StringUtils.isBlank(ipAndPort.address)) DEFALUT_IP else ipAndPort.address
//                envs["FIELD_CLINET_PORT"] =
//                    java.lang.String.valueOf(if (ipAndPort.port == null) DEFALUT_PORT else ipAndPort.port)
//                conf.mainClassName = clientClass
//                conf.setVMParameters(
//                    ("-Dnc.runMode=develop\n" +
//                            " -Dnc.jstart.server=\$FIELD_CLINET_IP$\n" +
//                            " -Dnc.jstart.port=\$FIELD_CLINET_PORT$\n" +
//                            " -Xmx768m -XX:MaxPermSize=256m\n" +
//                            " -Dnc.fi.autogenfile=N")
//                )
            }
            envs["FIELD_NC_HOME"] = homePath
            conf.setModule(selectModule)
            conf.envs = envs
            conf.workingDirectory = homePath
            conf.shortenCommandLine = ShortenCommandLine.CLASSPATH_FILE
        }
    }
}