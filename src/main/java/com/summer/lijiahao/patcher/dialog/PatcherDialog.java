package com.summer.lijiahao.patcher.dialog;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.summer.lijiahao.abs.AbstractDialog;
import com.summer.lijiahao.base.NccEnvSettingService;
import com.summer.lijiahao.patcher.util.ExportPatcherUtil;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class PatcherDialog extends AbstractDialog {

    private final AnActionEvent event;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField savePath;
    private JButton fileChooseBtn;
    private JPanel filePanel;
    private JTextField patcherName;
    private JTextField serverName;
    private JCheckBox srcFlagCheckBox;
    private JProgressBar progressBar;
    private JPanel logPanel;
    private JCheckBox cloudFlagCheckBox;
    private JBList fieldList;

    public PatcherDialog(final AnActionEvent event) {
        this.event = event;
        setTitle("export ncc patcher...");

        logPanel.setVisible(false);
        patcherName.setEditable(true);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
//        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new MyWindowAdapter());

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        NccEnvSettingService envSettingService = NccEnvSettingService.getInstance(event.getProject());
        String text = envSettingService == null ? null : envSettingService.getLastPatcherPath();

        savePath.setText(text);
        // 保存路径按钮事件
        fileChooseBtn.addActionListener(e -> {
            String userDir = System.getProperty("user.home");
            JFileChooser fileChooser = new JFileChooser(userDir/** + "/Desktop"**/);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int flag = fileChooser.showOpenDialog(null);
            if (flag == JFileChooser.APPROVE_OPTION) {
                savePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

    }

    private void onOK() {
        // 条件校验
        if (null == patcherName.getText() || "".equals(patcherName.getText())) {
            Messages.showErrorDialog(this, "Please set patcher name!", "Error");
            return;
        }
        if (null == savePath.getText() || "".equals(savePath.getText())) {
            Messages.showErrorDialog(this, "Please select save path!", "Error");
            return;
        }
        ListModel<VirtualFile> model = fieldList.getModel();
        if (model.getSize() == 0) {
            Messages.showErrorDialog(this, "Please select export file!", "Error");
            return;
        }

        String exportPath = savePath.getText();
        NccEnvSettingService envSettingService = NccEnvSettingService.getInstance(event.getProject());
        if (envSettingService != null) {
            envSettingService.setLastPatcherPath(exportPath);
        }

        boolean srcFlag = srcFlagCheckBox.isSelected();
        boolean cloudFlag = cloudFlagCheckBox.isSelected();

        // 设置当前进度值
        logPanel.setVisible(true);
        progressBar.setValue(0);
        // 绘制百分比文本（进度条中间显示的百分数）
        progressBar.setStringPainted(true);
        progressBar.addChangeListener(e -> {
            Dimension dimension = progressBar.getSize();
            Rectangle rect = new Rectangle(0, 0, dimension.width, dimension.height);
            progressBar.paintImmediately(rect);
        });

//        try {
        // 修复导出报错 --chenshuaim
        SwingUtilities.invokeLater(() -> {
            ExportPatcherUtil util = new ExportPatcherUtil(patcherName.getText(), serverName.getText(), exportPath, srcFlag, cloudFlag, event);
            try {
                util.exportPatcher(progressBar);
                String zipName = util.getZipName();
                if (StringUtils.isBlank(zipName)) {
                    zipName = "no files export , please build project , or select src retry !";
                } else {
                    zipName = "outFile : " + zipName;
                }
                Messages.showInfoMessage("Success!\n" + zipName, "Tips");
                dispose();
            } catch (Exception e) {
                Messages.showErrorDialog(e.getMessage(), "Error");
            } finally {
                util.delete(new File(util.getExportPath()));
                dispose();
            }

        });
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

        // add your code here
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        VirtualFile[] data = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        assert data != null;
        fieldList = new JBList(data);
        fieldList.setEmptyText("No file selected!");
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        filePanel = decorator.createPanel();
    }

    private class MyWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            onCancel();
        }
    }
}
