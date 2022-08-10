package com.summer.lijiahao.script.pub.db.script.init;

import com.summer.lijiahao.script.pub.db.script.ISqlFile;

import java.sql.Connection;
import java.util.Map;

public interface IDBRecordService {
    ISqlFile[] geneSqlFile(IDbRecordScript paramIDbRecordScript, Connection paramConnection, DbRecordSqlFileCfg paramDbRecordSqlFileCfg, Map<String, MLTableMetaInfo> paramMap);
}
