package com.zebra.demo.data.remote.listener;


public interface ResponseListener<T> {

    void onDataReceived(T root);
    void onError(String e);
    void onFailure(Throwable t);
}
