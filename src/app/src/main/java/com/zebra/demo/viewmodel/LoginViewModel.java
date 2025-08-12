package com.zebra.demo.viewmodel;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zebra.demo.data.remote.exception.ErrorResource;
import com.zebra.demo.data.remote.exception.ExceptionHandler;
import com.zebra.demo.data.remote.listener.ExceptionHandlerListener;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.Login;
import com.zebra.demo.data.remote.model.LoginRequest;
import com.zebra.demo.data.remote.repository.LoginRepository;
import com.zebra.demo.view.listener.ApiResponseListener;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> userIdValidationText = new MutableLiveData<>();
    public MutableLiveData<String> passwordValidationText = new MutableLiveData<>();

    public MutableLiveData<String> portValidationText = new MutableLiveData<>();
    public MutableLiveData<String> ipValidationText = new MutableLiveData<>();
    public MutableLiveData<String> protocolValidationText = new MutableLiveData<>();
    public MutableLiveData<String> domainNameValidationText = new MutableLiveData<>();

    public MutableLiveData<String> userId = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> networkErrorMutableData = new MutableLiveData<>();


    /**
     * Form Validation for username and password
     *
     * @param apiResponseListener
     * @return true if valid else false
     */

    private boolean formValidation(ApiResponseListener apiResponseListener, LoginRequest loginRequest) {

        portValidationText.setValue(null);
        ipValidationText.setValue(null);
        protocolValidationText.setValue(null);
        domainNameValidationText.setValue(null);

        userIdValidationText.setValue(null);
        passwordValidationText.setValue(null);


        //User Name
        if (TextUtils.isEmpty(loginRequest.getUsername())) {
            userIdValidationText.setValue("User Name cannot be empty");
            apiResponseListener.onFailed("User Name cannot be empty");
            return false;
        }

        //Password
        if (TextUtils.isEmpty(loginRequest.getPassword())) {
            passwordValidationText.setValue("Password cannot be empty");
            apiResponseListener.onFailed("Password cannot be empty");
            return false;
        }

        return true;
    }

    /**
     * Sending Login Request to API
     *
     * @param apiResponseListener
     */


    /**
     * Sending User Info Request to API
     *
     * @param apiResponseListener
     */


    /**
     * Sending Login Request to API
     *
     * @param apiResponseListener
     */

    public void sendLoginRequest(ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null) {
            return;
        }
        LoginRequest loginRequest = new LoginRequest();
        Log.d("LoginDebug", loginRequest.toString());
        loginRequest.setUsername(userId.getValue() == null ? "" : userId.getValue().trim());
        loginRequest.setPassword(password.getValue() == null ? "" : password.getValue().trim());
        Log.d("LoginDebug", loginRequest.toString());
        if (formValidation(apiResponseListener, loginRequest)) {

            isDataLoading.setValue(true);
            LoginRepository loginRepository = new LoginRepository();
            loginRepository.loginRequest(loginRequest, new ResponseListener<Login>() {
                @Override
                public void onDataReceived(Login login) {
                    if (login != null && login.getResult()) {
                        setUserPreference(login, apiResponseListener);
                    } else {
                        apiResponseListener.onFailed("Invalid Login");
                    }
                    isDataLoading.setValue(false);
                }

                @Override
                public void onError(String e) {
                    if (e != null) {
                        apiResponseListener.onFailed(e);
                        isDataLoading.setValue(false);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if (t != null) {
                        ExceptionHandler.handleLoginListenerException(apiResponseListener, t, getExceptionHandler());
                        isDataLoading.setValue(false);
                    }
                }
            });
        }
    }

    private void setUserPreference(Login login, ApiResponseListener apiResponseListener) {
        if (login != null && login.getData() != null /*&& !login.user.isEmpty()*/) {
            LoginRepository loginRepository = new LoginRepository();
            loginRepository.saveUserDetails(login);
            apiResponseListener.onSuccess(login);
        } else {
            apiResponseListener.onFailed("Login Failed");
        }
    }


    private ExceptionHandlerListener getExceptionHandler() {
        return new ExceptionHandlerListener() {
            @Override
            public void onInternetUnavailable() {
                networkErrorMutableData.setValue(true);
            }

            @Override
            public void onInvalidLogin(ErrorResource errorResource) {

            }

            @Override
            public void onForbidden(ErrorResource errorResource) {

            }

            @Override
            public void onResourceNotFound(ErrorResource errorResource) {

            }

            @Override
            public void onInternalError(ErrorResource errorResource) {

            }

            @Override
            public void onUnknownException(ErrorResource errorResource) {

            }

            @Override
            public void onResourceConflict(ErrorResource errorResource) {

            }
        };
    }

    public MutableLiveData<Boolean> getNetworkErrorMutableData() {
        return networkErrorMutableData;
    }

    public void setNetworkErrorMutableData(MutableLiveData<Boolean> networkErrorMutableData) {
        this.networkErrorMutableData = networkErrorMutableData;
    }
}
