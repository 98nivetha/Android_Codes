package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class DFloorDetail {
    public int Floorid;
    public String Floorcode;
    public String Floorname;
    public String Status;
    public String Createdby;
    public String Createdon;
    public String Modifiedby;
    public String Modifiedon;

    @NonNull
    @Override
    public String toString() {
        return Floorname == null ? "" : Floorname;
    }
}
