package com.zebra.demo.view.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.zebra.demo.R;
import com.zebra.demo.data.local.SharedPreference;
import com.zebra.demo.databinding.FragmentMainMenuBinding;
import com.zebra.demo.view.activity.LoginActivity;
import com.zebra.demo.view.activity.MainActivity;
import com.zebra.demo.view.fragment.IBA_FRAGMENTS.InventoryReportFragment;
import com.zebra.demo.view.fragment.IBA_FRAGMENTS.MaterialViewFragment;
import com.zebra.demo.view.fragment.IBA_FRAGMENTS.PutAwayFragment;
import com.zebra.demo.view.fragment.IBA_FRAGMENTS.RFIDMappingFragment;
import com.zebra.demo.view.fragment.IBA_FRAGMENTS.RackWiseInfoFragment;
import com.zebra.demo.view.fragment.IBA_FRAGMENTS.ReAllocateFragment;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.DeviceDiscoverActivity;

public class MainMenuFragment extends Fragment {
    FragmentMainMenuBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false);
        setOnClickListener();
        setValue();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolBarTitleWithoutBack(R.string.app_name);
        } else if (getActivity() instanceof ActiveDeviceActivity) {
            ((ActiveDeviceActivity) getActivity()).setToolBarTitleWithoutBack(R.string.app_name);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (getActivity() == null) {
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("Are you sure you want to logout ?").setPositiveButton("Yes", (dialog, which) -> {
                    SharedPreference sharedPreference = new SharedPreference(getActivity());
                    sharedPreference.setLoggedIn(false);
                    sharedPreference.clearSession();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }).setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
    }


    /**
     * Adding Values to UI
     */
    private void setValue() {
        SharedPreference sharedPreference = new SharedPreference(getActivity());
        binding.titleTxt.setText("Welcome " + sharedPreference.getUserName() + " !");
        binding.descTxt.setText(sharedPreference.getUserDeptName());
    }

    /**
     * UI Click Listener method
     */
    private void setOnClickListener() {

        //Tag Mapping
        binding.rfidMappingRlay.setOnClickListener(view -> {
            loadFragment(new RFIDMappingFragment()                          );
        });

        //Asset View
        binding.rfidMappingRlay.setOnClickListener(view -> {
            //TODO: Comment This call later
            /*AssetViewFragment fragment = new AssetViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString(AssetViewFragment.ASSET_SR_CODE_KEY,"319413");
            fragment.setArguments(bundle);
            loadFragment(fragment);*/
            //TODO: Uncomment This line later
            loadFragment(new AssetViewFragment());

        });
        binding.putAwayRlay.setOnClickListener(view -> {
            loadFragment(new PutAwayFragment());
        });
        //Audit
        binding.auditRlay.setOnClickListener(view -> {
            loadFragment(new AssetAuditTypeSelectionFragment());
        });

        //Asset Search
        binding.materialInfoRlay.setOnClickListener(view -> {
            loadFragment(new MaterialViewFragment());
        });

        binding.rackwiseInfoImg.setOnClickListener(view -> {
            loadFragment(new RackWiseInfoFragment());
        });

        binding.reAllocateRlay.setOnClickListener(view -> {
            loadFragment(new ReAllocateFragment());
        });

        binding.inventoryReportRlay.setOnClickListener(view -> {
            loadFragment(new InventoryReportFragment());
        });

        //Connect To Device
        binding.btnConnectToDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DeviceDiscoverActivity.class);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).switchToFragmentAddBackStack(fragment);
        } else if (getActivity() instanceof ActiveDeviceActivity) {
            ((ActiveDeviceActivity) getActivity()).switchToFragmentAddBackStack(fragment);
        }
    }
}
