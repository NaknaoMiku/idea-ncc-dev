package com.summer.lijiahao.script.pub.db.model;

import java.util.List;

public interface IConstraint {
    String getName();

    ITable getTable();

    List<IColumn> getColumns();
}
