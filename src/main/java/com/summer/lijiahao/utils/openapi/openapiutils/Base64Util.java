package com.summer.lijiahao.utils.openapi.openapiutils;

import org.apache.commons.codec.binary.Base64;


public class Base64Util {
    public static String encryptBASE64(byte[] key) {
        return (new Base64()).encodeToString(key);
    }


    public static byte[] decryptBASE64(String key) {
        return (new Base64()).decode(key);
    }
}
