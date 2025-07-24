package com.zebra.demo.data.remote.responsemodels;

public class RfidDetail {
    private int Packid;
    private int Detailid;
    private int Inwardid;
    private int Modelid;
    private int Materialid;
    private int Quantity;
    private String Rfid;
    private int Status;
    private Integer Rackid;

    // Getters and Setters
    public int getPackid() {
        return Packid;
    }

    public void setPackid(int packid) {
        Packid = packid;
    }

    public int getDetailid() {
        return Detailid;
    }

    public void setDetailid(int detailid) {
        Detailid = detailid;
    }

    public int getInwardid() {
        return Inwardid;
    }

    public void setInwardid(int inwardid) {
        Inwardid = inwardid;
    }

    public int getModelid() {
        return Modelid;
    }

    public void setModelid(int modelid) {
        Modelid = modelid;
    }

    public int getMaterialid() {
        return Materialid;
    }

    public void setMaterialid(int materialid) {
        Materialid = materialid;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getRfid() {
        return Rfid;
    }

    public void setRfid(String rfid) {
        Rfid = rfid;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public Integer getRackid() {
        return Rackid;
    }

    public void setRackid(Integer rackid) {
        Rackid = rackid;
    }
}

