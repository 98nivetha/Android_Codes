package com.zebra.demo.data.remote;
import com.zebra.demo.data.remote.model.Login;
import com.zebra.demo.data.remote.model.LoginRequest;
import com.zebra.demo.data.remote.requestmodels.PickListRequest;
import com.zebra.demo.data.remote.requestmodels.PutAwayScanningRequest;
import com.zebra.demo.data.remote.requestmodels.ReturnPartsValidInfoRequest;
import com.zebra.demo.data.remote.requestmodels.StockInwardRequest;
import com.zebra.demo.data.remote.responsemodels.GetAllMaterialInfoMainResponse;
import com.zebra.demo.data.remote.responsemodels.GetAllRackDatasResponse;
import com.zebra.demo.data.remote.responsemodels.GetSingleMaterialDataResponse;
import com.zebra.demo.data.remote.responsemodels.PickListInsertUpdateResponse;
import com.zebra.demo.data.remote.responsemodels.PickListResponse;
import com.zebra.demo.data.remote.responsemodels.PutAwayInsertResponse;
import com.zebra.demo.data.remote.responsemodels.PutAwayScanningResponse;
import com.zebra.demo.data.remote.responsemodels.ReturnDefectiveTypeResponse;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsInsertUpdateResponse;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsValidInfoResponse;
import com.zebra.demo.data.remote.responsemodels.RfidMappingResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


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

    @POST("/IBA_IVM_AND_RETURN_Insert_Update")
    Call<ReturnPartsInsertUpdateResponse> ReallocateInsertUpdate(@Body RequestBody requestBody);

    @POST("/IBA_IVM_AND_RETURN_GetAll")
    Call<ReturnDefectiveTypeResponse> ReallocateDefectiveParts(@Body RequestBody returnId);


    @POST("/IBA_IVM_AND_PUT_AWAY_PutAwayScanning")
    Call<PutAwayScanningResponse> putAwayScanning (@Body PutAwayScanningRequest request);

    @POST("/IBA_IVM_AND_PUT_AWAY_Insert_Update")
    Call<PutAwayInsertResponse> addPutAwayDatas (@Body StockInwardRequest request);

    @POST("/IBA_IVM_AND_PICKLIST_PickListScanning")
    Call<PickListResponse> pickListScanning (@Body PickListRequest request);

    @POST("/IBA_IVM_AND_PICKLIST_Insert_Update")
    Call<PickListInsertUpdateResponse> pickListInsertAndUpdate(@Body RequestBody request);

    //IBA PROJECT END//
}
