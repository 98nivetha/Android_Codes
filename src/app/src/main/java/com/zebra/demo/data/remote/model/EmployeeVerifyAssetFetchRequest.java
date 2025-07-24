package com.zebra.demo.data.remote.model;

import java.util.List;

public class EmployeeVerifyAssetFetchRequest {

    private List<EmployeeVerifyAssetFetchTagIdRequest> Tagids;

    public EmployeeVerifyAssetFetchRequest() {
    }

    public EmployeeVerifyAssetFetchRequest(List<EmployeeVerifyAssetFetchTagIdRequest> tagids) {
        Tagids = tagids;
    }

    public List<EmployeeVerifyAssetFetchTagIdRequest> getTagids() {
        return Tagids;
    }

    public void setTagids(List<EmployeeVerifyAssetFetchTagIdRequest> tagids) {
        Tagids = tagids;
    }
}
