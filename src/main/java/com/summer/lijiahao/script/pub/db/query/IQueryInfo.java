package com.summer.lijiahao.script.pub.db.query;

import com.summer.lijiahao.script.pub.db.model.ITable;

import java.util.List;

public interface IQueryInfo {
    ITable getTable();

    String getWhereCondition();

    List<? extends IQueryInfo> getChildren();
}
