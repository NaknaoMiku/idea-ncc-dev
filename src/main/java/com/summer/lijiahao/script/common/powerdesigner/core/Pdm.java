package com.summer.lijiahao.script.common.powerdesigner.core;

import com.summer.lijiahao.script.pub.db.model.IFkConstraint;
import com.summer.lijiahao.script.pub.db.model.IIndex;
import com.summer.lijiahao.script.pub.db.model.ITable;

import java.util.ArrayList;
import java.util.List;

public class Pdm {
    private String pdmName;

    private String pdmDesc;

    private String version;

    private final List<ITable> tables = new ArrayList();

    private final List<IFkConstraint> fkConstraints = new ArrayList();

    private final List<IIndex> indexs = new ArrayList();

    private final List<ViewInfo> views = new ArrayList();

    public List<ITable> getTables() {
        return this.tables;
    }

    public List<ViewInfo> getViews() {
        return this.views;
    }

    public List<IFkConstraint> getFkConstraints() {
        return this.fkConstraints;
    }

    public List<IIndex> getIndexs() {
        return this.indexs;
    }

    public String getPdmName() {
        return this.pdmName;
    }

    public void setPdmName(String pdmName) {
        this.pdmName = pdmName;
    }

    public String getPdmDesc() {
        return this.pdmDesc;
    }

    public void setPdmDesc(String pdmDesc) {
        this.pdmDesc = pdmDesc;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class ViewInfo {
        private String name;

        private String desc;

        private String sql;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getSql() {
            return this.sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }
    }
}
