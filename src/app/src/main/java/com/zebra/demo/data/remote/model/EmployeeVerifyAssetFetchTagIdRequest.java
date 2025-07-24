package com.zebra.demo.data.remote.model;

public class EmployeeVerifyAssetFetchTagIdRequest {



    private String Tagid;

    public EmployeeVerifyAssetFetchTagIdRequest() {
    }



    public EmployeeVerifyAssetFetchTagIdRequest(String tagid) {
        Tagid = tagid;
    }

    public String getTagid() {
        return Tagid;
    }

    public void setTagid(String tagid) {
        Tagid = tagid;
    }
}
