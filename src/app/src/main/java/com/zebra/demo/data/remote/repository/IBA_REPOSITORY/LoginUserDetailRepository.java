package com.zebra.demo.data.remote.repository.IBA_REPOSITORY;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.Login;
import com.zebra.demo.data.remote.model.LoginRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginUserDetailRepository extends BaseApiService {
    public void loginRequest(LoginRequest loginRequest, ResponseListener<Login> responseListener) {
        Call<Login> call = getApiService().login(loginRequest);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }
}
