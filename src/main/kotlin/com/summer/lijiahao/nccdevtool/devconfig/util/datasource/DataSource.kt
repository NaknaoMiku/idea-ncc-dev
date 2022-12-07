package com.summer.lijiahao.nccdevtool.devconfig.util.datasource

class DataSource(
    dataSourceName: String = "",
    oidMark: String = "OA",
    databaseUrl: String = "jdbc:oracle:thin:@127.0.0.1:1521/orcl",
    user: String = "sa",
    password: String = "sa",
    driverClassName: String = "oracle.jdbc.OracleDriver",
    databaseType: String = "ORACLE11G",
    isBase: String = "false",

    maxCon: String = "50",
    minCon: String = "1",
    dataSourceClassName: String = "nc.bs.mw.ejb.xares.IerpDataSource",
    xaDataSourceClassName: String = "nc.bs.mw.ejb.xares.IerpXADataSource",
    conIncrement: String = "0",
    conInUse: String = "0",
    conIdle: String = "0"
) {

    var dataSourceName: String
    var oidMark: String
    var databaseUrl: String
    var user: String
    var password: String
    var driverClassName: String
    var databaseType: String
    var isBase: String
    var maxCon: String
    var minCon: String
    var dataSourceClassName: String
    var xaDataSourceClassName: String
    var conIncrement: String
    var conInUse: String
    var conIdle: String

    var url: String
    var port: String
    var dbName: String
    val jdbcType: String

    init {
        this.dataSourceName = dataSourceName
        this.oidMark = oidMark
        this.databaseUrl = databaseUrl
        this.user = user
        this.password = password
        this.driverClassName = driverClassName
        this.databaseType = databaseType
        this.isBase = isBase
        this.maxCon = maxCon
        this.minCon = minCon
        this.dataSourceClassName = dataSourceClassName
        this.xaDataSourceClassName = xaDataSourceClassName
        this.conIncrement = conIncrement
        this.conInUse = conInUse
        this.conIdle = conIdle

        this.dbName = databaseUrl.split("/")[1]
        val urlAndPort = databaseUrl.split("@")[1].split("/")[0].split(":")
        this.url = urlAndPort[0]
        this.port = urlAndPort[1]
        this.jdbcType = databaseUrl.split(":")[0]
    }
}