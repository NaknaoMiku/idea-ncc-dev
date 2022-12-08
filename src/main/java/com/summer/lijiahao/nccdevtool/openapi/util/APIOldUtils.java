package com.summer.lijiahao.nccdevtool.openapi.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class APIOldUtils
        implements IAPIUtils {
    private final String secret_level = "L0";
    private String grant_type = "client";
    private String apiUrl;
    private String pubKey;


    private String baseUrl;


    private String biz_center;

    private String client_id;

    private String client_secret;

    private String user_name;

    private String pwd;

    public static void main(String[] args) {
        APIOldUtils util = new APIOldUtils();
        try {
            String token = util.getToken();
            System.out.println(token);
            util.setApiUrl("nccloud/api/riaorg/orgmanage/org/queryOrgByCode");
            String json = "{\"code\": [\"01\", \"T2001\"]}";
            util.getAPIRetrun(token, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getApiUrl() {
        return this.apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getTokenByClient() throws Exception {
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("grant_type", "client_credentials");

        paramMap.put("client_id", this.client_id);

        paramMap.put("client_secret",
                URLEncoder.encode(Encryption.pubEncrypt(this.pubKey, this.client_secret), StandardCharsets.UTF_8));

        paramMap.put("biz_center", this.biz_center);


        String sign = SHA256Util.getSHA256(this.client_id + this.client_secret + this.pubKey);
        paramMap.put("signature", sign);

        String url = this.baseUrl + "nccloud/opm/accesstoken";
        String mediaType = "application/x-www-form-urlencoded";
        String token = doPost(url, paramMap, mediaType, null, "");
        return token;
    }

    private String getTokenByRefreshToken(String refresh_token) throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("grant_type", "refresh_token");

        paramMap.put("client_id", this.client_id);

        paramMap.put("client_secret",
                URLEncoder.encode(Encryption.pubEncrypt(this.pubKey, this.client_secret), StandardCharsets.UTF_8));

        String sign = SHA256Util.getSHA256(this.client_id + this.client_secret + refresh_token + this.pubKey);
        paramMap.put("signature", sign);
        paramMap.put("refresh_token", refresh_token);
        paramMap.put("biz_center", this.biz_center);

        String url = this.baseUrl + "nccloud/opm/accesstoken";
        String mediaType = "application/x-www-form-urlencoded";
        String token = doPost(url, paramMap, mediaType, null, "");
        return token;
    }

    public String getToken() throws Exception {
        String token = null;
        if ("password".equals(this.grant_type)) {

            token = getTokenByPWD();
        } else if ("client".equals(this.grant_type)) {

            token = getTokenByClient();
        } else {
            throw new Exception("token获取模式错误");
        }
        return token;
    }

    private String getTokenByPWD() throws Exception {
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("grant_type", "password");

        paramMap.put("client_id", this.client_id);

        paramMap.put("client_secret",
                URLEncoder.encode(Encryption.pubEncrypt(this.pubKey, this.client_secret), StandardCharsets.UTF_8));

        paramMap.put("username", this.user_name);

        paramMap.put("password",
                URLEncoder.encode(Encryption.pubEncrypt(this.pubKey, this.pwd), StandardCharsets.UTF_8));

        paramMap.put("biz_center", this.biz_center);

        String sign =
                SHA256Util.getSHA256(this.client_id + this.client_secret + this.user_name + this.pwd + this.pubKey);
        paramMap.put("signature", sign);
        String url = this.baseUrl + "nccloud/opm/accesstoken";
        String mediaType = "application/x-www-form-urlencoded";
        String token = doPost(url, paramMap, mediaType, null, "");
        return token;
    }

    private String dealResponseBody(String source, String security_key, String level) throws Exception {
        String result = null;
        if (level == null || "".equals(level.trim()) || "L0".equals(level)) {
            result = source;
        } else if ("L1".equals(level)) {
            result = Decryption.symDecrypt(security_key, source);
        } else if ("L2".equals(level)) {
            result = CompressUtil.gzipDecompress(source);
        } else if ("L3".equals(level)) {
            result = CompressUtil.gzipDecompress(Decryption.symDecrypt(security_key, source));
        } else if ("L4".equals(level)) {
            result = Decryption.symDecrypt(security_key, CompressUtil.gzipDecompress(source));
        } else {
            throw new Exception("无效的安全等级");
        }

        return result;
    }

    private String dealRequestBody(String source, String security_key, String level) throws Exception {
        String result = null;
        if (level == null || "".equals(level.trim()) || "L0".equals(level)) {
            result = source;
        } else if ("L1".equals(level)) {
            result = Encryption.symEncrypt(security_key, source);
        } else if ("L2".equals(level)) {
            result = CompressUtil.gzipCompress(source);
        } else if ("L3".equals(level)) {
            result = Encryption.symEncrypt(security_key, CompressUtil.gzipCompress(source));
        } else if ("L4".equals(level)) {
            result = CompressUtil.gzipCompress(Encryption.symEncrypt(security_key, source));
        } else {
            throw new Exception("无效的安全等级");
        }

        return result;
    }

    private String doPost(String baseUrl, Map<String, String> paramMap, String mediaType, Map<String, String> headers, String json) throws Exception {
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        OutputStream out = null;
        BufferedReader bufferedReader = null;
        String result = null;
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(baseUrl);
            if (paramMap != null) {
                sb.append("?");
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    sb.append(key + "=" + value).append("&");
                }
                baseUrl = sb.toString().substring(0, sb.toString().length() - 1);
            }

            URL urlObj = new URL(baseUrl);
            urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.setConnectTimeout(50000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.addRequestProperty("content-type", mediaType);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    urlConnection.addRequestProperty(key, headers.get(key));
                }
            }
            out = urlConnection.getOutputStream();
            out.write(json.getBytes(StandardCharsets.UTF_8));
            out.flush();

            int resCode = urlConnection.getResponseCode();
            if (resCode == 200 || resCode == 201 ||
                    resCode == 202) {
                in = urlConnection.getInputStream();
            } else {
                in = urlConnection.getErrorStream();
            }
            bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            StringBuffer temp = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            String ecod = urlConnection.getContentEncoding();
            if (ecod == null) {
                ecod = StandardCharsets.UTF_8.name();
            }
            result = new String(temp.toString().getBytes(StandardCharsets.UTF_8), ecod);
        } catch (Exception e) {
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw e;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw e;
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw e;
                }
            }
            urlConnection.disconnect();
        }
        return result;
    }

    public String getGrant_type() {
        return this.grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getAPIRetrun(String token, String json) throws Exception {
        String access_token, security_key;
        APIReturnEntity tokenJson = JSON.parseObject(token, APIReturnEntity.class);


        if (tokenJson != null && tokenJson.getData() != null && tokenJson.getData() instanceof JSONObject) {
            access_token = ((JSONObject) tokenJson.getData()).getString("access_token");
            security_key = ((JSONObject) tokenJson.getData()).getString("security_key");
        } else {

            throw new Exception("获取token失败:" + token);
        }

        String url = this.baseUrl + this.apiUrl;

        Map<String, String> headermap = new HashMap<>();
        headermap.put("access_token", access_token);
        headermap.put("client_id", this.client_id);

        StringBuffer sb = new StringBuffer();
        sb.append(this.client_id);
        if (json != null) {
            sb.append(json);
        }
        sb.append(this.pubKey);
        String sign = SHA256Util.getSHA256(sb.toString());
        headermap.put("signature", sign);


        headermap.put("repeat_check", "Y");
        headermap.put("ucg_flag", "y");

        String mediaType = "application/json;charset=utf-8";


        String requestBody = dealRequestBody(json, security_key, this.secret_level);


        String result = doPost(url, null, mediaType, headermap, requestBody);
        String responseBody = dealResponseBody(result, security_key, this.secret_level);
        return responseBody;
    }


    public void init(String ip, String port, String biz_center, String client_id, String client_secret, String pubKey, String user_name, String pwd) {
        this.baseUrl = "http://" + ip + ":" + port + "/";
        this.biz_center = biz_center;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.pubKey = pubKey;
        this.user_name = user_name;
        this.pwd = pwd;
    }

    class SecretConst {
        public static final String LEVEL0 = "L0";


        public static final String LEVEL1 = "L1";


        public static final String LEVEL2 = "L2";


        public static final String LEVEL3 = "L3";


        public static final String LEVEL4 = "L4";
    }
}
