package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class DBuildingDetail {

    public int Buildingid;
    public String Companycode;
    public String Buildingcode;
    public String Buildingname;
    public String Status;
    public String Createdby;
    public String Createdon;
    public String Modifiedby;
    public String Modifiedon;

    @NonNull
    @Override
    public String toString() {
        return Buildingname == null ? "" : Buildingname;
    }
}
