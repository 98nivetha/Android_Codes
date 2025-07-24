package com.zebra.demo.data.remote.repository.IBA_REPOSITORY;
import android.util.Log;
import com.zebra.demo.data.remote.ApiService;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.responsemodels.RfidMappingResponse;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RfidMappingRepository extends BaseApiService {
    private static RfidMappingRepository instance;

    public static RfidMappingRepository getInstance() {
        if (instance == null) {
            instance = new RfidMappingRepository();
        }
        return instance;
    }

    public void fetchInwardDetails(int inwardId, ResponseListener<RfidMappingResponse> responseListener) {
        ApiService apiService = getApiService();

        if (apiService == null) {
            Log.e("API", "ApiService is null!");
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("Rfidid", inwardId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString());
        Call<RfidMappingResponse> call = apiService.getInwardDetails(requestBody);

        call.enqueue(new Callback<RfidMappingResponse>() {
            @Override
            public void onResponse(Call<RfidMappingResponse> call, Response<RfidMappingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseListener.onDataReceived(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e("API_RESPONSE", "Error body: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<RfidMappingResponse> call, Throwable t) {
                Log.e("API_RESPONSE", "onFailure", t);
                responseListener.onFailure(t);
            }
        });
    }
}
