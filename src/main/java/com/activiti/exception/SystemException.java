package com.activiti.exception;

public class SystemException extends BaseException {

    private static final long serialVersionUID = 125160326045586044L;

    public SystemException() {
        super();
    }

    public SystemException(String msg) {
        super(msg);
    }

    public SystemException(String msg, Throwable innerException) {
        super(msg, innerException);
    }

    public SystemException(String errorCode, String msg, Throwable innerException) {
        super(errorCode, msg, innerException);
    }
}
