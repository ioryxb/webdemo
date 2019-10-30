package com.crxdemo.ticketoffice.exception;

public class LogicException extends RuntimeException {

    private String errorMsg;

    private int code;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    //error message is like 10001_<message>, so index is 6
    private LogicException(String errorMsg) {
        super(errorMsg);
        this.code = Integer.parseInt(errorMsg.substring(0, 5));
        this.errorMsg = errorMsg.substring(6);
    }

    public static LogicException le(String errorMsg) {
        return new LogicException(errorMsg);
    }
}
