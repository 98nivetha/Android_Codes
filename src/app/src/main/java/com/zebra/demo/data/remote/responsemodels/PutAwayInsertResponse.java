package com.zebra.demo.data.remote.responsemodels;

public class PutAwayInsertResponse {
    private boolean Result;
    private String ErrorCode;
    private String Message;
    private Object Data;

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {
        this.Result = result;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        this.ErrorCode = errorCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        this.Data = data;
    }
}

