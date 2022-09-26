package com.summer.lijiahao.script.pub.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PrintIOUtil {

//    protected static Logger logger = LoggerFactory.getLogger(PrintIOUtil.class.getName());

    private static PrintIOUtil instance = null;
    private final Map<String, List<String>> sqlMap = new HashMap();

    public static PrintIOUtil getInstance() {
        if (instance == null)
            instance = new PrintIOUtil();
        return instance;
    }

    public boolean printSQLFile() {
        Set<String> fileNameSet = this.sqlMap.keySet();
        if (fileNameSet == null || fileNameSet.size() == 0)
            return false;
        try {
            for (String fileName : fileNameSet) {
                PrintWriter writer = null;
                List<String> sqlList = this.sqlMap.get(fileName);
                if (sqlList == null || sqlList.size() == 0)
                    continue;
                Collections.sort(sqlList);
                try {
                    writer = new PrintWriter(new OutputStreamWriter(
                            new FileOutputStream(new File(fileName), false),
                            StandardCharsets.UTF_8));
                    for (String sql : sqlList)
                        writer.println(sql);
                } catch (Exception e) {
//                    logger.error(e.getMessage(), e);
                    IOUtils.closeQuietly(writer);
                } finally {
                    IOUtils.closeQuietly(writer);
                }
            }
        } finally {
            this.sqlMap.clear();
        }
        return true;
    }

    public boolean resaveSql(List<String> sqlList, File folder, String mapFileNo) {
        if (sqlList != null && sqlList.size() > 0) {
            String fileName = folder.getAbsolutePath() + IOUtils.DIR_SEPARATOR +
                    mapFileNo + ".sql";
            List<String> list = this.sqlMap.get(fileName);
            if (list == null) {
                list = new ArrayList<String>();
                this.sqlMap.put(fileName, list);
            }
            list.addAll(sqlList);
        }
        return true;
    }
}
