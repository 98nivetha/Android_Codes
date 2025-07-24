package com.zebra.demo.data.remote.model;

import java.util.List;

public class EmployeeAssetScannedInsertRequest {

    EmployeeDetail EmployeeDetail;
    private List<ScanAssetDetail> AssetDetail;


    EmployeeAssetScannedInsertRequest() {
    }

    public EmployeeAssetScannedInsertRequest(com.zebra.demo.data.remote.model.EmployeeDetail employeeDetail, List<ScanAssetDetail> assetDetail) {
        EmployeeDetail = employeeDetail;
        AssetDetail = assetDetail;
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
