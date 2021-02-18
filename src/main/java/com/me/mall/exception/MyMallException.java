package com.me.mall.exception;

/**
 * 描述：统一异常
 */
public class MyMallException extends Exception {
    private final Integer code;
    private final String message;

    public MyMallException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public MyMallException(MyMallExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
