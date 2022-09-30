package com.summer.lijiahao.utils.openapi.openapiutils;

public interface IAPIUtils {

    
    String getToken() throws Exception;

    void init(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8);

    void setApiUrl(String paramString);

    String getAPIRetrun(String paramString1, String paramString2) throws Exception;
}
