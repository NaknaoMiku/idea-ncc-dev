package com.summer.lijiahao.nccdevtool.openapi.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.MGF1ParameterSpec;


public class Decryption {
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static String symDecrypt(String strkey, String src) throws Exception {
        String target = null;

        try {
            Key key = KeysFactory.getSymKey(strkey);

            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            IvParameterSpec iv = new IvParameterSpec(strkey.substring(0, 16).getBytes());
            cipher.init(2, key, iv);
            byte[] decodeResult = cipher.doFinal(Base64Util.decryptBASE64(src));
            target = new String(decodeResult);

        } catch (NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException |
                 javax.crypto.IllegalBlockSizeException | javax.crypto.BadPaddingException |
                 java.security.InvalidKeyException e) {
            e.printStackTrace();
            throw new Exception("解密失败" + e.getMessage());
        }

        return target;
    }


    public static String priDecrypt(String priKey, String src) throws Exception {
        String target = null;
        ByteArrayOutputStream out = null;
        try {
            Key key = KeysFactory.getPrivateKey(priKey);


            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(2, key,
                    new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"),
                            PSource.PSpecified.DEFAULT));


            byte[] data = Base64Util.decryptBASE64(src);
            int inputLen = data.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;

            int i = 0;

            while (inputLen - offSet > 0) {
                byte[] cache;
                if (inputLen - offSet > 128) {
                    cache = cipher.doFinal(data, offSet, 128);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 128;
            }
            target = out.toString();

        } catch (NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException | java.security.InvalidKeyException |
                 javax.crypto.IllegalBlockSizeException | javax.crypto.BadPaddingException e) {
            throw new Exception("解密失败", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return target;
    }
}