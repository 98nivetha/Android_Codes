package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class DSubAssetPartDetail {
    public int Assetregisterid;
    public String Companycode;
    public String SapAssetNo;
    public String SapAssetName;
    public int OverallQuantity;
    public String Serialnumber;
    public String Inventorynumber;
    public String Assetclass;
    public String Assetclassname;
    public String Costcenter;
    public String Costcentername;
    public String Building;
    public String Buildingname;
    public String Status;
    public String Statusname;
    public int Subassetid;
    public String Subassetcode;
    public String Make;
    public String Makename;
    public String Subitemname;
    public int Quantity;
    public String Uom;
    public String Floorcode;
    public String Floorname;
    public String Locationcode;
    public String Locationname;
    public String Subassetpartscode;
    public String RfidTag;
    public int Tagstatus;
    public String Createdby;
    public String Createdon;
    public String Modifiedon;
    public String Modifiedby;
    public int Assetrfidmappingid;
    public String Assetrfidmappingcode;

    @NonNull
    @Override
    public String toString() {
        return Subassetpartscode == null ? "" : Subassetpartscode;
    }
}
