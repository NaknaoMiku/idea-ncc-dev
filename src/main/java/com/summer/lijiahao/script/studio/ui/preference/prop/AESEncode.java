package com.summer.lijiahao.script.studio.ui.preference.prop;

import nc.uap.plugin.studio.ui.preference.rsa.Encode;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @description: TODO
 * @author: liuchao
 * @date: 2022/4/1
 */
public class AESEncode {
    static final IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes("1234567890123456"));
    static final String transform = "AES/CBC/PKCS5Padding";
    private static final String KEY = System.getProperty("project.aes.seed", "yonyou.default");
    private static final String KEY_AES = "AES";
    private static final String SECURE_RABDOM = "SHA1PRNG";
    private static final String FLAY = "#";

    public AESEncode() {
    }

    public static String encrypt(String data) {
        return "#" + aesEncode(data);
    }

    public static String decrypt(String data) {
        if (data.charAt(0) == '#') {
            data = data.substring(1);
            return aesDecode(data);
        } else {
            return (new Encode()).decode(data);
        }
    }

    private static String aesEncode(String text) {
        String encodedString = null;

        try {
            ByteBuffer inBuffer = ByteBuffer.allocateDirect(1024);
            ByteBuffer outBuffer = ByteBuffer.allocateDirect(1024);
            inBuffer.put(getUTF8Bytes(text));
            inBuffer.flip();
            SecretKeySpec secretKeySpec = generateKey();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, secretKeySpec, iv);
            int updateBytes = cipher.update(inBuffer, outBuffer);
            int finalBytes = cipher.doFinal(inBuffer, outBuffer);
            outBuffer.flip();
            byte[] encoded = new byte[updateBytes + finalBytes];
            outBuffer.duplicate().get(encoded);
            encodedString = parseByte2HexStr(encoded);
        } catch (Throwable var9) {
            var9.printStackTrace();
        }

        return encodedString;
    }

    private static String aesDecode(String encodedString) {
        ByteBuffer decoded = ByteBuffer.allocateDirect(1024);

        try {
            ByteBuffer outBuffer = ByteBuffer.allocateDirect(1024);
            outBuffer.put(parseHexStr2Byte(encodedString));
            outBuffer.flip();
            SecretKeySpec secretKeySpec = generateKey();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, secretKeySpec, iv);
            cipher.update(outBuffer, decoded);
            cipher.doFinal(outBuffer, decoded);
            decoded.flip();
        } catch (Throwable var5) {
            var5.printStackTrace();
        }

        return asString(decoded);
    }

    private static SecretKeySpec generateKey() throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(KEY.getBytes());
        kgen.init(256, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
        return keySpec;
    }

    private static byte[] getUTF8Bytes(String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / 2];

            for (int i = 0; i < hexStr.length() / 2; ++i) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }

            return result;
        }
    }

    private static String asString(ByteBuffer buffer) {
        ByteBuffer copy = buffer.duplicate();
        byte[] bytes = new byte[copy.remaining()];
        copy.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }
}

