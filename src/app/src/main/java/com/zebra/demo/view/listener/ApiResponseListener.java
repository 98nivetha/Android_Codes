package com.zebra.demo.view.listener;

public interface ApiResponseListener {
    public void onSuccess(Object data);
    public void onFailed(Object data);
}
