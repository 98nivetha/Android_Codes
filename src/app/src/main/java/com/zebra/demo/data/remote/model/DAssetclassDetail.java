package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class DAssetclassDetail {
    public int Assetclassid;
    public String Assetclasscode;
    public String Assetclassdescription;
    public String Status;
    public String Createdby;
    public String Createdon;
    public String Modifiedby;
    public String Modifiedon;
    public String Assettype;

    @NonNull
    @Override
    public String toString() {
        return Assetclassdescription == null ? "" : Assetclassdescription;
    }
}
