package com.summer.lijiahao.utils.openapi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class OpenApiToolWin
        implements ToolWindowFactory {
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        OpenApiView openApiView = new OpenApiView();
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(openApiView, "", true);
        toolWindow.getContentManager().addContent(content);
    }
}