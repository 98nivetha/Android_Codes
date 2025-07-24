package com.zebra.demo.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.zebra.demo.R;
import com.zebra.demo.databinding.ActivityLoginBinding;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    LoginViewModel loginViewModel;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setLogin(loginViewModel);
        setOnClickListener();
        setObserver();
    }


    private void setObserver() {
        loginViewModel.isDataLoading.observe(this, value -> {
            binding.rltAtvtyProgress.setVisibility(value ? View.VISIBLE : View.GONE);
        });

        loginViewModel.userIdValidationText.observe(this, value -> {
            binding.elblLoginLoginname.setError(value);
        });

        loginViewModel.passwordValidationText.observe(this, value -> {
            binding.elblLoginPasscode.setError(value);
        });

        loginViewModel.getNetworkErrorMutableData().observe(this, this::loadNetworkErrorView);

    }

    private void loadNetworkErrorView(boolean value) {
        if (value) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void setOnClickListener() {
        binding.btnLogin.setOnClickListener(view -> {
            loginViewModel.sendLoginRequest(new ApiResponseListener() {
                @Override
                public void onSuccess(Object data) {
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailed(Object data) {
                    if (data instanceof String) {
                        Toast.makeText(LoginActivity.this, String.valueOf(data), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}
