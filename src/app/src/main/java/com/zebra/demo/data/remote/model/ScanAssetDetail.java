package com.zebra.demo.data.remote.model;

public class ScanAssetDetail {


     private String Scanid;
     private String Tagid;
     private String AssetCode;
     private String AssetName;
     private String PLACEOF_ASSET;
     private String PLACEOF_ASSET_NAME;
     private int EMP_AST_STATUS;
     private String Remarks;
     private String TagIDS;

    public ScanAssetDetail() {

    }

    public String getScanid() {
        return Scanid;
    }

    public void setScanid(String scanid) {
        Scanid = scanid;
    }

    public String getTagid() {
        return Tagid;
    }

    public void setTagid(String tagid) {
        Tagid = tagid;
    }

    public String getAssetCode() {
        return AssetCode;
    }

    public void setAssetCode(String assetCode) {
        AssetCode = assetCode;
    }

    public String getAssetName() {
        return AssetName;
    }

    public void setAssetName(String assetName) {
        AssetName = assetName;
    }

    public String getPLACEOF_ASSET() {
        return PLACEOF_ASSET;
    }

    public void setPLACEOF_ASSET(String PLACEOF_ASSET) {
        this.PLACEOF_ASSET = PLACEOF_ASSET;
    }

    public String getPLACEOF_ASSET_NAME() {
        return PLACEOF_ASSET_NAME;
    }

    public void setPLACEOF_ASSET_NAME(String PLACEOF_ASSET_NAME) {
        this.PLACEOF_ASSET_NAME = PLACEOF_ASSET_NAME;
    }

    public int getEMP_AST_STATUS() {
        return EMP_AST_STATUS;
    }

    public void setEMP_AST_STATUS(int EMP_AST_STATUS) {
        this.EMP_AST_STATUS = EMP_AST_STATUS;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getTagIDS() {
        return TagIDS;
    }

    public void setTagIDS(String tagIDS) {
        TagIDS = tagIDS;
    }
}
