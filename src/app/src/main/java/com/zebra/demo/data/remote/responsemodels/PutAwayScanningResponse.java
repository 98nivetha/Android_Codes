package com.zebra.demo.data.remote.responsemodels;
import java.util.List;

public class PutAwayScanningResponse {
    private boolean Result;
    private String ErrorCode;
    private String Message;
    private Data Data;

    // Getters and Setters
    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {
        Result = result;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Data getData() {
        return Data;
    }

    public void setData(Data data) {
        Data = data;
    }


    // Nested Data class
    public static class Data {
        private List<MaterialItem> Valid;
        private List<MaterialItem> Invalid;
        private int TotalScanned;

        public List<MaterialItem> getValid() {
            return Valid;
        }

        public void setValid(List<MaterialItem> valid) {
            Valid = valid;
        }

        public List<MaterialItem> getInvalid() {
            return Invalid;
        }

        public void setInvalid(List<MaterialItem> invalid) {
            Invalid = invalid;
        }

        public int getTotalScanned() {
            return TotalScanned;
        }

        public void setTotalScanned(int totalScanned) {
            TotalScanned = totalScanned;
        }
    }

    public static class MaterialItem {
        private int Packid;
        private int Detailid;
        private int Inwardid;
        private Integer Modelid;
        private int Materialid;
        private String Materialcode;
        private String Materialname;
        private boolean Cci;
        private int Actualrack;
        private int Quantity;
        private String Rfid;
        private int Rackid;
        private String Rackcode;
        private String Rackgroup;
        private boolean Racktype;
        private String Stockoutdate;
        private int Status;
        private int Createby;
        private String Createdon;
        private int Modifiedby;
        private String Modifiedon;

        // Getters and Setters for all fields
        public int getPackid() { return Packid; }
        public void setPackid(int packid) { Packid = packid; }

        public int getDetailid() { return Detailid; }
        public void setDetailid(int detailid) { Detailid = detailid; }

        public int getInwardid() { return Inwardid; }
        public void setInwardid(int inwardid) { Inwardid = inwardid; }

        public Integer getModelid() { return Modelid; }
        public void setModelid(Integer modelid) { Modelid = modelid; }

        public int getMaterialid() { return Materialid; }
        public void setMaterialid(int materialid) { Materialid = materialid; }

        public String getMaterialcode() { return Materialcode; }
        public void setMaterialcode(String materialcode) { Materialcode = materialcode; }

        public String getMaterialname() { return Materialname; }
        public void setMaterialname(String materialname) { Materialname = materialname; }

        public boolean isCci() { return Cci; }
        public void setCci(boolean cci) { Cci = cci; }

        public int getActualrack() { return Actualrack; }
        public void setActualrack(int actualrack) { Actualrack = actualrack; }

        public int getQuantity() { return Quantity; }
        public void setQuantity(int quantity) { Quantity = quantity; }

        public String getRfid() { return Rfid; }
        public void setRfid(String rfid) { Rfid = rfid; }

        public int getRackid() { return Rackid; }
        public void setRackid(int rackid) { Rackid = rackid; }

        public String getRackcode() { return Rackcode; }
        public void setRackcode(String rackcode) { Rackcode = rackcode; }

        public String getRackgroup() { return Rackgroup; }
        public void setRackgroup(String rackgroup) { Rackgroup = rackgroup; }

        public boolean isRacktype() { return Racktype; }
        public void setRacktype(boolean racktype) { Racktype = racktype; }

        public String getStockoutdate() { return Stockoutdate; }
        public void setStockoutdate(String stockoutdate) { Stockoutdate = stockoutdate; }

        public int getStatus() { return Status; }
        public void setStatus(int status) { Status = status; }

        public int getCreateby() { return Createby; }
        public void setCreateby(int createby) { Createby = createby; }

        public String getCreatedon() { return Createdon; }
        public void setCreatedon(String createdon) { Createdon = createdon; }

        public int getModifiedby() { return Modifiedby; }
        public void setModifiedby(int modifiedby) { Modifiedby = modifiedby; }

        public String getModifiedon() { return Modifiedon; }
        public void setModifiedon(String modifiedon) { Modifiedon = modifiedon; }
    }

}

