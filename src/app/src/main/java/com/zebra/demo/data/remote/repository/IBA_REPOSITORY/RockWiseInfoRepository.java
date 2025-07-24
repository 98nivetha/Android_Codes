package com.zebra.demo.data.remote.repository.IBA_REPOSITORY;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.responsemodels.GetAllMaterialInfoMainResponse;
import com.zebra.demo.data.remote.responsemodels.GetAllRackDatasResponse;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RockWiseInfoRepository extends BaseApiService {
    public static RockWiseInfoRepository instance;

    public static RockWiseInfoRepository getInstance() {
        if (instance == null) {
            instance = new RockWiseInfoRepository();
        }
        return instance;
    }


    public void getAllRocks(String s, ResponseListener<GetAllRackDatasResponse> responseListener) {
        String rawJson = "{ \"Qrcode\": \"\" }";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), rawJson);
        Call<GetAllRackDatasResponse> call = getApiService().getAllRacks(requestBody);
        call.enqueue(new Callback<GetAllRackDatasResponse>() {
            @Override
            public void onResponse(Call<GetAllRackDatasResponse> call, Response<GetAllRackDatasResponse> response) {
                if (response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<GetAllRackDatasResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }

    public void getAllMaterialDatas(String s, ResponseListener<GetAllMaterialInfoMainResponse> responseListener) {
        String qrCode = s;
        String rawJson = "{ \"Qrcode\": \"" + qrCode + "\" }";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), rawJson);
        Call<GetAllMaterialInfoMainResponse> call = getApiService().getAllMaterialBasedOnRacks(requestBody);
        call.enqueue(new Callback<GetAllMaterialInfoMainResponse>() {
            @Override
            public void onResponse(Call<GetAllMaterialInfoMainResponse> call, Response<GetAllMaterialInfoMainResponse> response) {
                if (response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }
            @Override
            public void onFailure(Call<GetAllMaterialInfoMainResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }
}
