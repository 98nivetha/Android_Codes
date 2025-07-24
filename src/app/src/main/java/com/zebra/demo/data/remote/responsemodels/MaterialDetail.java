package com.zebra.demo.data.remote.responsemodels;

public class MaterialDetail {
    public String Materialcode;
    public String Materialname;
    public Integer Quantity;
    public Integer Packingstandard;

    public String getMaterialcode() {
        return Materialcode;
    }

    public String getMaterialname() {
        return Materialname;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public Integer getPackingstandard() {
        return Packingstandard;
    }

    public void setMaterialcode(String materialname) {
        this.Materialname = materialname;
    }

    public void setMaterialname(String materialcode) {
        this.Materialcode = materialcode;
    }

    public void setQuantity(Integer quantity) {
        this.Quantity = quantity;
    }

    public void setPackingstandard(Integer packingstandard) {
        this.Packingstandard = packingstandard;
    }
}
