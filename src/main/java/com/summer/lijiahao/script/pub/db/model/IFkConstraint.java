package com.summer.lijiahao.script.pub.db.model;

import java.util.List;

public interface IFkConstraint extends IConstraint {
    ITable getRefTable();

    List<IColumn> getRefColumns();
}
