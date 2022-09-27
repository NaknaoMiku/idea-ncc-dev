package com.summer.lijiahao.utils.openapi.openapiutils;

import java.security.KeyPair;


public class KeyPairs {
    private final KeyPair keyPair;

    public KeyPairs(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String getPublicKey() {
        return Base64Util.encryptBASE64(this.keyPair.getPublic().getEncoded());
    }

    public String getPrivateKey() {
        return Base64Util.encryptBASE64(this.keyPair.getPrivate().getEncoded());
    }
}
