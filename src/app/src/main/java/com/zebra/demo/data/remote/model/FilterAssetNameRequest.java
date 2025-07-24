package com.zebra.demo.data.remote.model;

public class FilterAssetNameRequest {

    private String Department;
    private String category;
    private String Location;
    private int MappedStatus;

    public FilterAssetNameRequest(String department, String category, String location, int mappedStatus) {
        Department = department;
        this.category = category;
        Location = location;
        MappedStatus = mappedStatus;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getMappedStatus() {
        return MappedStatus;
    }

    public void setMappedStatus(int mappedStatus) {
        MappedStatus = mappedStatus;
    }
}
