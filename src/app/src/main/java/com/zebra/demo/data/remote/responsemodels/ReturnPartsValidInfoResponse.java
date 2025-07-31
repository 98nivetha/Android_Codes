package com.zebra.demo.data.remote.responsemodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReturnPartsValidInfoResponse {

    @SerializedName("Result")
    private boolean result;

    @SerializedName("ErrorCode")
    private String errorCode;

    @SerializedName("Message")
    private String message;

    @SerializedName("Data")
    private Data data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public static class Data {
        @SerializedName("Valid")
        private List<ValidItem> valid;

        @SerializedName("TotalScanned")
        private int totalScanned;

        public List<ValidItem> getValid() {
            return valid;
        }

        public int getTotalScanned() {
            return totalScanned;
        }
    }

    public static class ValidItem implements Parcelable {
        // All your fields here
        @SerializedName("Packid")
        private int packid;

        @SerializedName("Detailid")
        private int detailid;

        @SerializedName("Inwardid")
        private int inwardid;

        @SerializedName("Modelid")
        private int modelid;

        @SerializedName("Materialid")
        private int materialid;

        @SerializedName("Materialcode")
        private String materialcode;

        @SerializedName("Materialname")
        private String materialname;

        @SerializedName("Cci")
        private boolean cci;

        @SerializedName("Actualrack")
        private int actualrack;

        @SerializedName("Quantity")
        private int quantity;

        @SerializedName("Rfid")
        private String rfid;

        @SerializedName("Rackid")
        private int rackid;

        @SerializedName("Rackcode")
        private String rackcode;

        @SerializedName("Rackgroup")
        private String rackgroup;

        @SerializedName("Racktype")
        private String racktype;

        @SerializedName("Stockoutdate")
        private String stockoutdate;

        @SerializedName("Status")
        private int status;

        @SerializedName("Createby")
        private int createby;

        @SerializedName("Createdon")
        private String createdon;

        @SerializedName("Modifiedby")
        private int modifiedby;

        @SerializedName("Modifiedon")
        private String modifiedon;

        protected ValidItem(Parcel in) {
            packid = in.readInt();
            detailid = in.readInt();
            inwardid = in.readInt();
            modelid = in.readInt();
            materialid = in.readInt();
            materialcode = in.readString();
            materialname = in.readString();
            cci = in.readByte() != 0;
            actualrack = in.readInt();
            quantity = in.readInt();
            rfid = in.readString();
            rackid = in.readInt();
            rackcode = in.readString();
            rackgroup = in.readString();
            racktype = in.readString();
            stockoutdate = in.readString();
            status = in.readInt();
            createby = in.readInt();
            createdon = in.readString();
            modifiedby = in.readInt();
            modifiedon = in.readString();
        }

        public static final Parcelable.Creator<ValidItem> CREATOR = new Creator<ValidItem>() {
            @Override
            public ValidItem createFromParcel(Parcel in) {
                return new ValidItem(in);
            }

            @Override
            public ValidItem[] newArray(int size) {
                return new ValidItem[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(packid);
            dest.writeInt(detailid);
            dest.writeInt(inwardid);
            dest.writeInt(modelid);
            dest.writeInt(materialid);
            dest.writeString(materialcode);
            dest.writeString(materialname);
            dest.writeByte((byte) (cci ? 1 : 0));
            dest.writeInt(actualrack);
            dest.writeInt(quantity);
            dest.writeString(rfid);
            dest.writeInt(rackid);
            dest.writeString(rackcode);
            dest.writeString(rackgroup);
            dest.writeString(racktype);
            dest.writeString(stockoutdate);
            dest.writeInt(status);
            dest.writeInt(createby);
            dest.writeString(createdon);
            dest.writeInt(modifiedby);
            dest.writeString(modifiedon);
        }

        @Override
        public int describeContents() {
            return 0;
        }
        // Getters
        public int getPackid() { return packid; }
        public int getDetailid() { return detailid; }
        public int getInwardid() { return inwardid; }
        public int getModelid() { return modelid; }
        public int getMaterialid() { return materialid; }
        public String getMaterialcode() { return materialcode; }
        public String getMaterialname() { return materialname; }
        public boolean isCci() { return cci; }
        public int getActualrack() { return actualrack; }
        public int getQuantity() { return quantity; }
        public String getRfid() { return rfid; }
        public int getRackid() { return rackid; }
        public String getRackcode() { return rackcode; }
        public String getRackgroup() { return rackgroup; }
        public String getRacktype() { return racktype; }
        public String getStockoutdate() { return stockoutdate; }
        public int getStatus() { return status; }
        public int getCreateby() { return createby; }
        public String getCreatedon() { return createdon; }
        public int getModifiedby() { return modifiedby; }
        public String getModifiedon() { return modifiedon; }
    }

}
