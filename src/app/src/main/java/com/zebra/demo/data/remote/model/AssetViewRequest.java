package com.zebra.demo.data.remote.model;

public class AssetViewRequest {
    private String RFIDTag;

    public AssetViewRequest(String RFIDTag) {
        this.RFIDTag = RFIDTag;
    }

    public String getRFIDTag() {
        return RFIDTag;
    }

    public void setRFIDTag(String RFIDTag) {
        this.RFIDTag = RFIDTag;
    }
}
