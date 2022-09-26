package com.summer.lijiahao.script.pub.db.model.impl;

import com.summer.lijiahao.script.pub.db.model.IColumn;
import com.summer.lijiahao.script.pub.db.model.ITable;
import com.summer.lijiahao.script.pub.db.model.ITableBase;

public class Column implements IColumn {
    private String name;

    private ITableBase table;

    private int dataType;

    private String typeName;

    private int length;

    private int precise;

    private boolean nullable = true;

    private String defaultValue;

    private String desc;

    private String stereoType;

    public int getDataType() {
        return this.dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return (this.name == null) ? null : this.name.toLowerCase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public ITableBase getTableBase() {
        return this.table;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPrecise() {
        return this.precise;
    }

    public void setPrecise(int precise) {
        this.precise = precise;
    }

    public boolean isNullable() {
        return this.nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setTable(ITable table) {
        this.table = table;
    }

    public String toString() {
        return this.name + ":" + this.typeName;
    }

    public String getStereotype() {
        return this.stereoType;
    }

    public void setStereotype(String stereoType) {
        this.stereoType = stereoType;
    }
}
