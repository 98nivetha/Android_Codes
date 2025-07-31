package com.zebra.demo.data.remote.requestmodels;

import java.util.List;

public class PutAwayScanningRequest {
    private int Rackid;
    private List<String> Rfids;

    // Constructor
    public PutAwayScanningRequest(int rackid, List<String> rfids) {
        this.Rackid = rackid;
        this.Rfids = rfids;
    }

    // Getter and Setter for Rackid
    public int getRackid() {
        return Rackid;
    }

    public void setRackid(int rackid) {
        this.Rackid = rackid;
    }

    // Getter and Setter for Rfids
    public List<String> getRfids() {
        return Rfids;
    }

    public void setRfids(List<String> rfids) {
        this.Rfids = rfids;
    }


    @Override
    public String toString() {
        return "PutAwayScanningRequest{Rackid=" + Rackid + ", Rfids=" + Rfids + "}";
    }
}

