package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class AssetCategory {
    public String CategoryCode;
    public String CategoryName;


    @NonNull
    @Override
    public String toString() {
        return  CategoryName == null ? "" : CategoryName;
    }
}
