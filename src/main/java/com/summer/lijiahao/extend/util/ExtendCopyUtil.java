package com.summer.lijiahao.extend.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.summer.lijiahao.base.BaseUtil;
import com.summer.lijiahao.base.NccEnvSettingService;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ExtendCopyUtil {

    public static final int BUFFER_SIZE = 8192;
    /**
     * 项目的鉴权文件路径
     */
    public static final String PROJECT_CONFIG_FILE_PATH = File.separator + "src" + File.separator + "client" + File.separator + "yyconfig";
    /**
     * 模块的鉴权文件路径
     */
    public static final String HOME_CONFIG_FILE_PATH = File.separator + "hotwebs" + File.separator + "nccloud" + File.separator + "WEB-INF" + File.separator
            + "extend" + File.separator + "yyconfig";

//    public void copyToNCHome1(AnActionEvent event) throws Exception {
//
//        String homePath = NccEnvSettingService.getInstance().getNcHomePath();
//
//        VirtualFile[] selectFile = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
//        List<String> authFileList = getAuthFileList(selectFile);
//
//        for (String file : authFileList) {
//            String toPath = homePath + File.separator + "hotwebs" + File.separator + "nccloud" + File.separator + "WEB-INF" + File.separator
//                    + "extend" + File.separator + "yyconfig" + File.separator + "modules";
//            String filePath = file.split(Matcher.quoteReplacement("yyconfig" + File.separator + "modules"))[1];
//            toPath += filePath;
//            FileUtil.copy(new File(file), new File(toPath));
//        }
//    }

    /**
     * //     * 获取鉴权文件
     * //     *
     * //     * @param selectFile
     * //     * @return
     * //
     */
//    private List<String> getAuthFileList(VirtualFile[] selectFile) {
//        Set<String> fileUrlSet = new HashSet<>();
//        if (null != selectFile) {
//            for (VirtualFile file : selectFile) {
//                getFileUrl(file.getPath(), fileUrlSet);
//            }
//        }
//        return new ArrayList<>(fileUrlSet);
//    }

//    /**
//     * 递归路径获取可导出的文件
//     *
//     * @param filePath
//     * @param fileUrlSet
//     */
//    private void getFileUrl(String filePath, Set<String> fileUrlSet) {
//
//        if (filePath.contains("src")) {
//            File file = new File(filePath);
//            if (file.isDirectory()) {
//                File[] childrenFile = file.listFiles();
//                for (File childFile : childrenFile) {
//                    getFileUrl(childFile.getPath(), fileUrlSet);
//                }
//            } else {
//                if (filePath.endsWith("xml")) {
//                    filePath = new File(filePath).getPath();
//                    String tag1 = "client" + File.separator + "yyconfig" + File.separator + "modules";
//                    String tag2 = "config" + File.separator + "action";
//                    String tag3 = "config" + File.separator + "authorize";
//                    if ((filePath.contains(tag1) && filePath.contains(tag2))
//                            || (filePath.contains(tag1) && filePath.contains(tag3))) {
//                        fileUrlSet.add(file.getPath());
//                    }
//                }
//            }
//        }
//    }

    /**
     * 拷贝一个模块的鉴权文件到home中，拷贝方法就是将该模块鉴权文件目录下的所有xml文件拷贝到home中的鉴权文件目录下
     *
     * @param event
     * @throws Exception
     */
    public static void copyToNCHome(AnActionEvent event) throws Exception {
        Module module = BaseUtil.getModule(event);
        String homePath = NccEnvSettingService.getInstance().getNcHomePath();
        if (StringUtils.isBlank(homePath)) {
            Messages.showMessageDialog("请先设置NC Home", "Error", Messages.getErrorIcon());
            return;
        }

        VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
        if (contentRoots.length == 0) {
            Messages.showMessageDialog("请先设置NC Home", "Error", Messages.getErrorIcon());
            return;
        }
        String modulePath = contentRoots[0].getPath();
        File file = new File(modulePath);
        if (file.isDirectory()) {
            String[] list = file.list();
            for (String com : list) {
                if (!com.endsWith(".xml") && !com.endsWith(".iml") && !com.equals("META-INF")) {
                    copyDir(contentRoots[0].getPath() + File.separator + com + PROJECT_CONFIG_FILE_PATH, homePath + HOME_CONFIG_FILE_PATH);
                }
            }
        }
    }

    /**
     * 将某个文件下下的所有xml文件拷贝到另一个文件夹
     *
     * @param fromDirPath
     * @param toDirPath
     * @throws IOException
     */
    private static void copyDir(String fromDirPath, String toDirPath) throws IOException {
        File file = new File(fromDirPath);
        String[] subFilePaths = file.list();
        if (subFilePaths == null) {
            return;
        }
        if (!(new File(toDirPath)).exists()) {
            if (!(new File(toDirPath)).mkdir()) {
                Messages.showMessageDialog("Home所在的目录不存在或者，你没有操作home所在目录的权限", "Error", Messages.getErrorIcon());
                return;
            }
        }
        // 递归拷贝
        for (String subFilePath : subFilePaths) {
            File file0 = new File(fromDirPath + File.separator + subFilePath);
            if (file0.isDirectory()) {
                copyDir(fromDirPath + File.separator + subFilePath, toDirPath + File.separator + subFilePath);
            } else if (file0.isFile()) {
                copyXmlFile(fromDirPath + File.separator + subFilePath, toDirPath + File.separator + subFilePath);
            }

        }
    }

    /**
     * 复制xml文件
     *
     * @param fromFilePath
     * @param newFilePath
     * @throws IOException
     */
    private static void copyXmlFile(@NotNull String fromFilePath, @NotNull String newFilePath) throws IOException {
        if (!fromFilePath.endsWith(".xml")) {
            return;
        }
        File fromFile = new File(fromFilePath);
        File toFile = new File(newFilePath);
        FileUtil.copy(fromFile, toFile);
    }
}
