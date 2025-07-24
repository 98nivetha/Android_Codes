package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class AssetHeader {

    public String AssetSrCode;
    public String AssetCode;
    public String AssetName;
    public String Category;
    public String CategoryName;
    public String Department;
    public String DeptDesc;
    public String Location;
    public String LocationName;
    public String RFIDTag;
    public int MappedStatus;
    public String CreatedBy;

    @NonNull
    @Override
    public String toString() {
        return AssetName == null ? "" : AssetName;
    }
}


/*
    public String AssetSrCode;
    public String AssetCode;
    public String AssetName;
    public String Group;
    public String Department;
    public String Location;
    public int MappedStatus;
    public String RFIFTag;
* */