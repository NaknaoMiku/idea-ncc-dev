package com.summer.lijiahao.script.studio.ui.preference.prop;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

public class AESGeneratorKey {
    public static final String DEFAULT_PROVIDER_NAME = "BC";
    private static final byte[] KEY_END = {43, 65, 23, 6, -54, -24, -16, 26, 7, 34, -29, -52, -14, 27, 38, 41};
    private static final int AESKEY_LENGTH = 256;
    private static final int TRANS_KEY_LENGTH = 32;
    private static final byte[] DEFAULT_TRANS_KEY = {34, 25, 64, 23, 54, 65, 76, 34, -3, -54, -13, -35, 34, 54, 23};
    private static final String DEFAUL_WAY = "SHA1PRNG";

    static {
        if (Security.getProvider("BC") == null) {
            BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
            Security.addProvider(bouncyCastleProvider);
        }
    }

    public static byte[] genKey()
            throws IOException {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            SecretKey secretKey = null;
            KeyGenerator kgen = null;
            if (Security.getProvider("BC") == null) {
                kgen = KeyGenerator.getInstance("AES");
            } else {
                kgen = KeyGenerator.getInstance("AES", "BC");
            }
            kgen.init(256, secureRandom);
            secretKey = kgen.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchProviderException e) {
            throw new IOException("gen key error:" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e.getMessage());
        }
    }

    public static byte[] genKey(byte[] transKey)
            throws IOException {
        byte[] key = new byte[32];
        System.arraycopy(transKey, 8, key, 0, 24);
        System.arraycopy(KEY_END, 0, key, 24, 8);
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            KeyGenerator kgen = null;
            if (Security.getProvider("BC") == null) {
                kgen = KeyGenerator.getInstance("AES");
            } else {
                kgen = KeyGenerator.getInstance("AES", "BC");
            }
            secureRandom.setSeed(key);
            kgen.init(256, secureRandom);
            kgen.generateKey();
            return key;
        } catch (NoSuchProviderException e) {
            throw new IOException("gen key error:" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e.getMessage());
        }
    }

    public static byte[] genTransKey() {
        String ClientIP = null;
        byte[] transKey = new byte[32];
        try {
            ClientIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException localUnknownHostException) {
        }
        if ((ClientIP == null) || (ClientIP.equals(""))) {
            ClientIP = "uap:localHost";
        }
        try {
            byte[] srcKey = ClientIP.getBytes("unicode");
            if (srcKey.length > 32) {
                System.arraycopy(srcKey, 0, transKey, 0, 32);
            } else {
                System.arraycopy(srcKey, 0, transKey, 0, srcKey.length);
                for (int i = srcKey.length; i < 32; i++) {
                    transKey[i] = 0;
                }
            }
            return transKey;
        } catch (UnsupportedEncodingException e) {
        }
        return DEFAULT_TRANS_KEY;
    }

    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static String generHexStrKey() {
        try {
            return parseByte2HexStr(genKey());
        } catch (IOException e) {
            e.printStackTrace();
//            Logger.error(e);
        }
        return null;
    }

    public static byte[] genBindIpKey()
            throws IOException {
        return genKey(genTransKey());
    }
}
