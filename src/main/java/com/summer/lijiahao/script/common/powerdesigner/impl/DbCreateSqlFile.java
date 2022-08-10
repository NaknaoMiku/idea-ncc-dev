package com.summer.lijiahao.script.common.powerdesigner.impl;

import com.summer.lijiahao.script.common.powerdesigner.itf.ISqlFile;
import com.summer.lijiahao.script.common.powerdesigner.core.ScriptType;

import java.io.File;

public class DbCreateSqlFile implements ISqlFile {
    private File file;

    public DbCreateSqlFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public ScriptType getScriptType() {
        return ScriptType.CREATE;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
