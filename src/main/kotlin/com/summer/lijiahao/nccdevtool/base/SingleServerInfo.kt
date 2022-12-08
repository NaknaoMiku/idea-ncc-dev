package com.summer.lijiahao.nccdevtool.base

class SingleServerInfo {
    var javaHome = System.getProperty("java.home", "D:/j2sdk1.4.2_10")
    var name = "server"
    var jvmArgs = "-Xms768m -Xmx768m"
    var servicePort = 8005
    var ajp: Array<IpAndPort>? = null
    var http: Array<IpAndPort>? = null
    var https: Array<IpAndPort>? = null

    override fun toString(): String {
        return name
    }

    companion object {
        val nullServerInfo: SingleServerInfo
            get() {
                val nullinfo = SingleServerInfo()
                nullinfo.name = ""
                nullinfo.javaHome = ""
                nullinfo.jvmArgs = ""
                nullinfo.servicePort = 0
                return nullinfo
            }
        val defualtMasterServerInfo: SingleServerInfo
            get() {
                val nullinfo = SingleServerInfo()
                nullinfo.name = "Master"
                nullinfo.javaHome = ""
                nullinfo.jvmArgs = ""
                nullinfo.servicePort = 80
                return nullinfo
            }
    }
}