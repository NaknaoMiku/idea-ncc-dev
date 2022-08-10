package com.summer.lijiahao.script.pub.db.script.init;

import com.summer.lijiahao.script.pub.db.query.IQueryInfo;

import java.util.List;

public interface IDbRecordScript extends IQueryInfo, IDbRecordExportInfo {
    List<IDbRecordScript> getChildren();
}
