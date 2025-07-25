package com.zebra.demo.zebralib.rfidreader.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.R;
import com.zebra.demo.zebralib.application.Application;
import com.zebra.demo.zebralib.rfidreader.common.Constants;
import com.zebra.demo.zebralib.rfidreader.common.CustomToast;
import com.zebra.demo.zebralib.rfidreader.common.LinkProfileUtil;
import com.zebra.demo.zebralib.rfidreader.notifications.NotificationsService;
import com.zebra.demo.zebralib.rfidreader.rfid.RFIDController;
import com.zebra.rfid.api3.Antennas;
import com.zebra.rfid.api3.DYNAMIC_POWER_OPTIMIZATION;
import com.zebra.rfid.api3.INVENTORY_STATE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.SESSION;

import static com.zebra.demo.zebralib.rfidreader.rfid.RFIDController.ActiveProfile;
import static com.zebra.demo.zebralib.rfidreader.rfid.RFIDController.TAG;
import static com.zebra.demo.zebralib.rfidreader.rfid.RFIDController.dynamicPowerSettings;
import static com.zebra.demo.zebralib.rfidreader.rfid.RFIDController.isLocatingTag;
import static com.zebra.demo.zebralib.rfidreader.rfid.RFIDController.mConnectedReader;
import static com.zebra.demo.zebralib.rfidreader.rfid.RFIDController.mIsInventoryRunning;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p>
 * interface.
 */
public class ProfileFragment extends BackPressedFragment implements ProfileRecyclerViewAdapter.OnListFragmentInteractionListener {

    private ProfileRecyclerViewAdapter.OnListFragmentInteractionListener mListener;
    ArrayAdapter<String> linkAdapter;
    ArrayAdapter<CharSequence> sessionAdapter;
    private ProfileRecyclerViewAdapter profileViewAdapter;
    private LinkProfileUtil linkProfileUtil;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.readerprofilelist);
        // Set the adapter
        if (recyclerView instanceof RecyclerView) {
            Context context = view.getContext();
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            profileViewAdapter = new ProfileRecyclerViewAdapter(ProfileContent.ITEMS, mListener);
            recyclerView.setAdapter(profileViewAdapter);
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    mLayoutManager.getOrientation());
            mDividerItemDecoration.setDrawable((getContext().getResources().getDrawable(R.drawable.profile_divider)));
            recyclerView.addItemDecoration(mDividerItemDecoration);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = this;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        linkProfileUtil = LinkProfileUtil.getInstance();

        linkAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_small_font, linkProfileUtil.getSimpleProfiles());
        linkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sessionAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.session_array, R.layout.custom_spinner_layout);
        sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        profileViewAdapter.mLinkProfileAdapter = linkAdapter;
        profileViewAdapter.mSessionAdapter = sessionAdapter;
        if(SettingsDetailActivity.mSettingOnFactory == false) {
            Button button = getActivity().findViewById(R.id.profilelistbutton);
            button.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onBackPressed() {
        if(SettingsDetailActivity.mSettingOnFactory == true)
            return;

        if (mConnectedReader != null && mConnectedReader.isConnected()) {
            if (!(mIsInventoryRunning || isLocatingTag) && mConnectedReader.Config.Antennas != null) {
                for (ProfileRecyclerViewAdapter.ViewHolder holder : profileViewAdapter.mViewHolderAdapter) {
                    if (holder.mContentView.getText().equals("User Defined") && profileViewAdapter.isUserProfileEnable) {
                        ProfileContent.ProfilesItem mItem = holder.mItem;
                        if (!holder.mtextPower.getText().toString().equals(""))
                            mItem.powerLevel = new Integer(String.valueOf(holder.mtextPower.getText()));
                        if (linkProfileUtil.getSelectedLinkProfilePosition(ActiveProfile.LinkProfileIndex) != holder.mLinkProfileSpinner.getSelectedItemPosition())
                            mItem.LinkProfileIndex = LinkProfileUtil.getInstance().getSimpleProfileModeIndex(holder.mLinkProfileSpinner.getSelectedItemPosition());
                        mItem.SessionIndex = holder.mSession.getSelectedItemPosition();
                        mItem.DPO_On = holder.mDynamicPower.isChecked();
                        mItem.isOn = holder.contentSwitch.isChecked();
                        if (null != mListener) {
                            mListener.onProfileFragmentSwitchInteraction(holder.mItem, holder.contentSwitch.isChecked());
                        }
                    }
                }
            }
        }
        if (getActivity() instanceof SettingsDetailActivity)
            ((SettingsDetailActivity) getActivity()).callBackPressed();
        else
            ((ActiveDeviceActivity) getActivity()).callBackPressed();
        //
    }

    @Override
    public void onProfileFragmentSwitchInteraction(ProfileContent.ProfilesItem item, boolean isChecked) {
        Log.d(TAG, "Content switched " + item.id + " " + isChecked);
        Application.cycleCountProfileData = null;
        if (isChecked && mConnectedReader != null && mConnectedReader.isConnected() && !item.content.equals("Reader Defined")) {
            if (!(mIsInventoryRunning || isLocatingTag) && mConnectedReader.Config.Antennas != null) {
                try {
                    // Antenna
                    Antennas.AntennaRfConfig antennaRfConfig;
                    antennaRfConfig = mConnectedReader.Config.Antennas.getAntennaRfConfig(1);
                    int maxPower = LinkProfileUtil.getInstance().getMaxPowerIndex();
                    if (item.powerLevel > maxPower) {
                        item.powerLevel = maxPower;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), getResources().getString(R.string.invalid_antenna_power), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    antennaRfConfig.setTransmitPowerIndex(item.powerLevel);
                    if (antennaRfConfig.getrfModeTableIndex() != item.LinkProfileIndex) {
                        antennaRfConfig.setTari(0);
                        antennaRfConfig.setrfModeTableIndex(item.LinkProfileIndex);
                    }
                    mConnectedReader.Config.Antennas.setAntennaRfConfig(1, antennaRfConfig);
                    RFIDController.antennaRfConfig = antennaRfConfig;

                    // Singulation
                    Antennas.SingulationControl singulationControl;
                    singulationControl = mConnectedReader.Config.Antennas.getSingulationControl(1);
                    singulationControl.setSession(SESSION.GetSession(item.SessionIndex));
                    if (item.id.equals("0"))
                        singulationControl.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_AB_FLIP);
                    else if (!item.id.equals("5"))
                        singulationControl.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_A);

                    mConnectedReader.Config.Antennas.setSingulationControl(1, singulationControl);
                    RFIDController.singulationControl = singulationControl;

                    // DPO
                    if (item.DPO_On)
                        mConnectedReader.Config.setDPOState(DYNAMIC_POWER_OPTIMIZATION.ENABLE);
                    else
                        mConnectedReader.Config.setDPOState(DYNAMIC_POWER_OPTIMIZATION.DISABLE);
                    dynamicPowerSettings = mConnectedReader.Config.getDPOState();

                    // store profile
                    // off the active profile and store
                    if (!ActiveProfile.id.equals(item.id)) {
                        ActiveProfile.isOn = false;
                        ActiveProfile.StoreProfile();
                    }
                    // store currently active profile
                    item.StoreProfile();

                } catch (InvalidUsageException e) {
                    sendNotification(Constants.ACTION_READER_STATUS_OBTAINED, getString(R.string.status_failure_message) + "\n" + e.getVendorMessage());
                } catch (OperationFailureException e) {
                    sendNotification(Constants.ACTION_READER_STATUS_OBTAINED, getString(R.string.status_failure_message) + "\n" + e.getVendorMessage());
                }
            }
        } else {
            // off the active profile and store
            if (!ActiveProfile.id.equals(item.id)) {
                ActiveProfile.isOn = false;
                ActiveProfile.StoreProfile();
            }
            // store currently active profile
            item.StoreProfile();
        }
    }

    public void sendNotification(String action, String data) {
        if (Application.isActivityVisible()) {
            if (action.equalsIgnoreCase(Constants.ACTION_READER_BATTERY_CRITICAL) || action.equalsIgnoreCase(Constants.ACTION_READER_BATTERY_LOW)) {
                new CustomToast(getActivity(), R.layout.toast_layout, data).show();
            } else {
                Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent i = new Intent(getActivity(), NotificationsService.class);
            i.putExtra(Constants.INTENT_ACTION, action);
            i.putExtra(Constants.INTENT_DATA, data);
            getActivity().startService(i);
        }
    }

}
