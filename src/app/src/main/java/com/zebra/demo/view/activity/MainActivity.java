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


        //setOnClickListener();
        //setValue();
        switchToFragment(new MainMenuFragment());
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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

                                //Clear Local SharedPreference
                                SharedPreference sharedPreference = new SharedPreference(MainActivity.this);
                                sharedPreference.setLoggedIn(false);
                                sharedPreference.clearSession();

                                //Move to Login Page
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

    public void setToolBarTitleWithBack(int resource) {
        binding.toolbar.setTitle(resource);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setToolBarTitleWithBack(String title) {
        binding.toolbar.setTitle(title);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Page back click
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        //finish();
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


/**
 * Main Menu Activity Class
 */
//public class MainActivity extends AppCompatActivity {
//    ActivityMainBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//
//        setToolBar();
//        setOnClickListener();
//        setValue();
//    }
//
//    /**
//     * Adding Values to UI
//     */
//    private void setValue() {
//        SharedPreference sharedPreference = new SharedPreference(MainActivity.this);
//        binding.titleTxt.setText("Welcome " + sharedPreference.getUserName() + " !");
//        binding.descTxt.setText(sharedPreference.getUserDeptName());
//    }
//
//    /**
//     * Adding Toolbar menu with alert dialog
//     */
//    private void setToolBar() {
//        binding.toolbar.inflateMenu(R.menu.main_menu);
//
//        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if (item.getItemId() == R.id.logout) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
//                            .setMessage("Are you sure you want to logout ?")
//                            .setPositiveButton("Yes", (dialog, which) -> {
//
//                                //Clear Local SharedPreference
//                                SharedPreference sharedPreference = new SharedPreference(MainActivity.this);
//                                sharedPreference.setLoggedIn(false);
//                                sharedPreference.clearSession();
//
//                                //Move to Login Page
//                                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                                startActivity(intent);
//                                finish();
//                            })
//                            .setNegativeButton("No", (dialog, which) -> dialog.cancel());
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                }
//                return false;
//            }
//        });
//    }
//
//    /**
//     * UI Click Listener method
//     */
//    private void setOnClickListener() {
//
//        //Tag Mapping
//        binding.tagMappingRlay.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, TagMappingFragment.class);
//            startActivity(intent);
//        });
//
//        //Asset View
//        binding.assetViewRlay.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, AssetViewFragment.class);
//            startActivity(intent);
//        });
//
//        //Asset Inward
//        binding.assetInwardRlay.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, AssetInwardListingFragment.class);
//            startActivity(intent);
//        });
//
//        //Asset Outward
//        binding.assetOutwardRlay.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, AssetOutwardListFragment.class);
//            startActivity(intent);
//        });
//
//        //Audit
//        binding.auditRlay.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, AssetAuditTypeSelectionFragment.class);
//            startActivity(intent);
//
//
//        });
//
//        //Asset Search
//        binding.assetSearchRlay.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, AssetSearchFragment.class);
//            startActivity(intent);
//        });
//
//        //Connect To Device
//        binding.btnConnectToDevice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, DeviceDiscoverActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//}
