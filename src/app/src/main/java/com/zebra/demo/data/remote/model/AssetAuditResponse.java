package com.zebra.demo.data.remote.model;

import java.util.List;

public class AssetAuditResponse {

    public boolean Result;
    public String ErrorCode = null;
    public String Message;
    public List<AssetAudit> Data;

   /* private AssetAudit Scandetails;
    private ApiResponse ErrorDetail;

    public AssetAudit getScandetails() {
        return Scandetails;
    }

    public void setScandetails(AssetAudit scandetails) {
        Scandetails = scandetails;
    }

    public ApiResponse getErrorDetail() {
        return ErrorDetail;
    }

    public void setErrorDetail(ApiResponse errorDetail) {
        ErrorDetail = errorDetail;
    }*/
}
