package com.activiti.exception;

public class ApplicationException extends BaseException {

    private static final long serialVersionUID = 125160326045586044L;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String msg) {
        super(msg);
    }

    public ApplicationException(String msg, Throwable innerException) {
        super(msg, innerException);
    }

    public ApplicationException(String errorCode, String msg, Throwable innerException) {
        super(errorCode, msg, innerException);
    }
}
