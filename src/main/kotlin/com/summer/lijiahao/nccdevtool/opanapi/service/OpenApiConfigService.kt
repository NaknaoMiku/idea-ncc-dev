package com.summer.lijiahao.nccdevtool.opanapi.service

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project

@State(name = "openapi", storages = [Storage("\$PROJECT_CONFIG_DIR$/openapi.xml")])
class OpenApiConfigService : PersistentStateComponent<OpenApiConfig?> {
    private var config: OpenApiConfig = OpenApiConfig()
    override fun getState(): OpenApiConfig {
        return config
    }

    override fun loadState(config: OpenApiConfig) {
        this.config = config
    }

    companion object {
        fun getInstance(project: Project): OpenApiConfigService {
            return project.getService(OpenApiConfigService::class.java)
        }
    }
}
