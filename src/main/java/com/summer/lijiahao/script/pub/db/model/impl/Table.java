package com.summer.lijiahao.script.pub.db.model.impl;

import com.summer.lijiahao.script.pub.db.model.IColumn;
import com.summer.lijiahao.script.pub.db.model.IFkConstraint;
import com.summer.lijiahao.script.pub.db.model.IPkConstraint;
import com.summer.lijiahao.script.pub.db.model.ITable;
import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.List;

public class Table implements ITable {
    private final List<IColumn> allColumns = new ArrayList<>();
    private final List<IFkConstraint> fkConstraints = new ArrayList<>();
    private String name;
    private IPkConstraint pkConstraint;

    private String desc;

    public List<IColumn> getAllColumns() {
        return this.allColumns;
    }

    public List<IFkConstraint> getFkConstraints() {
        return this.fkConstraints;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null) {
            this.name = null;
        } else {
            this.name = name.toLowerCase();
        }
    }

    public IPkConstraint getPkConstraint() {
        return this.pkConstraint;
    }

    public void setPkConstraint(IPkConstraint pkConstraint) {
        this.pkConstraint = pkConstraint;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public IColumn getColumnByName(String colName) {
        for (IColumn col : this.allColumns) {
            if (col.getName().equalsIgnoreCase(colName))
                return col;
        }
        return null;
    }

    public IFkConstraint getFkConstraintByRefTableName(String refTableName) {
        for (IFkConstraint fkConstraint : this.fkConstraints) {
            if (fkConstraint.getRefTable().getName().equalsIgnoreCase(refTableName))
                return fkConstraint;
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append("(");
        for (IColumn col : this.allColumns)
            sb.append(col.getName()).append(" ").append(col.getTypeName()).append(", ");
        sb.append(System.lineSeparator()).append("primary key: (");
        for (IColumn pkCol : this.pkConstraint.getColumns())
            sb.append(pkCol.getName()).append(",");
        sb.deleteCharAt(sb.length() - 1).append(")");
        if (!this.fkConstraints.isEmpty())
            for (IFkConstraint fkCol : this.fkConstraints) {
                sb.append(System.lineSeparator()).append("foreign key: (");
                for (IColumn col : fkCol.getColumns())
                    sb.append(col.getName()).append(",");
                sb.deleteCharAt(sb.length() - 1).append(")");
            }
        sb.append(")");
        return sb.toString();
    }
}
