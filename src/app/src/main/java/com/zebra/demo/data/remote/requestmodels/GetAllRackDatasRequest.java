package com.zebra.demo.data.remote.requestmodels;

public class GetAllRackDatasRequest {
    private String _rackcode;

    GetAllRackDatasRequest(String rackcode) {
        this._rackcode = rackcode;
    }

    public String getRackcode() {
        return _rackcode;
    }

    public void setRackCode(String rackcode) {
        this._rackcode = rackcode;
    }
}
