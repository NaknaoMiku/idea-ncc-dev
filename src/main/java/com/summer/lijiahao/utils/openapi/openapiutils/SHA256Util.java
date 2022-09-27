package com.summer.lijiahao.utils.openapi.openapiutils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class SHA256Util {
    public static String getSHA256(String str, String key) throws Exception {
        byte[] salt = new byte[16];
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(key.getBytes());
        random.nextBytes(salt);
        String salt_value = Base64Util.encryptBASE64(salt);
        return getSHA256(str + salt_value.replaceAll("\r|\n", ""));
    }


    public static String getSHA256(String str) throws Exception {
        String encodestr = "";
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        encodestr = byte2Hex(messageDigest.digest());
        return encodestr;
    }

    public static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        String temp = null;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
