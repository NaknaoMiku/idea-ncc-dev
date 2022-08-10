package com.summer.lijiahao.script.studio.connection.provider;

import com.summer.lijiahao.script.studio.connection.model.DataSourceMetaInfo;
import com.summer.lijiahao.script.studio.ui.preference.prop.DataSourceMeta;

import java.net.URL;
import java.util.Map;

public interface IDataSourceProvider {
    URL[] getDriverLibraries();

    DataSourceMetaInfo[] getDataSourceMetas();

    String getDefaultDataSourceName();

    String getSecondaryDataSourceName();

    String[] getDataSourceNames();

    void reset();

    Map<String, DataSourceMeta> getDataSourceMetaMap();
}
