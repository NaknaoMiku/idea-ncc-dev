package com.summer.lijiahao.script.common.powerdesigner.util;

import com.intellij.openapi.vfs.VirtualFile;
import com.summer.lijiahao.script.common.powerdesigner.core.Pdm;
import com.summer.lijiahao.script.common.powerdesigner.exception.PDMParseRuntimeException;
import com.summer.lijiahao.script.common.powerdesigner.impl.PropertyComparator;
import com.summer.lijiahao.script.common.powerdesigner.itf.IDataDictionaryGenerator;
import com.summer.lijiahao.script.pub.db.model.IColumn;
import com.summer.lijiahao.script.pub.db.model.IIndex;
import com.summer.lijiahao.script.pub.db.model.ITable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DataDictionaryGeneratorFactory {
    //    protected static Logger logger = LoggerFactory.getLogger(DataDictionaryGeneratorFactory.class.getName());
    private static final IDataDictionaryGenerator ddGenerator = new DataDictionaryGenerator();

    public static IDataDictionaryGenerator getInstance() {
        return ddGenerator;
    }

    static class DataDictionaryGenerator implements IDataDictionaryGenerator {
        static final String TEMPL_ENCODING = "UTF-8";

        static final String DD_TEMPL_PATH = "nc/uap/studio/build/pdm/vm/";

        static final String INDEX_TEMPL_NAME = "index.templ";

        static final String HEADER_TEMPL_NAME = "header.templ";

        static final String HOME_TEMPL_NAME = "home.templ";

        static final String LEFT_NAVIGATION_TEMPL_NAME = "leftNavigation.templ";

        static final String TABLE_CONTENT_TEMPL_NAME = "tableContent.templ";

        private VelocityEngine ve;

        public DataDictionaryGenerator() {
            initVelocityEngine();
        }

        public void generate(VirtualFile pdmFile, boolean geneReference, File ddRoot) {
            PdmUtil.validatePdm(pdmFile);
            Pdm pdm = PdmUtil.parsePdm(pdmFile, geneReference);
            String pdmFileName = pdmFile.getName();
            int indexOfLastDot = -1;
            if ((indexOfLastDot = pdmFileName.lastIndexOf(".")) != -1)
                pdmFileName = pdmFileName.substring(0, indexOfLastDot).toLowerCase();
            if (ddRoot.exists())
                try {
                    FileUtils.forceDelete(ddRoot);
                } catch (IOException iOException) {
                    throw new PDMParseRuntimeException("????????????" + ddRoot.getPath() + "?????????");
                }
            if (!ddRoot.mkdirs())
                throw new PDMParseRuntimeException("????????????" + ddRoot.getPath() + "?????????");
            Writer writer = null;
            Map<String, List<IIndex>> tableMapIndexs = new HashMap<String, List<IIndex>>();
            for (IIndex index : pdm.getIndexs()) {
                List<IIndex> tableIndexs = null;
                if ((tableIndexs = tableMapIndexs.get(index.getTable().getName())) == null) {
                    tableIndexs = new ArrayList<IIndex>();
                    tableMapIndexs.put(index.getTable().getName(), tableIndexs);
                }
                tableIndexs.add(index);
            }
            try {
                try {
                    Template indexTempl = this.ve.getTemplate("nc/uap/studio/build/pdm/vm/index.templ", "UTF-8");
                    VelocityContext vc = new VelocityContext();
                    vc.put("pdmName", pdmFileName);
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ddRoot, "nc_dd_index.html")), StandardCharsets.UTF_8));
                    indexTempl.merge(vc, writer);
                } finally {
                    IOUtils.closeQuietly(writer);
                }
                writer = null;
                try {
                    Template headerTempl = this.ve.getTemplate("nc/uap/studio/build/pdm/vm/header.templ", "UTF-8");
                    VelocityContext vc = new VelocityContext();
                    vc.put("pdmName", pdmFileName);
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ddRoot, "nc_dd_header.html")), StandardCharsets.UTF_8));
                    headerTempl.merge(vc, writer);
                } finally {
                    IOUtils.closeQuietly(writer);
                }
                writer = null;
                try {
                    Template headerTempl = this.ve.getTemplate("nc/uap/studio/build/pdm/vm/home.templ", "UTF-8");
                    VelocityContext vc = new VelocityContext();
                    vc.put("pdmName", pdmFileName);
                    vc.put("reportName", pdmFileName);
                    vc.put("moduleName", pdmFileName);
                    vc.put("geneDatetime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ddRoot, "nc_dd_doc_home.html")), StandardCharsets.UTF_8));
                    headerTempl.merge(vc, writer);
                } finally {
                    IOUtils.closeQuietly(writer);
                }
                writer = null;
                try {
                    Template indexTempl = this.ve.getTemplate("nc/uap/studio/build/pdm/vm/leftNavigation.templ", "UTF-8");
                    VelocityContext vc = new VelocityContext();
                    vc.put("pdmName", pdmFileName);
                    List<ITable> tables = new ArrayList<ITable>(pdm.getTables());
                    Collections.sort(tables, new PropertyComparator("name"));
                    vc.put("tables", tables);
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ddRoot, "nc_dd_navigator.html")), StandardCharsets.UTF_8));
                    indexTempl.merge(vc, writer);
                } finally {
                    IOUtils.closeQuietly(writer);
                }
                Template tableContentTempl = this.ve.getTemplate("nc/uap/studio/build/pdm/vm/tableContent.templ", "UTF-8");
                for (ITable table : pdm.getTables()) {
                    List<String> pkColNames = new ArrayList<String>();
                    if (table.getPkConstraint() != null && table.getPkConstraint().getColumns() != null)
                        for (IColumn pkCol : table.getPkConstraint().getColumns())
                            pkColNames.add(pkCol.getName());
                    VelocityContext vc = new VelocityContext();
                    vc.put("table", table);
                    vc.put("indexs", tableMapIndexs.get(table.getName()));
                    vc.put("pkColNames", pkColNames);
                    writer = null;
                    try {
                        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ddRoot, table.getName() + ".html")), StandardCharsets.UTF_8));
                        tableContentTempl.merge(vc, writer);
                    } finally {
                        IOUtils.closeQuietly(writer);
                    }
                }
            } catch (Exception e) {
//                logger.error("??????PDM(" + pdmFile.getPath() + ")????????????????????????", e);
                throw new PDMParseRuntimeException("??????PDM(" + pdmFile.getPath() + ")????????????????????????");
            }
            String resourceDir = "D:/work/cc_workspace/huping_NC_DSGN_SDP5.6_dev_2/NC5_DSGN5.0_VOB/NC_DSGN_SDP/dbscript/nc/sdp/build/dbcreate/dd/resource";
            try {
                FileUtils.copyDirectory(new File(resourceDir), ddRoot);
            } catch (IOException e) {
//                logger.error("??????????????????(" + resourceDir + ")???(" + ddRoot + ")?????????", e);
                throw new PDMParseRuntimeException("");
            }
        }

        private void initVelocityEngine() {
            this.ve = new VelocityEngine();
            Properties prop = new Properties();
            prop.setProperty("file.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            try {
                this.ve.init(prop);
            } catch (Exception e) {
//                logger.error(e.getMessage(), e);
                throw new PDMParseRuntimeException("?????????VelocityEngine?????????");
            }
        }
    }
}
