package com.summer.lijiahao.upm.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.summer.lijiahao.base.BaseUtil;
import com.summer.lijiahao.base.NccEnvSettingService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EjbConfCopyUtil {
//    public void copyToNCHome(AnActionEvent event) throws Exception {
//
//        String homePath = NccEnvSettingService.getInstance().getNcHomePath();
//        //当前工程
//        Project project = event.getProject();
//        VirtualFile[] selectFile = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
//
//        Map<Module, List<VirtualFile>> moduleMap = new HashMap<>();
//
//        //按模块区分
//        for (VirtualFile virtualFile : selectFile) {
//            Module module = ModuleUtil.findModuleForFile(virtualFile, project);
//            List<VirtualFile> list = moduleMap.get(module);
//            if (list == null) {
//                list = new ArrayList<>();
//            }
//            list.add(virtualFile);
//            moduleMap.put(module, list);
//        }
//
//        //按模块递归复制
//        for (Map.Entry<Module, List<VirtualFile>> entry : moduleMap.entrySet()) {
//            List<String> list = getUpmList(entry.getValue());
//            String ncModuleName = getNCModuleName(entry.getKey());
//            for (String s : list) {
//                String toPath = homePath + File.separator + "modules" + File.separator + ncModuleName + File.separator + "META-INF" + File.separator + new File(s).getName();
//                FileUtil.copy(new File(s), new File(toPath));
//            }
//        }
//    }

//    /**
//     * 筛选upm文件
//     *
//     * @param virtualFileList
//     * @return
//     */
//    private List<String> getUpmList(List<VirtualFile> virtualFileList) {
//        Set<String> set = new HashSet<>();
//        if (null != virtualFileList) {
//            for (VirtualFile v : virtualFileList) {
//                set.addAll(getFileUrl(v.getPath()));
//            }
//        }
//        return new ArrayList<>(set);
//    }

    /**
     * 递归路径获取可导出的文件
     *
     * @param filePath
     * @param fileUrlSet
     */
    private Set<String> getFileUrl(String filePath) {
        Set<String> fileUrlSet = new HashSet<>();
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] childrenFile = file.listFiles();
            if (childrenFile == null) {
                return fileUrlSet;
            }
            for (File childFile : childrenFile) {
                fileUrlSet.addAll(getFileUrl(childFile.getPath()));
            }
        } else {
            if ((filePath.endsWith(".rest") || filePath.endsWith(".upm")) && new File(filePath).getParent().endsWith("META-INF")) {
                fileUrlSet.add(filePath);
            }
        }
        return fileUrlSet;
    }

    /**
     * 只拷贝选中的模块的upm文件到home
     *
     * @param event
     */
    public void copy(AnActionEvent event) {
        String homePath = NccEnvSettingService.getInstance().getNcHomePath();
        Module module = BaseUtil.getModule(event);
        String ncModuleName = getNCModuleName(module);
        Set<String> fileUrls;
        if (module.getModuleFile() == null) {
            return;
        }
        // 目标路径，但是缺少文件名字
        String toPath = homePath + File.separator + "modules" + File.separator + ncModuleName + File.separator
                + "META-INF" + File.separator;
        // 获取某个模块下所有的upm文件
        fileUrls = getFileUrl(module.getModuleFile().getParent().getPath());
        List<String> errorList = new ArrayList<>();
        for (String fileUrl : fileUrls) {
            File file = new File(fileUrl);
            try {
                // 逐个拷贝到home
                FileUtil.copy(file, new File(toPath + file.getName()));
            } catch (IOException ignored) {
                errorList.add(file.getName());
            }
        }
        if (!errorList.isEmpty()) {
            Messages.showMessageDialog("文件" + errorList + "拷贝出错", "Error", Messages.getErrorIcon());
        }
    }

    /**
     * 获取nc模块名称
     *
     * @param module
     * @return
     */
    private String getNCModuleName(Module module) {

        String ncModuleName = null;
        VirtualFile virtualFile = module.getModuleFile();
        if (virtualFile == null) {
            return null;
        }
        String modulePath = virtualFile.getParent().getPath();
        try {
            File file = new File(modulePath + File.separator + "META-INF" + File.separator + "module.xml");
            if (file.exists()) {
                InputStream in = new FileInputStream(file);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(in);
                Element root = doc.getDocumentElement();
                ncModuleName = root.getAttribute("name");
            }
        } catch (Exception e) {
            //抛错就认为不是nc项目
        }
        return ncModuleName;
    }
}
