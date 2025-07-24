package com.zebra.demo.data.remote.responsemodels;

public class GetSingleMaterialDataResponse {
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
        private V_RfidDetails V_RfidDetails;

        public V_RfidDetails getV_RfidDetails() {
            return V_RfidDetails;
        }

        public void setV_RfidDetails(V_RfidDetails v_RfidDetails) {
            V_RfidDetails = v_RfidDetails;
        }
    }

    // Nested V_RfidDetails class
    public static class V_RfidDetails {
        private int Packid;
        private int Detailid;
        private int Inwardid;
        private int Modelid;
        private int Materialid;
        private String Materialcode;
        private String Materialname;
        private int Packingstandard;
        private int Quantity;
        private String Rfid;
        private int Status;
        private int Createby;
        private String Createdon;
        private int Modifiedby;
        private String Modifiedon;
        private int Rackid;
        private String Inwarddate;
        private String Rackgroup;
        private String Rackcode;
        private String Rackname;

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

        public String getMaterialcode() {
            return Materialcode;
        }

        public void setMaterialcode(String materialcode) {
            Materialcode = materialcode;
        }

        public String getMaterialname() {
            return Materialname;
        }

        public void setMaterialname(String materialname) {
            Materialname = materialname;
        }

        public int getPackingstandard() {
            return Packingstandard;
        }

        public void setPackingstandard(int packingstandard) {
            Packingstandard = packingstandard;
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

        public Integer getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public int getCreateby() {
            return Createby;
        }

        public void setCreateby(int createby) {
            Createby = createby;
        }

        public String getCreatedon() {
            return Createdon;
        }

        public void setCreatedon(String createdon) {
            Createdon = createdon;
        }

        public int getModifiedby() {
            return Modifiedby;
        }

        public void setModifiedby(int modifiedby) {
            Modifiedby = modifiedby;
        }

        public String getModifiedon() {
            return Modifiedon;
        }

        public void setModifiedon(String modifiedon) {
            Modifiedon = modifiedon;
        }

        public int getRackid() {
            return Rackid;
        }

        public void setRackid(int rackid) {
            Rackid = rackid;
        }

        public String getInwarddate() {
            return Inwarddate;
        }

        public void setInwarddate(String inwarddate) {
            Inwarddate = inwarddate;
        }

        public String getRackgroup() {
            return Rackgroup;
        }

        public void setRackgroup(String rackgroup) {
            Rackgroup = rackgroup;
        }

        public String getRackcode() {
            return Rackcode;
        }

        public void setRackcode(String rackcode) {
            Rackcode = rackcode;
        }

        public String getRackname() {
            return Rackname;
        }

        public void setRackname(String rackname) {
            Rackname = rackname;
        }
    }
}
