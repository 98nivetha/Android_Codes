package com.zebra.demo.data.remote.repository;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.AssetDetail;
import com.zebra.demo.data.remote.model.AssetViewRequest;
import com.zebra.demo.data.remote.model.FilterAssetRFIDMappingResponse;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetRepository extends BaseApiService {
    private static AssetRepository assetRepository;

    public static AssetRepository getInstance() {
        if (assetRepository == null) {
            assetRepository = new AssetRepository();
        }
        return assetRepository;
    }

    public void fetchAssetDetails(AssetViewRequest assetViewRequest, ResponseListener<List<AssetDetail>> responseListener) {
        Call<List<AssetDetail>> call = getApiService().fetchAssetDetails("BuildConfig.AUTHORIZATION",assetViewRequest);
        call.enqueue(new Callback<List<AssetDetail>>() {
            @Override
            public void onResponse(Call<List<AssetDetail>> call, Response<List<AssetDetail>> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<AssetDetail>> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    public void fetchAssetFilterDetails(AssetViewRequest assetViewRequest, ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {

        String rawJson = "{ \"RFIDTag\": \""+ assetViewRequest.getRFIDTag() +"\"}";

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                rawJson
        );

        Call<FilterAssetRFIDMappingResponse> call = getApiService().filterAssetRFIDMapping(requestBody);
        call.enqueue(new Callback<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<FilterAssetRFIDMappingResponse> call, Response<FilterAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<FilterAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    /*
    public void fetchAssetFilterDetails(AssetViewRequest assetViewRequest, ResponseListener<AssetFilterResponse> responseListener) {
        Call<AssetFilterResponse> call = getApiService().fetchAssetFilterDetails(assetViewRequest);
        call.enqueue(new Callback<AssetFilterResponse>() {
            @Override
            public void onResponse(Call<AssetFilterResponse> call, Response<AssetFilterResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<AssetFilterResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }
     */

    public void fetchMultipleAssetdetailsviaRFID(String tagIds, ResponseListener<List<AssetDetail>> responseListener) {
        Call<List<AssetDetail>> call = getApiService().fetchMultipleAssetdetailsviaRFID("BuildConfig.AUTHORIZATION",tagIds);
        call.enqueue(new Callback<List<AssetDetail>>() {
            @Override
            public void onResponse(Call<List<AssetDetail>> call, Response<List<AssetDetail>> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<AssetDetail>> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }
}
