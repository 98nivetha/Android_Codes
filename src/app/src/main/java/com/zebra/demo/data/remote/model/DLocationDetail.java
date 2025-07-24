package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class DLocationDetail {
    public int Locationid;
    public String Locationcode;
    public String Locationname;
    public String Buildingcode;
    public String Floorcode;
    public String Status;
    public String Createdby;
    public String Createdon;
    public String Modifiedby;
    public String Modifiedon;

    @NonNull
    @Override
    public String toString() {
        return Locationname == null ? "" : Locationname;
    }
}
