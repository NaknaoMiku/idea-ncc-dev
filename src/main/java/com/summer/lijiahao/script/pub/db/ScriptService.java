package com.summer.lijiahao.script.pub.db;

import com.summer.lijiahao.script.pub.db.model.IColumn;
import com.summer.lijiahao.script.pub.db.model.ITable;
import com.summer.lijiahao.script.pub.db.query.SqlQueryResultSet;
import com.summer.lijiahao.script.pub.db.script.export.IScriptExportStratege;
import com.summer.lijiahao.script.pub.db.script.export.SqlQueryInserts;
import com.summer.lijiahao.script.studio.connection.ConnectionService;
import com.summer.lijiahao.script.studio.connection.exception.ConnectionException;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ScriptService implements IScriptService {
    //    protected static Logger logger = LoggerFactory.getLogger(ScriptService.class.getName());
    public static final String SQL_SERPERATOR = System.lineSeparator() + "go" +
            System.lineSeparator();

    private IScriptExportStratege stratege;

    public SqlQueryInserts convert2InsertSQLs(SqlQueryResultSet rs, boolean includeDeletes) {
        if (rs == null)
            return null;
        SqlQueryInserts inserts = getInserts(rs, SQL_SERPERATOR,
                includeDeletes);
        if (rs.getSubResultSets() != null)
            for (SqlQueryResultSet resultSet : rs.getSubResultSets())
                fillSqls(inserts, resultSet, SQL_SERPERATOR, includeDeletes);
        return inserts;
    }

    private void fillSqls(SqlQueryInserts parentInserts, SqlQueryResultSet rs, String separator, boolean includeDeletes) {
        SqlQueryInserts inserts = getInserts(rs, separator,
                includeDeletes);
        parentInserts.getSubInserts().add(inserts);
        if (rs.getSubResultSets() != null)
            for (SqlQueryResultSet resultSet : rs.getSubResultSets())
                fillSqls(inserts, resultSet, separator, includeDeletes);
    }

    private SqlQueryInserts getInserts(SqlQueryResultSet resultSet, String separator, boolean includeDeletes) {
        ITable table = resultSet.getTable();
        SqlQueryInserts inserts = new SqlQueryInserts(table);
        inserts.setResults(new ArrayList());
        for (Map<String, Object> result : resultSet.getResults()) {
            if (includeDeletes) {
                String delete = ScriptHelper.convert2DeleteSql(table, result,
                        separator);
                inserts.getResults().add(delete);
            }
            String sql = ScriptHelper.convert2InsertSqls(table, result,
                    separator, this.stratege);
            inserts.getResults().add(sql);
        }
        inserts.setResultSet(resultSet);
        return inserts;
    }

    public void sync(String dataSource, boolean deleteFirst, SqlQueryResultSet rs) throws Exception {
        SqlQueryInserts inserts = convert2InsertSQLs(rs, deleteFirst);
        Stack<SqlQueryInserts> stack = new Stack<SqlQueryInserts>();
        stack.add(inserts);
        while (!stack.empty()) {
            SqlQueryInserts cInserts = stack.pop();
            if (cInserts == null)
                continue;
            if (cInserts.getSubInserts() != null)
                stack.addAll(cInserts.getSubInserts());
            List<String> sqls = cInserts.getResults();
            for (int i = 0; i < sqls.size(); i++) {
                String sql = sqls.get(i);
                int lastIndexOf = sql.lastIndexOf(SQL_SERPERATOR);
                if (lastIndexOf > 0)
                    sqls.set(i, sql.substring(0, lastIndexOf));
            }
            try {
                ConnectionService.executeBatch(dataSource,
                        sqls.toArray(new String[sqls.size()]));
                syncBlob(dataSource, cInserts);
            } catch (Exception i) {
                throw new Exception(i.getMessage());
            }
        }
    }

    private void syncBlob(String dataSource, SqlQueryInserts inserts) throws ConnectionException {
        SqlQueryResultSet rs = inserts.getResultSet();
        ITable table = rs.getTable();
        List<IColumn> blobColumns = getBlobColumns(table);
        if (blobColumns.size() > 0) {
            List<Map<String, Object>> results = inserts.getResultSet()
                    .getResults();
            if (table.getPkConstraint() == null ||
                    table.getPkConstraint().getColumns().size() == 0)
                throw new RuntimeException("???" + table.getName() +
                        "???????????????????????????blob?????????");
            List<IColumn> pkColumns = table.getPkConstraint().getColumns();
            StringBuilder buffer = new StringBuilder();
            buffer.append("update ").append(table.getName())
                    .append(" set ${column}= ? where ");
            for (int i = 0; i < pkColumns.size(); i++) {
                buffer.append(pkColumns.get(i).getName()).append("=?");
                if (i != pkColumns.size() - 1)
                    buffer.append(" and ");
            }
            String template = buffer.toString();
            for (int i = 0; i < blobColumns.size(); i++) {
                String blobName = blobColumns.get(i).getName();
                String sql = template.replace("${column}", blobName);
                for (Map<String, Object> result : results) {
                    Object obj = result.get(blobColumns.get(i).getName());
                    if (obj == null)
                        continue;
                    Connection conn =
                            ConnectionService.getConnection(dataSource);
                    PreparedStatement statement = null;
                    try {
                        statement = conn.prepareStatement(sql);
                        if (obj instanceof Blob) {
                            Blob blob = (Blob) obj;
                            statement
                                    .setBinaryStream(1, blob.getBinaryStream());
                        } else if (obj instanceof byte[]) {
                            byte[] blobBytes = (byte[]) obj;
                            InputStream iis = new ByteArrayInputStream(
                                    blobBytes);
                            statement.setBinaryStream(1, iis, blobBytes.length);
                        } else {
//                            logger.error("????????????????????????");
                            ConnectionService.closeQuietly(conn, statement, null);
                            continue;
                        }
                        for (int j = 0; j < pkColumns.size(); j++) {
                            String pkName = pkColumns.get(i).getName();
                            statement.setObject(j + 2, result.get(pkName));
                        }
                        statement.execute();
                    } catch (SQLException e) {
//                        logger.error(e.getMessage(), e);
                        ConnectionService.closeQuietly(conn, statement, null);
                    } finally {
                        ConnectionService.closeQuietly(conn, statement, null);
                    }
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

    public boolean export(SqlQueryResultSet rs, IScriptExportStratege stratege, boolean includeDeletes) {
        this.stratege = stratege;
        SqlQueryInserts inserts = convert2InsertSQLs(rs,
                includeDeletes);
        return stratege.export(inserts);
    }

    public SqlQueryInserts convert2InsertSQLs(SqlQueryResultSet rs) {
        return convert2InsertSQLs(rs, false);
    }

    public boolean export(SqlQueryResultSet rs, IScriptExportStratege stratege) {
        return export(rs, stratege, false);
    }
}
