package com.summer.lijiahao.script.common.tablestruct.service.impl;

import com.summer.lijiahao.base.BusinessException;
import com.summer.lijiahao.base.NccEnvSettingService;
import com.summer.lijiahao.script.common.tablestruct.model.MainTableCfg;
import com.summer.lijiahao.script.common.tablestruct.service.ICommonTableStructQueryService;
import com.summer.lijiahao.script.common.tablestruct.util.XStreamParser;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

public class URLZipFileQueryService implements ICommonTableStructQueryService {
    public static final String CACHE_FOLDER_NAME = ".tables";
    //    protected static Logger logger = LoggerFactory.getLogger(URLZipFileQueryService.class.getName());
    private static final String DEFAULT_URL = "http://20.10.80.128:12345/cfgs/tables.zip";
    private final NccEnvSettingService envSettingService = NccEnvSettingService.getInstance();
    private final File localLocation;
    private Properties common;
    private List<MainTableCfg> cfgs;

    public URLZipFileQueryService() throws BusinessException {
        localLocation = getDefaultLocalCacheLocation();
    }

    public static URLZipFileQueryService getSingleton() throws BusinessException {
        return new URLZipFileQueryService();
    }

    public void sync() {
        //do nothing
//        File downloadFile = RemoteFileUtil.downloadFile("http://20.10.80.128:12345/cfgs/tables.zip", null);
//        RemoteFileUtil.syncZipFile2LocalFolder(downloadFile.getAbsolutePath(),
//                this.localLocation.getAbsolutePath(), true);
//        this.cfgs = null;
//        this.common = null;
    }

    public Properties getCommonMapping() {
        if (this.common == null) {
            if (isEmpty(this.localLocation)) {
                sync();
            }
            File file = new File(this.localLocation, "/common/mapping.properties");
            if (file.exists() && file.isFile()) {
                this.common = new Properties();
                InputStreamReader reader = null;
                try {
                    reader = new InputStreamReader(new FileInputStream(file),
                            StandardCharsets.UTF_8);
                    this.common.load(reader);
                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
                } catch (IOException e) {
//                    e.printStackTrace();
                } finally {
                    if (reader != null)
                        try {
                            reader.close();
                        } catch (IOException iOException) {
                        }
                }
            } else {
                System.out.println("未找到公共配置");
                return new Properties();
            }
        }
        return this.common;
    }

    public List<MainTableCfg> getCommonMainTableCfgs() throws BusinessException {
        if (this.cfgs == null) {
            if (isEmpty(this.localLocation)) {
                sync();
            }
            List<File> files = findXmlFiles(this.localLocation + File.separator +
                    "/common/tablerule");
            if (files.size() > 0) {
                this.cfgs = new ArrayList();
                for (File xmlFile : files) {
                    InputStream input = null;
                    try {
                        input = new FileInputStream(xmlFile);
                        MainTableCfg mtCfg =
                                XStreamParser.getMainTableCfg(input);
                        if (mtCfg != null)
                            this.cfgs.add(mtCfg);
                    } catch (IOException e) {
//                        logger.error(e.getMessage(), e);
                        if (input != null)
                            try {
                                input.close();
                                continue;
                            } catch (IOException iOException) {
                                continue;
                            }
                    } finally {
                        if (input != null)
                            try {
                                input.close();
                            } catch (IOException iOException) {
                            }
                    }
                }
            } else {
                throw new BusinessException("can't find tables config:" + this.localLocation + File.separator +
                        "common" + File.separator + "tablerule");
            }
        }
        return this.cfgs;
    }

    private List<File> findXmlFiles(String path) {
        List<File> results = new ArrayList<File>();
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            Stack<File> stack = new Stack<File>();
            stack.add(folder);
            while (!stack.isEmpty()) {
                File pop = stack.pop();
                byte b;
                int i;
                File[] arrayOfFile = pop.listFiles();
                for (b = 0; arrayOfFile != null && b < arrayOfFile.length; b++) {
                    File child = arrayOfFile[b];
                    if (child.isDirectory()) {
                        stack.add(child);
                    } else if (child.getName().toLowerCase().endsWith(".xml")) {
                        results.add(child);
                    }
                }
            }
        }
        return results;
    }

    protected File getDefaultLocalCacheLocation() throws BusinessException {
        String path = envSettingService.getTablesPath();
        if (StringUtils.isBlank(path)) {
            throw new BusinessException("please set tables path");
        }
        File folder = new File(path);
        if (!folder.exists()) {
            throw new BusinessException("tables path is wrong ,can't find config");
        }
        return folder;
    }

    protected boolean isEmpty(File file) {
        if (file.isDirectory())
            return (file.list().length == 0);
        return false;
    }
}
