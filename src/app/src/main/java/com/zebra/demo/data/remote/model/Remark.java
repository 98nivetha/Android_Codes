package com.zebra.demo.data.remote.model;

public class Remark {
    private String RemarksCode;
    private String RemarksName;
    private String RStatus;

    Remark() {
    }

    public Remark(String remarksCode, String remarksName, String RStatus) {
        RemarksCode = remarksCode;
        RemarksName = remarksName;
        this.RStatus = RStatus;
    }

    public String getRemarksCode() {
        return RemarksCode;
    }

    public void setRemarksCode(String remarksCode) {
        RemarksCode = remarksCode;
    }

    public String getRemarksName() {
        return RemarksName;
    }

    public void setRemarksName(String remarksName) {
        RemarksName = remarksName;
    }

    public String getRStatus() {
        return RStatus;
    }

    public void setRStatus(String RStatus) {
        this.RStatus = RStatus;
    }
}
