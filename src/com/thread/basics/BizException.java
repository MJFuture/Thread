package com.thread.basics;

/**
 * 自定義異常
 * @author mjun
 *
 */
public class BizException extends Exception {

    //错误信息

    private String message;

    //错误码

    private String errorCode;

    public BizException() {

    }

    /**
     * 自定義異常
     * @param message  異常內容
     * @param errorCode 異常 號碼
     */
    public BizException(String message, String errorCode) {

        this.message = message;

        this.errorCode = errorCode;

    }

    @Override

    public String getMessage() {

        return message;

    }

    public void setMessage(String message) {

        this.message = message;

    }

    public String getErrorCode() {

        return errorCode;

    }

    public void setErrorCode(String errorCode) {

        this.errorCode = errorCode;

    }

}
