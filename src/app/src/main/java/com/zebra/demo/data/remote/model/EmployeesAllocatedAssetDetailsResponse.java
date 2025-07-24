package com.zebra.demo.data.remote.model;

import java.util.List;

public class EmployeesAllocatedAssetDetailsResponse {

    private String ErStatus;
    private String ErDescription;
    EmployeeDetail EmployeeDetail;
    private List<ScanAssetDetail> AssetDetail;


    EmployeesAllocatedAssetDetailsResponse() {
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

    public com.zebra.demo.data.remote.model.EmployeeDetail getEmployeeDetail() {
        return EmployeeDetail;
    }

    public void setEmployeeDetail(com.zebra.demo.data.remote.model.EmployeeDetail employeeDetail) {
        EmployeeDetail = employeeDetail;
    }

    public List<ScanAssetDetail> getAssetDetail() {
        return AssetDetail;
    }

    public void setAssetDetail(List<ScanAssetDetail> assetDetail) {
        AssetDetail = assetDetail;
    }
}
