package com.zebra.demo.data.remote.repository.IBA_REPOSITORY;
import android.util.Log;

import com.google.gson.Gson;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.requestmodels.PutAwayScanningRequest;
import com.zebra.demo.data.remote.requestmodels.StockInwardRequest;
import com.zebra.demo.data.remote.responsemodels.DisplayItem;
import com.zebra.demo.data.remote.responsemodels.GetAllRackDatasResponse;
import com.zebra.demo.data.remote.responsemodels.PutAwayInsertResponse;
import com.zebra.demo.data.remote.responsemodels.PutAwayScanningResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  PutAwayRepository extends BaseApiService {

    private static PutAwayRepository instance;

    public static PutAwayRepository getInstance() {
        if (instance == null) {
            instance = new PutAwayRepository();
        }
        return instance;
    }

    // In PutAwayRepository.java
    public void scanPutAwayPockets(PutAwayScanningRequest request, ResponseListener<List<DisplayItem>> responseListener) {
        Call<PutAwayScanningResponse> call = getApiService().putAwayScanning(request);
        call.enqueue(new Callback<PutAwayScanningResponse>() {
            @Override
            public void onResponse(Call<PutAwayScanningResponse> call, Response<PutAwayScanningResponse> response) {
                PutAwayScanningResponse responseBody = response.body();
                if (responseBody != null && responseBody.getData().getTotalScanned() != 0) {
                    List<DisplayItem> displayItems = new ArrayList<>();

                    for (PutAwayScanningResponse.MaterialItem item : responseBody.getData().getValid()) {
                        displayItems.add(new DisplayItem(item, true));
                    }

                    for (PutAwayScanningResponse.MaterialItem item : responseBody.getData().getInvalid()) {
                        displayItems.add(new DisplayItem(item, false));
                    }

                    responseListener.onDataReceived(displayItems);
                } else {
                    responseListener.onError("Empty or null data");
                }
            }

            @Override
            public void onFailure(Call<PutAwayScanningResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }


    public void getRacks(String s, ResponseListener<GetAllRackDatasResponse> responseListener) {
        String qrCode = s;
        String rawJson = "{ \"Qrcode\": \"" + qrCode + "\" }";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), rawJson);
        Call<GetAllRackDatasResponse> call = getApiService().getAllRacks(requestBody);
        call.enqueue(new Callback<GetAllRackDatasResponse>() {
            @Override
            public void onResponse(Call<GetAllRackDatasResponse> call, Response<GetAllRackDatasResponse> response) {
                if (response.code() == 200) {
                    Log.d("REPO TEST", String.valueOf(response.body()));
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            public void onFailure(Call<GetAllRackDatasResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }

    public void insertPutAway(StockInwardRequest request, ResponseListener<PutAwayInsertResponse> responseListener) {
        // Log the actual request content
        Log.e("PutAway API Request", new Gson().toJson(request));

        Call<PutAwayInsertResponse> call = getApiService().addPutAwayDatas(request);
        call.enqueue(new Callback<PutAwayInsertResponse>() {
            @Override
            public void onResponse(Call<PutAwayInsertResponse> call, Response<PutAwayInsertResponse> response) {
                if (response != null && response.isSuccessful()) {
                    responseListener.onDataReceived(response.body());
                    Log.d("PutAway_API_Success", "Response: " + new Gson().toJson(response.body()));
                } else {
                    Log.e("PutAway_API_Error", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PutAwayInsertResponse> call, Throwable t) {
                Log.e("PutAway_API_Failure", "Error: " + t.getMessage(), t);
                responseListener.onFailure(t);
            }
        });
    }


}
