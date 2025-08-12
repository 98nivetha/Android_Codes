package com.zebra.demo.data.remote.requestmodels;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StockInwardRequest {

    @SerializedName("IbaIvmInvStockinwardPackDetail")
    private List<IbaIvmInvStockinwardPackDetail> IbaIvmInvStockinwardPackDetail;

    public StockInwardRequest(List<IbaIvmInvStockinwardPackDetail> ibaIvmInvStockinwardPackDetail) {
        this.IbaIvmInvStockinwardPackDetail = ibaIvmInvStockinwardPackDetail;
    }

    public List<IbaIvmInvStockinwardPackDetail> getIbaIvmInvStockinwardPackDetail() {
        return IbaIvmInvStockinwardPackDetail;
    }

    public void setIbaIvmInvStockinwardPackDetail(List<IbaIvmInvStockinwardPackDetail> ibaIvmInvStockinwardPackDetail) {
        this.IbaIvmInvStockinwardPackDetail = ibaIvmInvStockinwardPackDetail;
    }

    public static class IbaIvmInvStockinwardPackDetail {

        private Integer Packid;
        private Integer Detailid;
        private Integer Inwardid;
        private Integer Modelid;
        private String Modelname;
        private Integer Materialid;
        private String Materialname;
        private Integer Quantity;
        private String Rfid;
        private Integer Status;
        private Integer Createby;
        private String Createdon;
        private Integer Modifiedby;
        private String Modifiedon;
        private Integer Rackid;

        // Getters with default values to avoid null issues
        public int getPackid() { return Packid != null ? Packid : 0; }
        public void setPackid(Integer packid) { this.Packid = packid; }

        public int getDetailid() { return Detailid != null ? Detailid : 0; }
        public void setDetailid(Integer detailid) { this.Detailid = detailid; }

        public int getInwardid() { return Inwardid != null ? Inwardid : 0; }
        public void setInwardid(Integer inwardid) { this.Inwardid = inwardid; }

        public int getModelid() { return Modelid != null ? Modelid : 0; }
        public void setModelid(Integer modelid) { this.Modelid = modelid; }

        public String getModelname() { return Modelname != null ? Modelname : ""; }
        public void setModelname(String modelname) { this.Modelname = modelname; }

        public int getMaterialid() { return Materialid != null ? Materialid : 0; }
        public void setMaterialid(Integer materialid) { this.Materialid = materialid; }

        public String getMaterialname() { return Materialname != null ? Materialname : ""; }
        public void setMaterialname(String materialname) { this.Materialname = materialname; }

        public int getQuantity() { return Quantity != null ? Quantity : 0; }
        public void setQuantity(Integer quantity) { this.Quantity = quantity; }

        public String getRfid() { return Rfid != null ? Rfid : ""; }
        public void setRfid(String rfid) { this.Rfid = rfid; }

        public int getStatus() { return Status != null ? Status : 0; }
        public void setStatus(Integer status) { this.Status = status; }

        public int getCreateby() { return Createby != null ? Createby : 0; }
        public void setCreateby(Integer createby) { this.Createby = createby; }

        public String getCreatedon() { return Createdon != null ? Createdon : ""; }
        public void setCreatedon(String createdon) { this.Createdon = createdon; }

        public int getModifiedby() { return Modifiedby != null ? Modifiedby : 0; }
        public void setModifiedby(Integer modifiedby) { this.Modifiedby = modifiedby; }

        public String getModifiedon() { return Modifiedon != null ? Modifiedon : ""; }
        public void setModifiedon(String modifiedon) { this.Modifiedon = modifiedon; }

        public int getRackid() { return Rackid != null ? Rackid : 0; }
        public void setRackid(Integer rackid) { this.Rackid = rackid; }
    }
}
