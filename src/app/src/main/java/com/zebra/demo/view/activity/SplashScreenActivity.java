package com.zebra.demo.view.activity;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.zebra.demo.R;
import com.zebra.demo.data.local.SharedPreference;
import com.zebra.demo.databinding.ActivitySplashScreenBinding;


public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_DURATION = 1000;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashScreenBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        new Handler().postDelayed(() -> {
            Intent intent = null;
            SharedPreference sharedPreference = new SharedPreference(SplashScreenActivity.this);
            if(sharedPreference.isLoggedIn()) {
                intent = new Intent(SplashScreenActivity.this,
                        MainActivity.class);
            } else {
                intent = new Intent(SplashScreenActivity.this,
                        MainActivity.class);
            }
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        }, SPLASH_TIME_DURATION);
    }
}
