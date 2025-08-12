package com.zebra.demo.data.remote.repository.IBA_REPOSITORY;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.LocalDateTimeAdapter;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.requestmodels.ReturnPartsValidInfoRequest;
import com.zebra.demo.data.remote.responsemodels.ReturnDefectiveTypeResponse;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsInsertUpdateResponse;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsValidInfoResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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

    public void MetaDetailsForDefecriveParts(Integer s, ResponseListener<ReturnDefectiveTypeResponse> responseListener) {
        Integer returnId = s;
        String rawJson = "{ \"Returnid\": 0 }";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), rawJson);
        Call<ReturnDefectiveTypeResponse> call = getApiService().ReallocateDefectiveParts(requestBody);
        call.enqueue(new Callback<ReturnDefectiveTypeResponse>() {
            @Override
            public void onResponse(Call<ReturnDefectiveTypeResponse> call, Response<ReturnDefectiveTypeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("MetaDetailsForDefecriveParts", "isSuccessful");
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    Log.e("MetaDetailsForDefecriveParts", "Response:\n" + gson.toJson(response.body()));
                    responseListener.onDataReceived(response.body());
                } else {
                    Log.e("MetaDetailsForDefecriveParts", "Request failed but got response");
                    responseListener.onDataReceived(null);
                }
            }

            @Override
            public void onFailure(Call<ReturnDefectiveTypeResponse> call, Throwable t) {
                Log.e("MetaDetailsForDefecriveParts", "Network error: " + t.getMessage());
                responseListener.onFailure(t);
            }
        });
    }

    public void returnScan(ReturnPartsValidInfoRequest request, ResponseListener<ReturnPartsValidInfoResponse> responseListener) {
        Call<ReturnPartsValidInfoResponse> call = getApiService().checkValidParts(request);
        call.enqueue(new Callback<ReturnPartsValidInfoResponse>() {
            @Override
            public void onResponse(Call<ReturnPartsValidInfoResponse> call, Response<ReturnPartsValidInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseListener.onDataReceived(response.body());
                } else {
                    if (response.errorBody() != null) {
                        Log.e("returnScan_ErrorBody", "No error body present");
                    } else {
                        Log.e("returnScan_ErrorBody", "No error body present");
                    }
                    responseListener.onDataReceived(null);
                }
            }

            @Override
            public void onFailure(Call<ReturnPartsValidInfoResponse> call, Throwable t) {
                Log.e("returnScan_API_Failure", "Error: " + t.getMessage(), t);
                responseListener.onDataReceived(null);
            }
        });
    }

    public void insertUpdateRetunStock(Map<String, Object> requestMap, ResponseListener<ReturnPartsInsertUpdateResponse> responseListener) {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))).setPrettyPrinting().create();
            String json = gson.toJson(requestMap);
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(json, mediaType);
            Call<ReturnPartsInsertUpdateResponse> call = getApiService().ReallocateInsertUpdate(requestBody);
            if (call == null) {
                Log.e("API", "Retrofit call object is NULL. Check getApiService().ReallocateInsertUpdate().");
                return;
            }
            call.enqueue(new Callback<ReturnPartsInsertUpdateResponse>() {
                @Override
                public void onResponse(Call<ReturnPartsInsertUpdateResponse> call, Response<ReturnPartsInsertUpdateResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().isResult()) {
                                Log.d("API", "Insert/Update SUCCESS");
                                responseListener.onDataReceived(response.body());
                            } else {
                                Log.e("API", "Insert/Update FAILED: result=false");
                                responseListener.onFailure(new Exception("Operation failed: result=false"));
                            }
                        } else {
                            Log.e("API", "Response body is NULL despite HTTP 200");
                            responseListener.onFailure(new Exception("Empty response body"));
                        }
                    } else {
                        try {
                            String errorStr = (response.errorBody() != null) ? response.errorBody().string() : "null";
                            responseListener.onFailure(new Exception("HTTP " + response.code() + ": " + errorStr));
                        } catch (IOException e) {
                            responseListener.onFailure(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReturnPartsInsertUpdateResponse> call, Throwable t) {
                    Log.e("API", "Network Failure: " + t.getMessage(), t);
                    responseListener.onFailure(t);
                }
            });

        } catch (Exception e) {
            Log.e("API", "Exception in insertUpdateRetunStock()", e);
            responseListener.onFailure(e);
        }
    }
}
