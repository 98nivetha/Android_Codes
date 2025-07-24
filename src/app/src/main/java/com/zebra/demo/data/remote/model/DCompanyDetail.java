package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class DCompanyDetail {
    public int Companyid;
    public String Companycode;
    public String Companyname;
    public String Address1;
    public String Address2;
    public String Place;
    public String Telephoneno;
    public String Areacode;
    public String Tngstno;
    public String Cstno;
    public String Gstnumber;
    public String Authorisedsignatory;
    public String Designation;
    public String Tin;
    public String Cinnumber;
    public String Country;
    public String City;
    public String Pincode;
    public String State;
    public String Panno;
    public String Status;
    public String Createdby;
    public String Createdon;
    public String Modifiedby;
    public String Modifiedon;
    public String Accountingyear;

    @NonNull
    @Override
    public String toString() {
        return Companyname == null ? "" : Companyname;
    }
}
