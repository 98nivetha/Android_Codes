package com.zebra.demo.data.remote.repository;

import com.zebra.demo.BuildConfig;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.AssetInward;
import com.zebra.demo.data.remote.model.AssetInwardAutoRequest;
import com.zebra.demo.data.remote.model.AssetInwardAutoResponse;
import com.zebra.demo.data.remote.model.AssetInwardManualRequest;
import com.zebra.demo.data.remote.model.AssetInwardManualResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetInwardRepository extends BaseApiService {
    private static AssetInwardRepository assetRepository;

    public static AssetInwardRepository getInstance() {
        if (assetRepository == null) {
            assetRepository = new AssetInwardRepository();
        }
        return assetRepository;
    }
    public void fetchAssetInwardManualGatePassList(String Fromdate, String Todate, String Search, ResponseListener<List<AssetInward>> responseListener) {

        Map<String, String> queryMapping = new HashMap<>();

        if(Fromdate != null && !Fromdate.trim().isEmpty()) {
            queryMapping.put("Fromdate", Fromdate);
        }

        if(Todate != null && !Todate.trim().isEmpty()) {
            queryMapping.put("Todate", Todate);
        }
        if(Search != null && !Search.trim().isEmpty()) {
            queryMapping.put("Search", Search);
        }

        Call<List<AssetInward>> call = getApiService().fetchGatePassDetailsAssetInwardManual("BuildConfig.AUTHORIZATION",queryMapping);
        call.enqueue(new Callback<List<AssetInward>>() {
            @Override
            public void onResponse(Call<List<AssetInward>> call, Response<List<AssetInward>> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }
            @Override
            public void onFailure(Call<List<AssetInward>> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }

    public void fetchAssetInwardAssetDetailsViaGatePass(String gatepassno, ResponseListener<List<AssetInwardAutoResponse>> responseListener) {
        Call<List<AssetInwardAutoResponse>> call = getApiService().fetchAssetInwardAssetDetailsViaGatePass("BuildConfig.AUTHORIZATION",gatepassno);
        call.enqueue(new Callback<List<AssetInwardAutoResponse>>() {
            @Override
            public void onResponse(Call<List<AssetInwardAutoResponse>> call, Response<List<AssetInwardAutoResponse>> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }
            @Override
            public void onFailure(Call<List<AssetInwardAutoResponse>> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }

    public void InsertAssetInwardAuto(List<AssetInwardAutoRequest> assetInwardAutoRequest, ResponseListener<List<AssetInwardAutoResponse>> responseListener) {
        Call<List<AssetInwardAutoResponse>> call = getApiService().InsertAssetInwardAuto("BuildConfig.AUTHORIZATION", assetInwardAutoRequest);
        call.enqueue(new Callback<List<AssetInwardAutoResponse>>() {
            @Override
            public void onResponse(Call<List<AssetInwardAutoResponse>> call, Response<List<AssetInwardAutoResponse>> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }
            @Override
            public void onFailure(Call<List<AssetInwardAutoResponse>> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }

    public void InsertAssetInwardManual(AssetInwardManualRequest assetInwardManualRequest, ResponseListener<AssetInwardManualResponse> responseListener) {
        Call<AssetInwardManualResponse> call = getApiService().InsertAssetInwardManual("BuildConfig.AUTHORIZATION", assetInwardManualRequest);
        call.enqueue(new Callback<AssetInwardManualResponse>() {
            @Override
            public void onResponse(Call<AssetInwardManualResponse> call, Response<AssetInwardManualResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }
            @Override
            public void onFailure(Call<AssetInwardManualResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }
}
