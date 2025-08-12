package com.zebra.demo.view.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.zebra.demo.R;
import com.zebra.demo.data.local.SharedPreference;
import com.zebra.demo.databinding.ActivityMainBinding;
import com.zebra.demo.view.fragment.MainMenuFragment;


/**
 * Main Menu Activity Class
 */

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);
        switchToFragment(new MainMenuFragment());
    }

    @Override
    public void onBackPressed() {
        checkExitApp();
    }

    private void checkExitApp() {
        try {
            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments < 1) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this)
                        .setMessage("Are you sure you want to exit this App?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.cancel());
                android.app.AlertDialog alert = builder.create();
                alert.show();

                Button negative = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setTextColor(getResources().getColor(R.color.alertIconColor));

                Button positive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setTextColor(getResources().getColor(R.color.alertIconColor));

            } else {
                super.onBackPressed();
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Adding Toolbar menu with alert dialog
     */
    private void setToolBar() {
        binding.toolbar.setTitle(R.string.app_name);
        binding.toolbar.inflateMenu(R.menu.main_menu);

        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Are you sure you want to logout ?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                SharedPreference sharedPreference = new SharedPreference(MainActivity.this);
                                sharedPreference.setLoggedIn(false);
                                sharedPreference.clearSession();
                                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                return false;
            }
        });
    }

    public void setToolBarTitleWithoutBack(int resource) {
        binding.toolbar.setTitle(resource);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }
    /**
     * Page back click
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    public void switchToFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, null).commit();
        }
    }

    public void switchToFragmentAddBackStack(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, null).addToBackStack(null).commit();
        }
    }
}

