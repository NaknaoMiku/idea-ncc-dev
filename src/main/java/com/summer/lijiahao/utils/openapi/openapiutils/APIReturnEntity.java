package com.summer.lijiahao.utils.openapi.openapiutils;

import java.io.Serializable;


public class APIReturnEntity
        implements Serializable {
    private static final long serialVersionUID = 325316809242559354L;
    private boolean success;
    private Object data;
    private String code;
    private String message;

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
