package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class DSubAssetDetail {
    public String Subitemname;
    public String Subassetcode;

    @NonNull
    @Override
    public String toString() {
        return Subitemname == null ? "" : Subitemname;
    }
}
