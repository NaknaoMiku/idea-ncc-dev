package com.summer.lijiahao.script.pub.db.script.init.impl;

import com.summer.lijiahao.script.pub.db.script.ISqlFile;
import com.summer.lijiahao.script.pub.db.script.ScriptType;

import java.io.File;

public class DbRecordSqlFile implements ISqlFile {
    private final File file;

    public DbRecordSqlFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public ScriptType getScriptType() {
        return ScriptType.RECORD;
    }
}
