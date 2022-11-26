package com.summer.lijiahao.utils.openapi;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.summer.lijiahao.utils.openapi.actions.FormatRequestJsonAction;
import com.summer.lijiahao.utils.openapi.actions.SendRequestAction;
import com.summer.lijiahao.utils.openapi.actions.SettingAction;
import com.summer.lijiahao.utils.openapi.ui.OpenApiTool;

public class OpenApiView
        extends SimpleToolWindowPanel {
    private OpenApiTool openApiTool;

    public OpenApiView() {
        super(false, true);
        this.openApiTool = new OpenApiTool();

        DefaultActionGroup barGroup = new DefaultActionGroup();
        barGroup.add(new SendRequestAction(this.openApiTool));
        barGroup.add(new SettingAction());
        barGroup.add(new FormatRequestJsonAction(this.openApiTool));

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("toolbar", barGroup, true);
        toolbar.setTargetComponent(this);
        setToolbar(toolbar.getComponent());
        setContent(this.openApiTool.getComponent());
    }


}
