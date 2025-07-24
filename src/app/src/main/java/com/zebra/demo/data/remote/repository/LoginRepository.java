package com.zebra.demo.data.remote.repository;
import com.zebra.demo.data.local.SharedPreference;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.Login;
import com.zebra.demo.data.remote.model.LoginRequest;
import com.zebra.demo.zebralib.application.Application;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository extends BaseApiService {

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


    public void saveUserDetails(Login login) {
        if(login != null && login.getData() != null && login.getData().getUserDetails() != null) {
            SharedPreference sharedPreference = new SharedPreference(Application.getAppContext());
            sharedPreference.setUserId(String.valueOf(login.getData().getUserDetails().getUsercode()));
            sharedPreference.setUserDeptName(login.getData().getUserDetails().getDepartmentcode());
            sharedPreference.setUserName(login.getData().getUserDetails().getUsername());
            sharedPreference.setToken(login.getData().getToken());
            sharedPreference.setUserLoginResp(login);

            sharedPreference.setLoggedIn(true);
        }
    }
}
