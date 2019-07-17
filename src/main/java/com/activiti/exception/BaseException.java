package com.activiti.exception;

public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // 400 BAD_REQUEST as default
    protected String errorCode;

    public BaseException() {
        super();
    }

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(String msg, Throwable innerException) {
        super(msg, innerException);
    }

    public BaseException(String errorCode, String msg, Throwable innerException) {
        super(msg, innerException);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
