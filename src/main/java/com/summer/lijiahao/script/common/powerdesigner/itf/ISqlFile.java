package com.summer.lijiahao.script.common.powerdesigner.itf;

import com.summer.lijiahao.script.common.powerdesigner.core.ScriptType;

import java.io.File;

public interface ISqlFile {
    File getFile();

    ScriptType getScriptType();
}
