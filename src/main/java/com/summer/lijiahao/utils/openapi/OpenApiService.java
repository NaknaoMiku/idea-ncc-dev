package com.summer.lijiahao.utils.openapi;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.summer.lijiahao.utils.openapi.setting.OpenApiConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "openapi", storages = {@Storage("$PROJECT_CONFIG_DIR$/openapi.xml")})
public class OpenApiService implements PersistentStateComponent<OpenApiConfig> {
    private OpenApiConfig config = new OpenApiConfig();

    public static OpenApiService getInstance(Project project) {
        return project.getService(OpenApiService.class);
    }

    @Nullable
    public OpenApiConfig getState() {
        return this.config;
    }

    public void loadState(@NotNull OpenApiConfig config) {
        this.config = config;
    }
}
