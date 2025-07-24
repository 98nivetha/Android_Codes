package com.zebra.demo.data.remote.responsemodels;

public class InwardDetail {
    private int Inwardid;
    private String Inwardcode;
    private String Inwarddate;
    private int Totalquantity;
    private double Totalprice;
    private int Status;

    public int getInwardid() {
        return Inwardid;
    }

    public void setInwardid(int inwardid) {
        Inwardid = inwardid;
    }

    public String getInwardcode() {
        return Inwardcode;
    }

    public void setInwardcode(String inwardcode) {
        Inwardcode = inwardcode;
    }

    public String getInwarddate() {
        return Inwarddate;
    }

    public void setInwarddate(String inwarddate) {
        Inwarddate = inwarddate;
    }

    public int getTotalquantity() {
        return Totalquantity;
    }

    public void setTotalquantity(int totalquantity) {
        Totalquantity = totalquantity;
    }

    public double getTotalprice() {
        return Totalprice;
    }

    public void setTotalprice(double totalprice) {
        Totalprice = totalprice;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
