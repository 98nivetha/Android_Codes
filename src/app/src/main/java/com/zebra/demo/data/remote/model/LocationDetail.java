package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class LocationDetail {
    public String LCode;
    public String LDesc;
    public String rstatus;

    @NonNull
    @Override
    public String toString() {
        return  LDesc == null ? "" : LDesc;
    }
}
