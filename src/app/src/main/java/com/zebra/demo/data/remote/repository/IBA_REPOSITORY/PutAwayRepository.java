package com.zebra.demo.data.remote.repository.IBA_REPOSITORY;
import android.util.Log;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.responsemodels.GetAllRackDatasResponse;
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
}
