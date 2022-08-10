package com.summer.lijiahao.script.pub.db;

import com.summer.lijiahao.script.pub.db.model.TableStructure;
import com.summer.lijiahao.script.pub.db.query.IQueryInfo;
import com.summer.lijiahao.script.pub.db.query.SqlQueryResultSet;

import java.sql.Connection;

public interface IQueryService {
    SqlQueryResultSet query(String paramString1, String paramString2, TableStructure paramTableStructure, Connection paramConnection) throws Exception;

    SqlQueryResultSet query(IQueryInfo paramIQueryInfo, Connection paramConnection);
}
