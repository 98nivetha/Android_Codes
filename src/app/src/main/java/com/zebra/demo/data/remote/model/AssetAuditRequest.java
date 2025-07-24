package com.zebra.demo.data.remote.model;

public class AssetAuditRequest {
    private String Rfidtag;
    private String Auditcode;
    private String Auditby;

    public String getRfidtag() {
        return Rfidtag;
    }

    public void setRfidtag(String rfidtag) {
        Rfidtag = rfidtag;
    }

    public String getAuditcode() {
        return Auditcode;
    }

    public void setAuditcode(String auditcode) {
        Auditcode = auditcode;
    }

    public String getAuditby() {
        return Auditby;
    }

    public void setAuditby(String auditby) {
        Auditby = auditby;
    }
}

/*
private String Auditid;
private String tagid;
private String scanlocation;
private String scanedby;

public String getAuditid() {
    return Auditid;
}

public void setAuditid(String auditid) {
    Auditid = auditid;
}

public String getTagid() {
    return tagid;
}

public void setTagid(String tagid) {
    this.tagid = tagid;
}

public String getScanlocation() {
    return scanlocation;
}

public void setScanlocation(String scanlocation) {
    this.scanlocation = scanlocation;
}

public String getScanedby() {
    return scanedby;
}

public void setScanedby(String scanedby) {
    this.scanedby = scanedby;
}*/
