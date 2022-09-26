package com.summer.lijiahao.script.pub.db.query;

import com.summer.lijiahao.script.pub.db.model.ITable;

import java.util.List;

public class QueryInfo implements IQueryInfo {
    public ITable table;

    public String whereCondition;

    public List<IQueryInfo> children;

    public List<IQueryInfo> getChildren() {
        return this.children;
    }

    public void setChildren(List<IQueryInfo> childs) {
        this.children = childs;
    }

    public ITable getTable() {
        return this.table;
    }

    public void setTable(ITable table) {
        this.table = table;
    }

    public String getWhereCondition() {
        return this.whereCondition;
    }

    public void setWhereCondition(String whereCondition) {
        this.whereCondition = whereCondition;
    }
}
