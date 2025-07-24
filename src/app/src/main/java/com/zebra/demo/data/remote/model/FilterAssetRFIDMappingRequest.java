package com.zebra.demo.data.remote.model;

public class FilterAssetRFIDMappingRequest {
    public String SubAssetcode;
    public String Companycode;
    public String Buildingcode;
    public String Floorcode;
    public String Locationcode;
    public String Assetclass;
    public String Assetcode;
    public String Flag;


    public int Tagstatus;
}


//{ "SubAssetcode":"1000000160-03","Tagstatus":"0"} For Filtering the all details based on the part code
//{  "Companycode": "cec08d11b8d64" } Dependency dropdown payload for getting building detail
//{"Buildingcode": "2103083128ed4"} Dependency dropdown payload for getting Floor detail
//{ "Floorcode": "4355970503b04","Buildingcode": "2103083128ed4"} Dependency dropdown payload for getting location detail//{"Locationcode": "4dd789f037a64"} Dependency dropdown payload for getting Asset class detail
//{"Assetclass": "OFF F&F", "Locationcode": "4dd789f037a64"} Dependency dropdown payload for getting Asset code detail
//{"Assetcode": "1000000160", "Locationcode": "4dd789f037a64"} Dependency dropdown payload for getting subasset code detail
//{"SubAssetcode": "1000000160-02", "Locationcode": "4dd789f037a64", "Flag": "RFIDMapping"} For getting asset details