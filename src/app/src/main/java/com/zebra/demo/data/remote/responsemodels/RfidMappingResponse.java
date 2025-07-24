package com.zebra.demo.data.remote.responsemodels;

public class RfidMappingResponse {
    private boolean Result;
    private Object ErrorCode;
    private String Message;
    private RfidMappingData Data;

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {
        Result = result;
    }

    public Object getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(Object errorCode) {
        ErrorCode = errorCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public RfidMappingData getData() {
        return Data;
    }

    public void setData(RfidMappingData data) {
        Data = data;
    }
}

