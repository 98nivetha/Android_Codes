package com.zebra.demo.data.remote.repository.IBA_REPOSITORY;

import android.util.Log;

import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.responsemodels.GetAllMaterialInfoMainResponse;
import com.zebra.demo.data.remote.responsemodels.GetSingleMaterialDataResponse;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaterialViewRepository extends BaseApiService {
    private static MaterialViewRepository instance;

    public static MaterialViewRepository getInstance() {
        if (instance == null) {
            instance = new MaterialViewRepository();
        }
        return instance;
    }

    public void getMaterialInfo(String s, ResponseListener<GetSingleMaterialDataResponse> responseListener) {
        String Rfid = "E280699500005011F1F554B2";
        String rawJson = "{ \"Rfid\": \"" + Rfid + "\" }";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), rawJson);
        Log.d("TEST", "GET SINGLE: " + rawJson);
        Call<GetSingleMaterialDataResponse> call = getApiService().getSingleMaterial(requestBody);
        call.enqueue(new Callback<GetSingleMaterialDataResponse>() {
            @Override
            public void onResponse(Call<GetSingleMaterialDataResponse> call, Response<GetSingleMaterialDataResponse> response) {
                if (response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }
            @Override
            public void onFailure(Call<GetSingleMaterialDataResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }
}
