package com.zebra.demo.data.remote.model;

public class RFIDTagMappingRequest {
        private String rfidTag;
    private String assetCode;
    private int Createdby;

    public RFIDTagMappingRequest(String rfidTag, String assetCode, int createdby) {
        this.rfidTag = rfidTag;
        this.assetCode = assetCode;
        Createdby = createdby;
    }

    public String getRfidTag() {
        return rfidTag;
    }

    public void setRfidTag(String rfidTag) {
        this.rfidTag = rfidTag;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public int getCreatedby() {
        return Createdby;
    }

    public void setCreatedby(int createdby) {
        Createdby = createdby;
    }
}
