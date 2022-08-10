package com.summer.lijiahao.devconfig.action.button;

import com.intellij.openapi.ui.Messages;
import com.summer.lijiahao.abs.AbstractButtonAction;
import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.base.BusinessException;
import com.summer.lijiahao.devconfig.DevConfigDialog;
import com.summer.lijiahao.library.util.LibrariesUtil;

import javax.swing.JTextField;
import java.awt.event.ActionEvent;

public class SetLibraryAction extends AbstractButtonAction {
    public SetLibraryAction(AbstractDialog dialog) {
        super(dialog);
    }

    @Override
    public void doAction(ActionEvent event) throws BusinessException {

        DevConfigDialog dialog = (DevConfigDialog) getDialog();
        String homePath = dialog.getComponent(JTextField.class, "homeText").getText();

        LibrariesUtil.setLibraries(homePath);
        Messages.showInfoMessage("设置完成！", "提示");
        dialog.setLibFlag(true);
    }
}
