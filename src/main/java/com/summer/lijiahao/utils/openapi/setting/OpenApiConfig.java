package com.summer.lijiahao.utils.openapi.setting;


public class OpenApiConfig {
    public static final String SERVER_IP = "server_ip";
    public static final String SERVER_PORT = "server_port";
    public static final String BUSICODE = "busicode";
    public static final String APP_SECRET = "app_secret";
    public static final String USERCODE = "usercode";
    public static final String APPID = "appid";
    public static final String PUB_SECRET = "pub_secret";
    private String serverip;
    private String serverPort;
    private String busicode;
    private String appSecret;
    private String usercode;
    private String appid;
    private String pubSecret;

    public String getServerip() {
        return this.serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

    public String getServerPort() {
        return this.serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getBusicode() {
        return this.busicode;
    }

    public void setBusicode(String busicode) {
        this.busicode = busicode;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPubSecret() {
        return this.pubSecret;
    }

    public void setPubSecret(String pubSecret) {
        this.pubSecret = pubSecret;
    }
}
