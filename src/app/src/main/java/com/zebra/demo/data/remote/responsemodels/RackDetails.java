package com.zebra.demo.data.remote.responsemodels;
import com.google.gson.annotations.SerializedName;

public class RackDetails {
    @SerializedName("Rackid")
    private int rackid;

    @SerializedName("Rackgroup")
    private String rackgroup;

    @SerializedName("Rackcode")
    private String rackcode;

    @SerializedName("Rackname")
    private String rackname;

    @SerializedName("Createdby")
    private int createdby;

    @SerializedName("Createdon")
    private String createdon;

    @SerializedName("Modifiedby")
    private int modifiedby;

    @SerializedName("Modifiedon")
    private String modifiedon;

    @SerializedName("Status")
    private int status;
    // Getters
    public Integer getRackid() {
        return rackid;
    }

    public String getRackgroup() {
        return rackgroup;
    }

    public String getRackcode() {
        return rackcode;
    }

    public String getRackname() {
        return rackname;
    }

    public Integer getCreatedby() {
        return createdby;
    }

    public Integer getModifiedby() {
        return modifiedby;
    }

    public Integer getStatus() {
        return status;
    }

    public String getCreatedon() {
        return createdon;
    }

    public String getModifiedon() {
        return modifiedon;
    }

    // Setters
    public void setRackid(Integer rackid) {
        this.rackid = rackid;
    }

    public void setRackgroup(String rackgroup) {
        this.rackgroup = rackgroup;
    }

    public void setRackcode(String rackcode) {
        this.rackcode = rackcode;
    }

    public void setRackname(String rackname) {
        this.rackname = rackname;
    }

    public void setCreatedby(Integer createdby) {
        this.createdby = createdby;
    }

    public void setModifiedby(Integer modifiedby) {
        this.modifiedby = modifiedby;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public void setModifiedon(String modifiedon) {
        this.modifiedon = modifiedon;
    }
    @Override
    public String toString() {
        return rackname; // or customize as needed
    }

}
