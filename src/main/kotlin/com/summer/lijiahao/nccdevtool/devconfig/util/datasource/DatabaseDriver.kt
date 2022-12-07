package com.summer.lijiahao.nccdevtool.devconfig.util.datasource

/**
 * <database>
 * 		<databaseType>SQLSERVER2008</databaseType>
 * 		<driver>
 * 			<driverType>JDBC</driverType>
 * 			<driverLib>sqlserver_2008/sqljdbc4.jar</driverLib>
 * 			<driverClass>com.microsoft.sqlserver.jdbc.SQLServerDriver</driverClass>
 * 			<driverUrl>jdbc:sqlserver://127.0.0.1:1433;database=nc60;sendStringParametersAsUnicode=true;responseBuffering=adaptive</driverUrl>
 * 			<maxCon>50</maxCon>
 * 			<minCon>10</minCon>
 * 			<providerClass>nc.bs.framework.was.admin.jdbc.SQLServerProvider</providerClass>
 * 		</driver>
 * 	</database>
 */

class DatabaseDriver(
    driverType: String,
    driverLib: String,
    driverClass: String,
    driverUrl: String,
    maxCon: String,
    minCon: String,
    providerClass: String
) {
    var driverType: String
    var driverLib: String
    var driverClass: String
    var driverUrl: String
    var maxCon: String
    var minCon: String
    var providerClass: String

    init {
        this.driverType = driverType
        this.driverLib = driverLib
        this.driverClass = driverClass
        this.driverUrl = driverUrl
        this.maxCon = maxCon
        this.minCon = minCon
        this.providerClass = providerClass
    }
}