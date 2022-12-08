package com.summer.lijiahao.nccdevtool.base

class IpAndPort {
    var address: String? = null
    var port: Int? = null

    constructor()
    constructor(address: Any, port: Any) {
        this.address = address.toString()
        if (port is Int) {
            this.port = port
        } else {
            this.port = Integer.valueOf(port.toString())
        }
    }
}