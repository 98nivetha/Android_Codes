package com.zebra.demo.data.remote.repository.IBA_REPOSITORY;
import android.util.Log;

import com.google.gson.Gson;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.requestmodels.ReturnPartsValidInfoRequest;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsValidInfoResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReAllocateRepository extends BaseApiService {
    private static ReAllocateRepository instance;

    public static ReAllocateRepository getInstance() {
        if (instance == null) {
            instance = new ReAllocateRepository();
        }
        return instance;
    }

    public void getAllPartsByRFID(ReturnPartsValidInfoRequest request, ResponseListener<ReturnPartsValidInfoResponse> responseListener) {
        Call<ReturnPartsValidInfoResponse> call = getApiService().checkValidParts(request);
        call.enqueue(new Callback<ReturnPartsValidInfoResponse>() {
            @Override
            public void onResponse(Call<ReturnPartsValidInfoResponse> call, Response<ReturnPartsValidInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("PutAway_API_Response", "Success: " + new Gson().toJson(response.body()));
                    responseListener.onDataReceived(response.body());
                } else {
                    Log.e("PutAway_API_NullBody", "Response code: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("PutAway_API_ErrorBody", response.errorBody().string());
                        } catch (IOException e) {
                            Log.e("PutAway_API_Exception", "Error reading errorBody", e);
                        }
                    } else {
                        Log.e("PutAway_API_ErrorBody", "No error body present");
                    }
                    responseListener.onDataReceived(null);
                }
            }

            @Override
            public void onFailure(Call<ReturnPartsValidInfoResponse> call, Throwable t) {
                Log.e("PutAway_API_Failure", "Error: " + t.getMessage(), t);
                responseListener.onDataReceived(null);
            }
        });
    }
}
