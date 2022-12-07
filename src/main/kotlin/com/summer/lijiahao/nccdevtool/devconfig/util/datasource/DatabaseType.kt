package com.summer.lijiahao.nccdevtool.devconfig.util.datasource

class DatabaseType(databaseTypeName: String, driver: LinkedHashMap<String, DatabaseDriver>) {
    val databaseTypeName: String
    var driver: LinkedHashMap<String, DatabaseDriver>

    init {
        this.databaseTypeName = databaseTypeName
        this.driver = driver
    }
}