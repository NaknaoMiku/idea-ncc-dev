package com.summer.lijiahao.utils.openapi.openapiutils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class KeysFactory {
    public static KeyPairs buildAsymKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        keyPairGenerator.initialize(1024, new SecureRandom());


        return new KeyPairs(keyPairGenerator.generateKeyPair());
    }


    public static String buildSymKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES/CTR/NoPadding");

        keyGenerator.init(256, new SecureRandom());

        SecretKey secretKey = keyGenerator.generateKey();

        return Base64Util.encryptBASE64(secretKey.getEncoded());
    }


    public static Key getPublicKey(String pubKey) throws Exception {
        Key key = null;

        try {
            byte[] keyBytes = Base64Util.decryptBASE64(pubKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            key = keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            throw new Exception("无效的密钥  ", e);
        }

        return key;
    }

    public static Key getPrivateKey(String priKey) throws Exception {
        Key key = null;

        try {
            byte[] keyBytes = Base64Util.decryptBASE64(priKey);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            key = keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            throw new Exception("无效密钥 ", e);
        }

        return key;
    }

    public static Key getSymKey(String symKey) throws Exception {
        Key key = null;

        try {
            byte[] keyBytes = Base64Util.decryptBASE64(symKey);

            key = new SecretKeySpec(keyBytes, "AES/CTR/NoPadding");
        } catch (Exception e) {
            throw new Exception("无效密钥 ", e);
        }

        return key;
    }
}