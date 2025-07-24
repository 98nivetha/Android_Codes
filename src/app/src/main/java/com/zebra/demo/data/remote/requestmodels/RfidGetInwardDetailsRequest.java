package com.zebra.demo.data.remote.requestmodels;

public class RfidGetInwardDetailsRequest {
    private int Rfidid;

    public RfidGetInwardDetailsRequest(int rfidid) {
        this.Rfidid = rfidid;
    }

    public int getRfidid() {
        return Rfidid;
    }

    public void setRfidid(int rfidid) {
        this.Rfidid = rfidid;
    }
}
