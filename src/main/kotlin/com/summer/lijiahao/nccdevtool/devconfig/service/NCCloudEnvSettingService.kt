package com.summer.lijiahao.nccdevtool.devconfig.service

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import org.jdom.Element


@State(name = "NCCloudEnvSetting", storages = [Storage(value = "\$PROJECT_CONFIG_DIR$/NCCloudEnvSetting.xml")])
class NCCloudEnvSettingService : PersistentStateComponent<Element> {

    private val ATTR_NCHOME_PATH = "ncHomePath"
    private val ATTR_TABLES_PATH = "tablesPath"
    private val ATTR__EX_MODULES = "ex_modules"
    private val ATTR_MUST_MODULES = "must_modules"
    private val ATTR_CONNECTD_DATA_SOURCE = "connectd_data_source"
    private val ATTR_NCCLOUDJAR = "nccloudJar"
    private val ATTR_NCCHRJAR = "ncchrJar"
    private val ATTR_LASTPATCHERPATH = "lastPatcherPath"
    var ncHomePath: String = ""
    var tablesPath: String = ""
    var ex_modules: String = ""
    var must_modules: String = ""
    var connected_data_source: String = ""
    var nccloudJar: String = ""
    var ncchrJAR: String = ""
    var lastPatcherPath: String = ""

    override fun getState(): Element {
        val element = Element("NCCloudEnvSetting")
        element.setAttribute(ATTR_NCHOME_PATH, ncHomePath)
        element.setAttribute(ATTR_TABLES_PATH, tablesPath)
        element.setAttribute(ATTR__EX_MODULES, ex_modules)
        element.setAttribute(ATTR_MUST_MODULES, must_modules)
        element.setAttribute(ATTR_CONNECTD_DATA_SOURCE, connected_data_source)
        element.setAttribute(ATTR_NCCLOUDJAR, nccloudJar)
        element.setAttribute(ATTR_NCCHRJAR, ncchrJAR)
        element.setAttribute(ATTR_LASTPATCHERPATH, lastPatcherPath)
        return element
    }

    override fun loadState(state: Element) {
        state.getAttributeValue(ATTR_NCHOME_PATH)?.let { this.ncHomePath = state.getAttributeValue(ATTR_NCHOME_PATH) }
        state.getAttributeValue(ATTR_TABLES_PATH)?.let { this.tablesPath = state.getAttributeValue(ATTR_TABLES_PATH) }
        state.getAttributeValue(ATTR__EX_MODULES)?.let { this.ex_modules = state.getAttributeValue(ATTR__EX_MODULES) }
        state.getAttributeValue(ATTR_MUST_MODULES)
            ?.let { this.must_modules = state.getAttributeValue(ATTR_MUST_MODULES) }
        state.getAttributeValue(ATTR_CONNECTD_DATA_SOURCE)
            ?.let { this.connected_data_source = state.getAttributeValue(ATTR_CONNECTD_DATA_SOURCE) }
        state.getAttributeValue(ATTR_NCCLOUDJAR)?.let { this.nccloudJar = state.getAttributeValue(ATTR_NCCLOUDJAR) }
        state.getAttributeValue(ATTR_NCCHRJAR)?.let { this.ncchrJAR = state.getAttributeValue(ATTR_NCCHRJAR) }
        state.getAttributeValue(ATTR_LASTPATCHERPATH)
            ?.let { this.lastPatcherPath = state.getAttributeValue(ATTR_LASTPATCHERPATH) }

    }

    companion object {
        @JvmStatic
        fun getInstance(event: AnActionEvent): NCCloudEnvSettingService {
            return event.project!!.getService(NCCloudEnvSettingService::class.java)
        }
    }
}