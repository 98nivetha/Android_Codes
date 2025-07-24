package com.zebra.demo.data.remote.repository;

import com.zebra.demo.BuildConfig;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.ApiResponse;
import com.zebra.demo.data.remote.model.AssetOutward;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetOutwardRepository extends BaseApiService {
    private static AssetOutwardRepository assetRepository;

    public static AssetOutwardRepository getInstance() {
        if (assetRepository == null) {
            assetRepository = new AssetOutwardRepository();
        }
        return assetRepository;
    }

    public void fetchAssetOutwardGatePassDetailsList(String Gatepassno, ResponseListener<List<AssetOutward>> responseListener) {
        Call<List<AssetOutward>> call = getApiService().fetchAssetOutwardGatePassDetailsList("BuildConfig.AUTHORIZATION",Gatepassno);
        call.enqueue(new Callback<List<AssetOutward>>() {
            @Override
            public void onResponse(Call<List<AssetOutward>> call, Response<List<AssetOutward>> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<AssetOutward>> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    public void insertAssetOutwardDetails(String Gatepassno, List<AssetOutward> assetOutwardRequest, ResponseListener<List<ApiResponse>> responseListener) {
        Call<List<ApiResponse>> call = getApiService().insertAssetOutwardDetails("BuildConfig.AUTHORIZATION",Gatepassno,assetOutwardRequest);
        call.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }
}
