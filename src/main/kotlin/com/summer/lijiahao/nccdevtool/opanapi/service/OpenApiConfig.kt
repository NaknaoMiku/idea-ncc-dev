package com.summer.lijiahao.nccdevtool.opanapi.service

class OpenApiConfig {
    var serverip: String? = null
    var serverPort: String? = null
    var busicode: String? = null
    var appSecret: String? = null
    var usercode: String? = null
    var appid: String? = null
    var pubSecret: String? = null

    companion object {
        const val SERVER_IP = "server_ip"
        const val SERVER_PORT = "server_port"
        const val BUSICODE = "busicode"
        const val APP_SECRET = "app_secret"
        const val USERCODE = "usercode"
        const val APPID = "appid"
        const val PUB_SECRET = "pub_secret"
    }
}