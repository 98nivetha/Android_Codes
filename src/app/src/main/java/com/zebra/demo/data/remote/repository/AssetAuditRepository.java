package com.zebra.demo.data.remote.repository;

import com.zebra.demo.BuildConfig;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.AssetAudit;
import com.zebra.demo.data.remote.model.AssetAuditDetail;
import com.zebra.demo.data.remote.model.AssetAuditRequest;
import com.zebra.demo.data.remote.model.AssetAuditResponse;
import com.zebra.demo.data.remote.model.AssetDetail;
import com.zebra.demo.data.remote.model.FilterAssetRFIDMappingResponse;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class AssetAuditRepository extends BaseApiService {
    private static AssetAuditRepository assetRepository;

    public static AssetAuditRepository getInstance() {
        if (assetRepository == null) {
            assetRepository = new AssetAuditRepository();
        }
        return assetRepository;
    }


    //Fetch Asset In-Progress
    public void fetchAssetAuditInProgressList(ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {
        String rawJson = "{ \"Processstage\": \"Inprocess\"}";
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

    //Fetch Asset Completed
    public void fetchAssetAuditCompletedList(ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {
        String rawJson = "{ \"Processstage\": \"Completed\"}";
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

    //Fetch Asset UpComing
    public void fetchAssetAuditUpcomingList(ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {
        String rawJson = "{ \"Processstage\": \"Upcoming\"}";
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

    //Fetch Audit Asset Object By Asset Code
    public void fetchAuditIdDetails(String AuditCode, ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {

        String rawJson = "{ \"Auditcode\": \""+ AuditCode +"\"}";
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

    //Insert Scanned Audit Asset
    public void insertAuditScanData(List<AssetAuditRequest> assetAuditRequest, ResponseListener<AssetAuditResponse> responseListener) {
        Call<AssetAuditResponse> call = getApiService().insertAuditScanData(assetAuditRequest);
        call.enqueue(new Callback<AssetAuditResponse>() {
            @Override
            public void onResponse(Call<AssetAuditResponse> call, Response<AssetAuditResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<AssetAuditResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }
}







/*
public void fetchAssetAuditInProgressList(ResponseListener<List<AssetAudit>> responseListener) {
        Call<List<AssetAudit>> call = getApiService().fetchAssetAuditInProgressList("BuildConfig.AUTHORIZATION");
        call.enqueue(new Callback<List<AssetAudit>>() {
            @Override
            public void onResponse(Call<List<AssetAudit>> call, Response<List<AssetAudit>> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<AssetAudit>> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }

    public void fetchAssetAuditCompletedList(ResponseListener<List<AssetAudit>> responseListener) {
        Call<List<AssetAudit>> call = getApiService().fetchAssetAuditCompletedList("BuildConfig.AUTHORIZATION");
        call.enqueue(new Callback<List<AssetAudit>>() {
            @Override
            public void onResponse(Call<List<AssetAudit>> call, Response<List<AssetAudit>> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<AssetAudit>> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }

    public void fetchAssetAuditUpcomingList(ResponseListener<List<AssetAudit>> responseListener) {
        Call<List<AssetAudit>> call = getApiService().fetchAssetAuditUpcomingList("BuildConfig.AUTHORIZATION");
        call.enqueue(new Callback<List<AssetAudit>>() {
            @Override
            public void onResponse(Call<List<AssetAudit>> call, Response<List<AssetAudit>> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<AssetAudit>> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }

    public void fetchAuditIdDetails(String Auditid, ResponseListener<AssetAuditDetail> responseListener) {
        Call<AssetAuditDetail> call = getApiService().fetchAuditIdDetails("BuildConfig.AUTHORIZATION", Auditid);
        call.enqueue(new Callback<AssetAuditDetail>() {
            @Override
            public void onResponse(Call<AssetAuditDetail> call, Response<AssetAuditDetail> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<AssetAuditDetail> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }
 */