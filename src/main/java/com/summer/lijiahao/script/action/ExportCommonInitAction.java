package com.summer.lijiahao.script.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.summer.lijiahao.abs.AbstractAnAction;
import com.summer.lijiahao.script.dialog.InitDataDialog;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * items预制脚本导出
 */
public class ExportCommonInitAction extends AbstractAnAction {


    @Override
    public void doAction(AnActionEvent event) {

        InitDataDialog dialog = new InitDataDialog();
        VirtualFile selectFile = getSelectFile(event);
        if (selectFile.isDirectory()) {
            return;
        }
        if (!"items.xml".equals(selectFile.getName())) {
            Messages.showErrorDialog("please select items.xml", "tips");
            return;
        }
        //Messages.showErrorDialog(project, psiFile.getPath(), "提示", Messages.getInformationIcon());

        String dsname = dialog.getDsName();
        if (StringUtils.isBlank(dsname)) {
            Messages.showMessageDialog("请设置基准库", "Error", Messages.getErrorIcon());
            return;
        }
        dialog.setItemFile(selectFile);
        dialog.setVisible(true);

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        VirtualFile file = getSelectFile(e);
        //VirtualFile在getPath以后只有"/"，因此不需要使用File.separator
        boolean flag = false;
        if (file != null) {
            flag = file.getPath().contains("script/conf") && file.getPath().endsWith("items.xml");
        }
        e.getPresentation().setEnabledAndVisible(flag);

    }
}
