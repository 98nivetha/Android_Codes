package com.zebra.demo.data.remote.requestmodels;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReturnPartsValidInfoRequest {

    @SerializedName("Rfids")
    private List<String> rfids;

    public ReturnPartsValidInfoRequest() {
    }

    public ReturnPartsValidInfoRequest(List<String> rfids) {
        this.rfids = rfids;
    }

    public List<String> getRfids() {
        return rfids;
    }

    public void setRfids(List<String> rfids) {
        this.rfids = rfids;
    }

    @Override
    public String toString() {
        return "ReturnPartsValidInfoRequest{" +
                "rfids=" + rfids +
                '}';
    }
}
