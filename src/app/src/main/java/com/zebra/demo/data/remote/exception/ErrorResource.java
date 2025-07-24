package com.zebra.demo.data.remote.exception;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ErrorResource {
    @Expose
    private Integer status;
    @Expose
    @SerializedName(value = "msg")
    private String message;
    
    public ErrorResource() {
    }
    
    public ErrorResource(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "ErrorResource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}