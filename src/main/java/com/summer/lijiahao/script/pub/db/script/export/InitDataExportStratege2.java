package com.summer.lijiahao.script.pub.db.script.export;

import com.intellij.openapi.vfs.VirtualFile;
import com.summer.lijiahao.script.pub.db.exception.DatabaseRuntimeException;
import com.summer.lijiahao.script.pub.db.model.IColumn;
import com.summer.lijiahao.script.pub.db.model.IPkConstraint;
import com.summer.lijiahao.script.pub.db.model.ITable;
import com.summer.lijiahao.script.pub.db.model.TableStructure;
import com.summer.lijiahao.script.pub.db.query.SqlQueryResultSet;
import com.summer.lijiahao.script.pub.util.PrintIOUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

public class InitDataExportStratege2 implements IScriptExportStratege, IExportConst {
    //    protected static Logger logger = LoggerFactory.getLogger(PrintIOUtil.class.getName());
    public static final String FLODER_BUSINESS = "business";

    public static final String FOLDER_ML = "dbml";

    public static final String DEFAULT_LANG = "simpchn";

    private final VirtualFile root;

    private final String mapName;

    private final Map<String, String> tableFilenameMap;

    private final boolean isBusiness;

    private final Map<String, List<String>> mlTableInfo;
    private final TableStructure struct;
    private String langCode;
    private int autoFileNo;

    public InitDataExportStratege2(VirtualFile folder, boolean isBusiness, String mapName, TableStructure struct, Map<String, String> tableNoMap, String langCode, Map<String, List<String>> mlTableInfo) {
        this.root = folder;
        this.isBusiness = isBusiness;
        this.mapName = mapName;
        this.struct = struct;
        this.tableFilenameMap = tableNoMap;
        this.langCode = langCode;
        this.mlTableInfo = mlTableInfo;
        this.autoFileNo = 0;
    }

    private String getAutoFileNo() {
        return String.valueOf(++this.autoFileNo);
    }

    public boolean export(SqlQueryInserts inserts) {
        if (this.root == null)
            return false;
        if ((!this.root.exists()) || (
                this.root.exists() && !this.root.isDirectory()))
            return false;
        ITable table = inserts.getTable();
        File folder = null;
        if (this.isBusiness) {
            folder = new File(this.root.getPath() + IOUtils.DIR_SEPARATOR +
                    "business" + IOUtils.DIR_SEPARATOR + this.mapName +
                    IOUtils.DIR_SEPARATOR + table.getName());
        } else {
            folder = new File(this.root.getPath() + IOUtils.DIR_SEPARATOR +
                    this.mapName + IOUtils.DIR_SEPARATOR + table.getName());
        }
        if ((!folder.exists() && !folder.mkdirs()) || (
                folder.exists() && !folder.isDirectory()))
            return false;
        boolean flag = this.isBusiness ? exportBusinessScript(inserts, folder) :
                exportNotBusinessScript(inserts, folder);
        PrintIOUtil.getInstance().printSQLFile();
        return flag;
    }

    private boolean exportNotBusinessScript(SqlQueryInserts inserts, File folder) {
        List<Map<String, Object>> resultSet = inserts.getResultSet()
                .getResults();
        List<String> sqls = inserts.getResults();
        boolean includeDeleted = (resultSet.size() != sqls.size());
        for (int i = 0; i < resultSet.size(); i++) {
            Map<String, Object> map = resultSet.get(i);
            ITable currentTable = inserts.getResultSet().getTable();
            IPkConstraint pkConstraint = currentTable.getPkConstraint();
            if (pkConstraint == null || pkConstraint.getColumns() == null ||
                    pkConstraint.getColumns().size() == 0) {
//                logger.error(currentTable.getName() + "??????????????????????????????????????????");
                return false;
            }
            String pkName = pkConstraint.getColumns().get(0).getName();
            String pkField = map.get(pkName).toString();
            String singleFolder = currentTable.getName() + "_" + pkField;
            File scriptFolder = new File(folder.getAbsolutePath() +
                    IOUtils.DIR_SEPARATOR + singleFolder);
            if ((!scriptFolder.exists() && !scriptFolder.mkdirs()) || (
                    scriptFolder.exists() && !scriptFolder.isDirectory()))
                return false;
            cleanFolder(scriptFolder);
            String mapNo = getFileNo(this.struct.getTable());
            List<String> sqlList = new LinkedList<String>();
            if (includeDeleted) {
                int p = 2 * i;
                if (sqls.size() > p && sqls.get(p) != null)
                    sqlList.add(sqls.get(p));
                p++;
                if (sqls.size() > p && sqls.get(p) != null)
                    sqlList.add(sqls.get(p));
            } else if (sqls.size() > i && sqls.get(i) != null) {
                sqlList.add(sqls.get(i));
            }
            boolean isSucess = PrintIOUtil.getInstance().resaveSql(sqlList,
                    scriptFolder, mapNo);
            if (!isSucess)
                return isSucess;
            List<IColumn> blobColumns = getBlobColumns(currentTable);
            if (blobColumns.size() > 0)
                if (currentTable.getPkConstraint() == null ||
                        currentTable.getPkConstraint().getColumns() == null ||
                        currentTable.getPkConstraint().getColumns().size() == 0) {
//                    logger.warn(currentTable.getName() + "?????????????????????????????????BLOB???");
                } else if (currentTable.getPkConstraint().getColumns().size() == 1) {
                    generateBlobFile(currentTable, map, pkName, blobColumns,
                            scriptFolder.getAbsolutePath(), mapNo);
                } else {
//                    logger.warn(currentTable.getName() + "?????????????????????????????????BLOB???");
                }
            List<TableStructure> subTables = this.struct.getSubTables();
            for (TableStructure subStruct : subTables) {
                List<SqlQueryInserts> subInserts = inserts
                        .getSubInserts();
                for (SqlQueryInserts subInsert : subInserts) {
                    if (subInsert.getTable().getName()
                            .equalsIgnoreCase(subStruct.getTable())) {
                        processSubTable(scriptFolder, subInsert, subStruct,
                                pkField);
                        break;
                    }
                }
            }
        }
        Stack<SqlQueryInserts> stack = new Stack<SqlQueryInserts>();
        stack.push(inserts);
        while (this.mlTableInfo != null && !stack.isEmpty()) {
            SqlQueryInserts current = stack.pop();
            if (current == null)
                continue;
            ITable currentTable = current.getTable();
            List<String> mlColumns = this.mlTableInfo.get(currentTable
                    .getName().toLowerCase());
            if (mlColumns != null && mlColumns.size() > 0)
                if (currentTable.getPkConstraint() == null ||
                        currentTable.getPkConstraint().getColumns() == null ||
                        currentTable.getPkConstraint().getColumns().size() == 0) {
//                    logger.error(currentTable.getName() + "??????????????????????????????????????????");
                } else if (currentTable.getPkConstraint().getColumns().size() == 1) {
                    geneMLCSVFile(current, mlColumns);
                } else {
//                    logger.error(currentTable.getName() + "??????????????????????????????????????????");
                }
            if (current.getSubInserts() != null)
                stack.addAll(current.getSubInserts());
        }
        return true;
    }

    private void cleanFolder(File scriptFolder) {
        if (scriptFolder != null && scriptFolder.isDirectory()) {
            File[] listFiles = scriptFolder.listFiles();
            byte b;
            for (b = 0; b < listFiles.length; b++) {
                File file = listFiles[b];
                if (file.isFile())
                    try {
                        file.delete();
                    } catch (Exception exception) {
                    }
            }
        }
    }

    private String getFileNo(String table) {
        for (String key : this.tableFilenameMap.keySet()) {
            if (key.equalsIgnoreCase(table))
                return this.tableFilenameMap.get(key);
        }
        return null;
    }

    private void processSubTable(File scriptFolder, SqlQueryInserts inserts, TableStructure struct, String pValue) {
        String mapNo = getFileNo(struct.getTable());
        List<String> sqlResults = inserts.getResults();
        List<Map<String, Object>> resultSet2 = inserts.getResultSet()
                .getResults();
        boolean includeDeletes = (resultSet2.size() != sqlResults.size());
        boolean isNoPK = false;
        boolean isMultiPK = false;
        if (inserts.getTable().getPkConstraint() == null ||
                inserts.getTable().getPkConstraint().getColumns() == null ||
                inserts.getTable().getPkConstraint().getColumns().size() == 0) {
            isNoPK = true;
        } else if (inserts.getTable().getPkConstraint().getColumns().size() > 1) {
            isMultiPK = true;
        }
        if (isNoPK) {
//            logger.error(inserts.getTable().getName() + "?????????????????????????????????");
            return;
        }
        String foreignKey = struct.getForeignKey();
        List<IColumn> pkColumns = inserts.getTable().getPkConstraint()
                .getColumns();
        String[] pkKeys = new String[pkColumns.size()];
        List<String> resultList = new LinkedList<String>();
        for (int j = 0; j < resultSet2.size(); j++) {
            Map<String, Object> datas = resultSet2.get(j);
            String key = foreignKey;
            for (String column : datas.keySet()) {
                if (column.equalsIgnoreCase(key))
                    key = column;
                for (int i = 0; i < pkColumns.size(); i++) {
                    if (column.equalsIgnoreCase(pkColumns.get(i).getName()))
                        pkKeys[i] = column;
                }
            }
            boolean isRight = pValue != null && pValue.equals(datas.get(key));
            if (isRight) {
                if (includeDeletes) {
                    if (sqlResults.size() > j * 2 &&
                            sqlResults.get(j * 2) != null)
                        resultList.add(sqlResults.get(j * 2));
                    if (sqlResults.size() > j * 2 + 1 &&
                            sqlResults.get(j * 2 + 1) != null)
                        resultList.add(sqlResults.get(j * 2 + 1));
                } else if (sqlResults.size() > j && sqlResults.get(j) != null) {
                    resultList.add(sqlResults.get(j));
                }
                List<IColumn> blobColumns = getBlobColumns(inserts
                        .getTable());
                if (blobColumns.size() > 0)
                    if (isNoPK) {
//                        logger.error(inserts.getTable().getName() + "?????????????????????????????????BLOB???");
                    } else if (isMultiPK) {
//                        logger.error(inserts.getTable().getName() + "?????????????????????????????????BLOB???");
                    } else {
                        generateBlobFile(inserts.getTable(), datas, pkKeys[0], blobColumns, scriptFolder.getAbsolutePath(), mapNo);
                    }
                if (isNoPK) {
//                    logger.error(inserts.getTable().getName() + "??????????????????????????????????????????");
                } else if (isMultiPK) {
//                    logger.error(inserts.getTable().getName() + "??????????????????????????????????????????");
                } else {
                    List<TableStructure> subTables = struct
                            .getSubTables();
                    for (TableStructure subStruct : subTables) {
                        List<SqlQueryInserts> subInserts = inserts
                                .getSubInserts();
                        for (SqlQueryInserts subInsert : subInserts) {
                            if (subInsert.getTable().getName()
                                    .equalsIgnoreCase(subStruct.getTable())) {
                                String dataPKValue = (String) datas
                                        .get(pkKeys[0]);
                                processSubTable(scriptFolder, subInsert,
                                        subStruct, dataPKValue);
                                break;
                            }
                        }
                    }
                }
            }
        }
        PrintIOUtil.getInstance().resaveSql(resultList, scriptFolder, mapNo);
    }

    private boolean exportBusinessScript(SqlQueryInserts inserts, File folder) {
        Stack<SqlQueryInserts> stack = new Stack<SqlQueryInserts>();
        stack.push(inserts);
        while (!stack.isEmpty()) {
            SqlQueryInserts current = stack.pop();
            if (current == null)
                continue;
            ITable currentTable = current.getTable();
            String mapFileNo = this.tableFilenameMap.get(currentTable.getName());
            if (mapFileNo == null)
                mapFileNo = "t" + getAutoFileNo();
            boolean isSucess = PrintIOUtil.getInstance().resaveSql(
                    current.getResults(), folder, mapFileNo);
            if (!isSucess)
                return false;
            if (current.getSubInserts() != null)
                stack.addAll(current.getSubInserts());
            if (currentTable.getPkConstraint() != null &&
                    currentTable.getPkConstraint().getColumns().size() == 1) {
                List<IColumn> blobColumns = getBlobColumns(currentTable);
                if (blobColumns.size() > 0)
                    if (currentTable.getPkConstraint() == null ||
                            currentTable.getPkConstraint().getColumns() == null ||
                            currentTable.getPkConstraint().getColumns()
                                    .size() == 0) {
//                        logger.error(currentTable.getName() + "?????????????????????????????????BLOB???");
                    } else if (currentTable.getPkConstraint().getColumns()
                            .size() == 1) {
                        IColumn pkColumn = currentTable.getPkConstraint()
                                .getColumns().get(0);
                        SqlQueryResultSet rs = current.getResultSet();
                        String fileFolderPath = folder.getAbsolutePath();
                        geneBlobFile(rs, pkColumn.getName(), blobColumns,
                                fileFolderPath, mapFileNo);
                    } else {
//                        logger.error(currentTable.getName() + "?????????????????????????????????BLOB???");
                    }
            }
            if (this.mlTableInfo != null) {
                List<String> mlColumns = this.mlTableInfo.get(currentTable
                        .getName().toLowerCase());
                if (mlColumns != null && mlColumns.size() > 0) {
                    if (currentTable.getPkConstraint() == null ||
                            currentTable.getPkConstraint().getColumns() == null ||
                            currentTable.getPkConstraint().getColumns()
                                    .size() == 0) {
//                        logger.error(currentTable.getName() + "??????????????????????????????????????????");
                        continue;
                    }
                    if (currentTable.getPkConstraint().getColumns()
                            .size() == 1) {
                        geneMLCSVFile(current, mlColumns);
                        continue;
                    }
//                    logger.error(currentTable.getName() + "??????????????????????????????????????????");
                }
            }
        }
        return true;
    }

    private boolean geneMLCSVFile(SqlQueryInserts current, List<String> mlColumns) {
        if (this.langCode == null)
            this.langCode = "simpchn";
        ITable currentTable = current.getTable();
        File csvFolder = new File(this.root.getPath() +
                IOUtils.DIR_SEPARATOR + "dbml" + IOUtils.DIR_SEPARATOR +
                this.langCode + IOUtils.DIR_SEPARATOR + currentTable.getName());
        if ((!csvFolder.exists() && !csvFolder.mkdirs()) || (
                csvFolder.exists() && !csvFolder.isDirectory()))
            return false;
        PrintWriter csvWriter = null;
        try {
            csvWriter = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(csvFolder.getAbsolutePath() +
                            IOUtils.DIR_SEPARATOR + "001.csv"),
                    StandardCharsets.UTF_8));
            csvWriter.println(currentTable.getName());
            IColumn pkColumn = currentTable.getPkConstraint()
                    .getColumns().get(0);
            csvWriter.print(pkColumn.getName());
            csvWriter.print(",");
            csvWriter.println(StringUtils.join(
                    mlColumns.toArray(new String[mlColumns.size()]), ","));
            Iterator iterator = current.getResultSet().getResults().iterator();
            while (iterator.hasNext()) {
                Map<String, Object> result = (Map) iterator.next();
                String pk = result.get(pkColumn.getName()).toString().trim();
                csvWriter.print(pk);
                for (int i = 0; i < mlColumns.size(); i++) {
                    csvWriter.print(",");
                    String column = mlColumns.get(i);
                    Object columnValue = result.get(column);
                    if (columnValue != null) {
                        String value = columnValue.toString().trim();
                        if (isContainerQuote(value))
                            value = "\"" + value + "\"";
                        csvWriter.print(value);
                    }
                }
                csvWriter.println();
            }
        } catch (FileNotFoundException e) {
//            logger.error(e.getMessage(), e);
            return false;
        } finally {
            IOUtils.closeQuietly(csvWriter);
        }
        return true;
    }

    private void geneBlobFile(SqlQueryResultSet resultSet, String pkColumnName, List<IColumn> lstBlobColumn, String fileFolderPath, String tableMapNo) {
        for (Map<String, Object> result : resultSet.getResults())
            generateBlobFile(resultSet.getTable(), result, pkColumnName,
                    lstBlobColumn, fileFolderPath, tableMapNo);
    }

    private void generateBlobFile(ITable table, Map<String, Object> result, String pkColumnName, List<IColumn> lstBlobColumn, String fileFolderPath, String tableMapNo) {
        String tableName = table.getName();
        byte[] btTableName = null;
        btTableName = tableName.getBytes(StandardCharsets.UTF_8);
        if (btTableName.length > 40)
            throw new DatabaseRuntimeException("??????" + tableName + "????????????40?????????");
        byte[] outputTableName = new byte[40];
        System.arraycopy(btTableName, 0, outputTableName, 0, btTableName.length);
        byte[] btPKColumnName = null;
        btPKColumnName = pkColumnName.getBytes(StandardCharsets.UTF_8);
        if (btPKColumnName.length > 40)
            throw new DatabaseRuntimeException("??????" + tableName + "???????????????" +
                    pkColumnName + "????????????40?????????");
        byte[] outputPKColumnName = new byte[40];
        System.arraycopy(btPKColumnName, 0, outputPKColumnName, 0,
                btPKColumnName.length);
        byte btBlobColumnCount = (byte) lstBlobColumn.size();
        String[] aryBlobColumnName = new String[lstBlobColumn.size()];
        for (int i = 0; i < lstBlobColumn.size(); i++)
            aryBlobColumnName[i] = lstBlobColumn.get(i).getName();
        byte[][] outputBlobColumnNames = new byte[aryBlobColumnName.length][40];
        for (int i = 0; i < aryBlobColumnName.length; i++) {
            byte[] outputBlobColumnName = new byte[40];
            byte[] btBlobColumnName = null;
            btBlobColumnName = aryBlobColumnName[i].getBytes(StandardCharsets.UTF_8);
            System.arraycopy(btBlobColumnName, 0, outputBlobColumnName, 0,
                    btBlobColumnName.length);
            outputBlobColumnNames[i] = outputBlobColumnName;
        }
        for (Map.Entry<String, Object> mapEntry : result.entrySet()) {
            String columnName = mapEntry.getKey();
            String pkValue = null;
            if (columnName.equals(pkColumnName)) {
                pkValue = mapEntry.getValue().toString().trim();
                String blobFileName = String.valueOf(fileFolderPath) + IOUtils.DIR_SEPARATOR +
                        tableMapNo + "-" + pkValue + ".blob";
                DataOutputStream out = null;
                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(new File(blobFileName));
                    out = new DataOutputStream(fout);
                    out.write(outputTableName);
                    out.write(outputPKColumnName);
                    out.write(new byte[]{btBlobColumnCount});
                    for (int ii = 0; ii < outputBlobColumnNames.length; ii++)
                        out.write(outputBlobColumnNames[ii]);
                    byte[] outputPKValue = pkValue.getBytes(StandardCharsets.UTF_8);
                    byte pkValueLen = (byte) outputPKValue.length;
                    out.write(new byte[]{pkValueLen});
                    out.write(outputPKValue);
                    for (int jj = 0; jj < aryBlobColumnName.length; jj++) {
                        IColumn blobColumn = table
                                .getColumnByName(aryBlobColumnName[jj]);
                        Object obj = result.get(aryBlobColumnName[jj]);
                        if (obj == null) {
                            byte[] bytes = new byte[4];
                            out.write(bytes);
                        } else if (blobColumn.getTypeName().equalsIgnoreCase(
                                "image")) {
                            if (obj instanceof byte[]) {
                                byte[] blobByte = (byte[]) obj;
                                if (blobByte == null) {
                                    byte[] bytes = new byte[4];
                                    out.write(bytes);
                                } else {
                                    out.writeInt(blobByte.length);
                                    out.write(blobByte);
                                }
                            } else {
                                String errorInfo = "?????????[" + tableName + "]???Image??????[" + aryBlobColumnName[jj] + "]???????????????????????????Iamge??????";
//                                logger.error(errorInfo);
                                throw new DatabaseRuntimeException(
                                        errorInfo);
                            }
                        } else if (blobColumn.getTypeName()
                                .equalsIgnoreCase("blob")) {
                            if (obj instanceof Blob) {
                                Blob blobValue = (Blob) obj;
                                if (blobValue == null) {
                                    byte[] bytes = new byte[4];
                                    out.write(bytes);
                                } else {
                                    byte[] blobByte = null;
                                    try {
                                        blobByte = blobValue.getBytes(1L, (int) blobValue.length());
                                    } catch (SQLException e) {
                                        String errorInfo = "?????????[" + tableName + "]???Blob??????[" + aryBlobColumnName[jj] + "]????????????????????????" + e.getMessage();
//                                        logger.error(errorInfo, e);
                                        throw new DatabaseRuntimeException(errorInfo);
                                    }
                                    out.writeInt(blobByte.length);
                                    out.write(blobByte);
                                }
                            } else {
                                String errorInfo = "?????????[" + tableName + "]???Blob??????[" + aryBlobColumnName[jj] + "]???????????????????????????Blob??????";
//                                logger.error(errorInfo);
                                throw new DatabaseRuntimeException(
                                        errorInfo);
                            }
                        } else if (blobColumn.getTypeName()
                                .equalsIgnoreCase("blob(128m)")) {
                            String errorInfo = "?????????????????????DB2????????????[" + tableName + "]??????????????????[" + aryBlobColumnName[jj] + "]????????????";
//                            logger.error(errorInfo);
                            throw new DatabaseRuntimeException(errorInfo);
                        }
                        out.flush();
                    }
                } catch (IOException e) {
//                    logger.error("????????????" + blobFileName + "?????????", e);
                    throw new DatabaseRuntimeException("????????????" + blobFileName + "?????????");
                } finally {
                    IOUtils.closeQuietly(fout);
                    IOUtils.closeQuietly(out);
                }
            }
        }
    }

    private List<IColumn> getBlobColumns(ITable table) {
        List<IColumn> columns = table.getAllColumns();
        List<IColumn> blobs = new ArrayList<IColumn>();
        for (IColumn column : columns) {
            if (isBlobColumn(column))
                blobs.add(column);
        }
        return blobs;
    }

    private boolean isBlobColumn(IColumn col) {
        return col.getTypeName().equalsIgnoreCase("image") ||
                col.getTypeName().equalsIgnoreCase("blob") ||
                col.getTypeName().equalsIgnoreCase("blob(128m)");
    }

    private boolean isContainerQuote(String value) {
        boolean needQuote = false;
        if (value.indexOf('"') != -1) {
            value = value.replace("\"", "\"\"");
            needQuote = true;
        }
        if (value.indexOf(",") != -1)
            needQuote = true;
        return needQuote;
    }

    public Map<String, List<String>> getMlTableInfo() {
        return this.mlTableInfo;
    }
}
