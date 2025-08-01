package com.zebra.demo.data.remote;
import com.zebra.demo.data.remote.model.ApiResponse;
import com.zebra.demo.data.remote.model.AssetAuditRequest;
import com.zebra.demo.data.remote.model.AssetAuditResponse;
import com.zebra.demo.data.remote.model.AssetDetail;
import com.zebra.demo.data.remote.model.AssetInward;
import com.zebra.demo.data.remote.model.AssetInwardAutoRequest;
import com.zebra.demo.data.remote.model.AssetInwardAutoResponse;
import com.zebra.demo.data.remote.model.AssetInwardManualRequest;
import com.zebra.demo.data.remote.model.AssetInwardManualResponse;
import com.zebra.demo.data.remote.model.AssetOutward;
import com.zebra.demo.data.remote.model.AssetViewRequest;
import com.zebra.demo.data.remote.model.EmployeeAssetScannedInsertRequest;
import com.zebra.demo.data.remote.model.EmployeeDetail;
import com.zebra.demo.data.remote.model.EmployeeVerifyAssetFetchRequest;
import com.zebra.demo.data.remote.model.EmployeeVerifyAssetFetchResponse;
import com.zebra.demo.data.remote.model.EmployeesAllocatedAssetDetailRequest;
import com.zebra.demo.data.remote.model.EmployeesAllocatedAssetDetailsResponse;
import com.zebra.demo.data.remote.model.FilterAssetNameRequest;
import com.zebra.demo.data.remote.model.FilterAssetRFIDMappingResponse;
import com.zebra.demo.data.remote.model.GetAllAssetRFIDMappingRequest;
import com.zebra.demo.data.remote.model.InsertAssetRFIDMappingRequest;
import com.zebra.demo.data.remote.model.InsertAssetRFIDMappingResponse;
import com.zebra.demo.data.remote.model.Login;
import com.zebra.demo.data.remote.model.LoginRequest;
import com.zebra.demo.data.remote.model.MetaDetail;
import com.zebra.demo.data.remote.model.UpdateAssetRFIDMappingRequest;
import com.zebra.demo.data.remote.model.UpdateAssetRFIDMappingResponse;
import com.zebra.demo.data.remote.requestmodels.PutAwayScanningRequest;
import com.zebra.demo.data.remote.requestmodels.ReturnPartsValidInfoRequest;
//import com.zebra.demo.data.remote.requestmodels.StockInwardRequest;
import com.zebra.demo.data.remote.requestmodels.StockInwardRequest;
import com.zebra.demo.data.remote.responsemodels.GetAllMaterialInfoMainResponse;
import com.zebra.demo.data.remote.responsemodels.GetAllRackDatasResponse;
import com.zebra.demo.data.remote.responsemodels.GetSingleMaterialDataResponse;
import com.zebra.demo.data.remote.responsemodels.PutAwayInsertResponse;
import com.zebra.demo.data.remote.responsemodels.PutAwayScanningResponse;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsValidInfoResponse;
import com.zebra.demo.data.remote.responsemodels.RfidMappingResponse;
import java.util.List;
import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {
    //IBA PROJECT START//

    @POST("/IBA_IVM_AND_RFID_GetAll")
    Call<RfidMappingResponse> getInwardDetails(@Body RequestBody requestBody);

    @POST("/login")
    Call<Login> login(@Body LoginRequest loginRequest);

    @POST("/IBA_IVM_AND_PUT_AWAY_FetchData")
    Call<GetAllRackDatasResponse> getAllRacks(@Body RequestBody requestBody);

    @POST("/IBA_IVM_AND_FIND_RACKINFO_FetchData")
    Call<GetAllMaterialInfoMainResponse> getAllMaterialBasedOnRacks (@Body RequestBody requestBody);

    @POST("/IBA_IVM_AND_FIND_MATERIALINFO_GetAll")
    Call<GetSingleMaterialDataResponse> getSingleMaterial (@Body RequestBody requestBody);

    @POST("/IBA_IVM_AND_RETURN_StockreturnScanning")
    Call<ReturnPartsValidInfoResponse> checkValidParts (@Body ReturnPartsValidInfoRequest request);

    @POST("/IBA_IVM_AND_PUT_AWAY_PutAwayScanning")
    Call<PutAwayScanningResponse> putAwayScanning (@Body PutAwayScanningRequest request);

    @POST("/IBA_IVM_AND_PUT_AWAY_Insert_Update")
    Call<PutAwayInsertResponse> addPutAwayDatas (@Body StockInwardRequest request);



    //IBA PROJECT END//

    //Get All Asset RFID Mapping
    @POST("/ASM_PFIZER_ASSET_RFIDMAPPING_ANDROID_GetAll")
    Call<FilterAssetRFIDMappingResponse> getAllAssetRFIDMapping(@Body GetAllAssetRFIDMappingRequest getAllAssetRFIDMappingRequest);

    //Filter Asset RFID Mapping
    //Asset Filter Detail (Asset View, Asset Search and Audit List)
    @POST("/ASM_PFIZER_ASSET_RFIDMAPPING_ANDROID_Filter")
    Call<FilterAssetRFIDMappingResponse> filterAssetRFIDMapping(@Body RequestBody requestBody);

    //Insert Asset RFID Mapping
    @POST("/ASM_PFIZER_ASSET_RFIDMAPPING_ANDROID_Insert")
    Call<InsertAssetRFIDMappingResponse> insertAssetRFIDMapping(@Body InsertAssetRFIDMappingRequest insertAssetRFIDMappingRequest);

    //Update Asset RFID Mapping
    @POST("/ASM_PFIZER_ASSET_RFIDMAPPING_ANDROID_Update")
    Call<UpdateAssetRFIDMappingResponse> updateAssetRFIDMapping(@Body UpdateAssetRFIDMappingRequest updateAssetRFIDMappingRequest);

    //Insert Audit Scan
    @POST("/ASM_PFIZER_ASSET_RFIDMAPPING_ANDROID_Scanning")
    Call<AssetAuditResponse> insertAuditScanData(@Body List<AssetAuditRequest> assetAuditRequest);


    /// Pass body
    //Asset Detail
    @POST(/*AppConstant.ASM_ANDROID_API + */"api/Assets/FetchAssetdetails")
    Call<List<AssetDetail>> fetchAssetDetails(@Header("Authorization") String authorization, @Body AssetViewRequest assetViewRequest);
    //Call<List<AssetDetail>>  fetchAssetDetails(@Header("Authorization") String authorization, @Query("RFIDTag") String tagid);

    @GET(/*AppConstant.ASM_ANDROID_API + */"api/Assets/FetchMultipleAssetdetailsviaRFID")
    Call<List<AssetDetail>> fetchMultipleAssetdetailsviaRFID(@Header("Authorization") String authorization, @Query("tagid") String tagids);


    //Audit
    /*@POST(*//*AppConstant.ASM_ANDROID_API + *//*"api/Assets/AuditUpcoming")
    Call<List<AssetAudit>> fetchAssetAuditUpcomingList(@Header("Authorization") String authorization);

    @POST(*//*AppConstant.ASM_ANDROID_API + *//*"api/Assets/AuditInProgress")
    Call<List<AssetAudit>> fetchAssetAuditInProgressList(@Header("Authorization") String authorization);

    @POST(*//*AppConstant.ASM_ANDROID_API + *//*"api/Assets/AuditCompleted")
    Call<List<AssetAudit>> fetchAssetAuditCompletedList(@Header("Authorization") String authorization);

    @POST(*//*AppConstant.ASM_ANDROID_API + *//*"api/Assets/FetchAuditIdDetails")
    Call<AssetAuditDetail> fetchAuditIdDetails(@Header("Authorization") String authorization, @Query("Auditid") String Auditid);

    @POST(*//*AppConstant.ASM_ANDROID_API + *//*"api/Assets/InsertAuditScanData")
    Call<AssetAuditResponse> insertAuditScanData(@Header("Authorization") String authorization, @Body List<AssetAuditRequest> assetAuditRequest);*/


    //Asset Outward
    @GET(/*AppConstant.ASM_ANDROID_API + */"api/Assets/GatePassDetails")
    Call<List<AssetOutward>> fetchAssetOutwardGatePassDetailsList(@Header("Authorization") String authorization, @Query("Gatepassno") String Gatepassno);

    @POST(/*AppConstant.ASM_ANDROID_API + */"api/Assets/InsertAssetOutwardDetails")
    Call<List<ApiResponse>> insertAssetOutwardDetails(@Header("Authorization") String authorization, @Query("Gatepassno") String Gatepassno, @Body List<AssetOutward> assetOutwardRequest);


    //Meta Details
    @POST(/*AppConstant.ASM_ANDROID_API + */"api/Assets/Fetchmetadetails")
    Call<MetaDetail> fetchMetaDetails(@Header("Authorization") String authorization, @Body FilterAssetNameRequest filterAssetNameRequest);

    // Asset Inward
    @GET(/*AppConstant.ASM_ANDROID_API + */"api/Assets/GatepassDetails_AssetInwardManual")
    Call<List<AssetInward>> fetchGatePassDetailsAssetInwardManual(@Header("Authorization") String authorization, @QueryMap Map<String, String> options);

    @GET(/*AppConstant.ASM_ANDROID_API + */"api/Assets/AssetInward_AssetDetailsviagatepass")
    Call<List<AssetInwardAutoResponse>> fetchAssetInwardAssetDetailsViaGatePass(@Header("Authorization") String authorization, @Query("gatepassno") String gatepassno);

    @POST(/*AppConstant.ASM_ANDROID_API + */"api/Assets/AssetInward_InsertAuto")
    Call<List<AssetInwardAutoResponse>> InsertAssetInwardAuto(@Header("Authorization") String authorization, @Body List<AssetInwardAutoRequest> assetInwardRequest);

    @POST(/*AppConstant.ASM_ANDROID_API + */"api/Assets/AssetInward_InsertManual")
    Call<AssetInwardManualResponse> InsertAssetInwardManual(@Header("Authorization") String authorization, @Body AssetInwardManualRequest assetInwardManualRequest);


    /*Employee Verification*/

    //Employee List
    @POST("api/Assets/EmployeeAssetVerificationInitial")
    Call<List<EmployeeDetail>> employeeAssetVerificationInitial(@Header("Authorization") String authorization);

    //Get Employee Detail with Assert
    @POST("api/Assets/EmployeesAllocatedAssetDetails")
    Call<EmployeesAllocatedAssetDetailsResponse> employeesAllocatedAssetDetails(@Header("Authorization") String authorization, @Body EmployeesAllocatedAssetDetailRequest employeesAllocatedAssetDetailRequest);

    //Get Assert Status
    @POST("api/Assets/EmployeeVerifyAssetFetch")
    Call<EmployeeVerifyAssetFetchResponse> employeeVerifyAssetFetch(@Header("Authorization") String authorization, @Body EmployeeVerifyAssetFetchRequest employeeVerifyAssetFetchRequest);

    //Employee Asset Scanned Insert
    @POST("api/Assets/EmployeeAssetScannedInsert")
    Call<ApiResponse> employeeAssetScannedInsert(@Header("Authorization") String authorization, @Body EmployeeAssetScannedInsertRequest employeeAssetScannedInsertRequest);


}
