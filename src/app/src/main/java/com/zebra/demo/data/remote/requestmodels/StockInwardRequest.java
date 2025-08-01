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
        private int Packid;
        private int Detailid;
        private int Inwardid;
        private int Modelid;
        private String Modelname;
        private int Materialid;
        private String Materialname;
        private int Quantity;
        private String Rfid;
        private int Status;
        private int Createby;
        private String Createdon;
        private int Modifiedby;
        private String Modifiedon;
        private int Rackid;

        // Getters and Setters
        public int getPackid() { return Packid; }
        public void setPackid(int packid) { this.Packid = packid; }

        public int getDetailid() { return Detailid; }
        public void setDetailid(int detailid) { this.Detailid = detailid; }

        public int getInwardid() { return Inwardid; }
        public void setInwardid(int inwardid) { this.Inwardid = inwardid; }

        public int getModelid() { return Modelid; }
        public void setModelid(int modelid) { this.Modelid = modelid; }

        public String getModelname() { return Modelname; }
        public void setModelname(String modelname) { this.Modelname = modelname; }

        public int getMaterialid() { return Materialid; }
        public void setMaterialid(int materialid) { this.Materialid = materialid; }

        public String getMaterialname() { return Materialname; }
        public void setMaterialname(String materialname) { this.Materialname = materialname; }

        public int getQuantity() { return Quantity; }
        public void setQuantity(int quantity) { this.Quantity = quantity; }

        public String getRfid() { return Rfid; }
        public void setRfid(String rfid) { this.Rfid = rfid; }

        public int getStatus() { return Status; }
        public void setStatus(int status) { this.Status = status; }

        public int getCreateby() { return Createby; }
        public void setCreateby(int createby) { this.Createby = createby; }

        public String getCreatedon() { return Createdon; }
        public void setCreatedon(String createdon) { this.Createdon = createdon; }

        public int getModifiedby() { return Modifiedby; }
        public void setModifiedby(int modifiedby) { this.Modifiedby = modifiedby; }

        public String getModifiedon() { return Modifiedon; }
        public void setModifiedon(String modifiedon) { this.Modifiedon = modifiedon; }

        public int getRackid() { return Rackid; }
        public void setRackid(int rackid) { this.Rackid = rackid; }
    }
}

