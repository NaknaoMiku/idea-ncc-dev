package com.summer.lijiahao.script.pub.db.script.export;

public interface IScriptExportStratege {
    String DEFAULT_ENCODING = "UTF-8";

    boolean export(SqlQueryInserts paramSqlQueryInserts);
}
