package com.zebra.demo.data.remote.model;

import java.util.List;

public class FilterAssetRFIDMappingData {
    //Get all & Filter
    public List<DBuildingDetail> D_BuildingDetails;
    public List<DFloorDetail> D_FloorDetails;
    public List<DLocationDetail> D_LocationDetails;
    public List<DAssetclassDetail> D_AssetClassDetails;

    //Get All Response
    public List<DCompanyDetail> D_CompanyDetails;
    public List<DMetaDetail> D_MetaDetails;
    public List<DDepartmentDetail> D_DepartmentDetails;
    public List<DCostcenterDetail> D_CostcenterDetails;
    public List<DMakeDetail> D_MakeDetails;
    public List<DAssetDetail> D_AssetResgiterDetails;
    public List<DSubAssetDetail> D_AssetSubcodeDetails;
    public List<DSubAssetPartDetail> D_AssetSubcodepartsDetails;
    public List<VAssetRFIDmappingDetail> V_AssetRFIDmappingDetails;
    public List<DAssetclassDetail> D_AssetclassDetails;

    //Filter
    public List<DAssetDetail> D_AssetDetails;
    public List<DSubAssetDetail> D_SubAssetDetails;
    public List<DSubAssetPartDetail> D_SubAssetPartDetails;

    //All Dropdown Values
    public List<DSubPartDetails> D_SubPartDetails;

    //Assert View
    public List<DAssetResgiterDetail> D_RFIDDetails;

    //Audit
    public List<AssetAudit> D_UpcomingDetails;
    public List<AssetAudit> D_InprocessDetails;
    public List<AssetAudit> D_CompletedDetails;
    public List<AssetAudit> D_AuditDetails;

}