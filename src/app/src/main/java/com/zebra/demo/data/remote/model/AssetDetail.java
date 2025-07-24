package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class AssetDetail {

    public String AssetCode;
    public String EmployeeName;
    public String Location;
    public String LocationEmployee;
    public String AssetName;
    public String Department;
    public String AssetType;
    public String AssetCategory;
    public double UnitPrice;
    public String Manufacture;
    public String ImagePath;
    public String PurchaseOrderNo;
    public String PurchasedDate;
    public long AssetLife;
    public String InvoiceDate;
    public String WarrantyStartDate;
    public String ExpiredDate;
    public String Usage;
    public String UatDevice;
    public String SupplierName;
    public String OrgUnit;
    public double Cgst;
    public double Sgst;
    public double Igst;
    public double NetAmount;
    public String MacAddress;
    public String HostName;
    public String Ownership;
    public String Status;
    public String RFIDTag;




    public AssetDetail( String RFIDTag,  String location,  String department, String category, String assetName, String assetCode) {
        this.RFIDTag = RFIDTag;
        Location = location;
        Department = department;
        AssetCategory = category;
        AssetName = assetName;
        AssetCode = assetCode;
    }



    /*

    public String AssetSrCode;
              public String AssetCode;
              public String AssetName;
              public String Group;
              public String Department;
              public String costcenter;
              public String RegisterDate;
              public String Location;
              public String condition;
              public String purchasedate;
              public String Vendor;
              public String Purchasecost;
              public String AssetLife;

              public String MappedStatus;
              public String RFIFTag;

    * */

    @NonNull
    @Override
    public String toString() {
        return AssetCode == null ? "" : AssetCode;
    }
}
