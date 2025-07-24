package com.zebra.demo.data.remote.model;

public class EmployeesAllocatedAssetDetailRequest {

    private String Tagid;
    private String EmployeeCode;

    public EmployeesAllocatedAssetDetailRequest() {
    }

    public EmployeesAllocatedAssetDetailRequest(String tagid, String employeeCode) {
        Tagid = tagid;
        EmployeeCode = employeeCode;
    }

    public String getTagid() {
        return Tagid;
    }

    public void setTagid(String tagid) {
        Tagid = tagid;
    }

    public String getEmployeeCode() {
        return EmployeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        EmployeeCode = employeeCode;
    }

}
