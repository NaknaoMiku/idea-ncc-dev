package com.summer.lijiahao.utils.openapi.openapiutils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.MGF1ParameterSpec;


public class Encryption {
    private static final int MAX_ENCRYPT_BLOCK = 117;

    public static String symEncrypt(String strkey, String src) throws Exception {
        String target = null;
        try {
            Key key = KeysFactory.getSymKey(strkey);

            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            IvParameterSpec iv = new IvParameterSpec(strkey.substring(0, 16).getBytes());
            cipher.init(1, key, iv);
            byte[] encodeResult = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
            target = Base64Util.encryptBASE64(encodeResult);
        } catch (NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException | java.io.UnsupportedEncodingException |
                 java.security.InvalidKeyException | javax.crypto.IllegalBlockSizeException |
                 javax.crypto.BadPaddingException e) {
            throw new Exception("加密失败", e);
        }
        return target;
    }


    public static String pubEncrypt(String pubKey, String src) throws Exception {
        String target = null;
        ByteArrayOutputStream out = null;
        try {
            Key key = KeysFactory.getPublicKey(pubKey);

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(1, key,
                    new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"),
                            PSource.PSpecified.DEFAULT));

            byte[] data = src.getBytes();
            int inputLen = data.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;

            int i = 0;

            while (inputLen - offSet > 0) {
                byte[] cache;
                if (inputLen - offSet > 117) {
                    cache = cipher.doFinal(data, offSet, 117);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 117;
            }

            target = Base64Util.encryptBASE64(out.toByteArray());
        } catch (NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException | java.security.InvalidKeyException |
                 javax.crypto.IllegalBlockSizeException | javax.crypto.BadPaddingException e) {
            throw new Exception("加密失败", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return target;
    }
}