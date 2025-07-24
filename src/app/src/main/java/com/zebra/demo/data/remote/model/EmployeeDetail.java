package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class EmployeeDetail {

    private String EmployeeCode;
    private String EmployeeName;
    private String DepartmentCode;
    private String DepartmentName;
    private String DesignationCode;
    private String DesignationName;
    private String Mailid;
    private String Mobile;
    private String Tagid;
    private String Status;
    private String Remarks;
    private String CreatedBy;

    /*private String EmployeeCode;
    private String EmployeeName;
    private String DepartmentCode;
    private String DepartmentName;
    private String Tagid;*/

    public EmployeeDetail() {
    }

   /* public EmployeeDetail(String employeeCode, String employeeName, String departmentCode, String departmentName, String tagid) {
        EmployeeCode = employeeCode;
        EmployeeName = employeeName;
        DepartmentCode = departmentCode;
        DepartmentName = departmentName;
        Tagid = tagid;
    }*/

    public String getEmployeeCode() {
        return EmployeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        EmployeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        DepartmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getTagid() {
        return Tagid;
    }

    public void setTagid(String tagid) {
        Tagid = tagid;
    }

    public String getDesignationCode() {
        return DesignationCode;
    }

    public void setDesignationCode(String designationCode) {
        DesignationCode = designationCode;
    }

    public String getDesignationName() {
        return DesignationName;
    }

    public void setDesignationName(String designationName) {
        DesignationName = designationName;
    }

    public String getMailid() {
        return Mailid;
    }

    public void setMailid(String mailid) {
        Mailid = mailid;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    @NonNull
    @Override
    public String toString() {
        return getEmployeeName();
    }
}
