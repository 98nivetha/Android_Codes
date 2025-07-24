package com.zebra.demo.data.remote.requestmodels;

public class GetAllMaterialInfoRequest {
    private String _Qrcode;

    GetAllMaterialInfoRequest(String qrcode) {
        this._Qrcode = qrcode;
    }

    public String getQrCode() {
        return _Qrcode;
    }

    public void set_Qrcode(String qrcode) {
        this._Qrcode = qrcode;
    }
}
