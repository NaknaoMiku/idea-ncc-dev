package com.summer.lijiahao.script.pub.db;

import com.summer.lijiahao.script.pub.db.query.SqlQueryResultSet;
import com.summer.lijiahao.script.pub.db.script.export.IScriptExportStratege;
import com.summer.lijiahao.script.pub.db.script.export.SqlQueryInserts;

public interface IScriptService {
    SqlQueryInserts convert2InsertSQLs(SqlQueryResultSet paramSqlQueryResultSet);

    SqlQueryInserts convert2InsertSQLs(SqlQueryResultSet paramSqlQueryResultSet, boolean paramBoolean);

    boolean export(SqlQueryResultSet paramSqlQueryResultSet, IScriptExportStratege paramIScriptExportStratege);

    boolean export(SqlQueryResultSet paramSqlQueryResultSet, IScriptExportStratege paramIScriptExportStratege, boolean paramBoolean);

    void sync(String paramString, boolean paramBoolean, SqlQueryResultSet paramSqlQueryResultSet) throws Exception;
}
