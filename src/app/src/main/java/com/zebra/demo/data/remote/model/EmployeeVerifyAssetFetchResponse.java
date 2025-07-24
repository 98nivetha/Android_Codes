package com.zebra.demo.data.remote.model;

import java.util.List;

public class EmployeeVerifyAssetFetchResponse {

    private String ErStatus;
    private String ErDescription;
    private List<ScanAssetDetail> AssetDetail;


    EmployeeVerifyAssetFetchResponse() {
    }

    public String getErStatus() {
        return ErStatus;
    }

    public void setErStatus(String erStatus) {
        ErStatus = erStatus;
    }

    public String getErDescription() {
        return ErDescription;
    }

    public void setErDescription(String erDescription) {
        ErDescription = erDescription;
    }

    public List<ScanAssetDetail> getAssetDetail() {
        return AssetDetail;
    }

    public void setAssetDetail(List<ScanAssetDetail> assetDetail) {
        AssetDetail = assetDetail;
    }
}
