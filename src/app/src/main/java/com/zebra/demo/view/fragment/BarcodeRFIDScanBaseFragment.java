package com.zebra.demo.view.fragment;
import static com.zebra.demo.zebralib.rfidreader.rfid.RFIDController.mIsInventoryRunning;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.view.listener.ReaderTriggerEvent;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.application.Application;
import com.zebra.demo.zebralib.rfidreader.common.ResponseHandlerInterfaces;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import com.zebra.demo.zebralib.rfidreader.rfid.RFIDController;
import com.zebra.demo.zebralib.rfidreader.settings.ISettingsUtil;
import com.zebra.rfid.api3.RFIDResults;

public class BarcodeRFIDScanBaseFragment extends Fragment implements ResponseHandlerInterfaces.ResponseTagHandler, ResponseHandlerInterfaces.TriggerEventHandler, ResponseHandlerInterfaces.BatchModeEventHandler, ResponseHandlerInterfaces.ResponseStatusHandler {

    private BarcodeRFIDScanResultListener barcodeRFIDScanResultListener;
    private static BarcodeRFIDScanBaseFragment mBarcodeRFIDScanBaseFragment = null;
    private static final String TAG = "BarcodeRFIDScanBaseFragment";
    boolean batchModeEventReceived = false;
    private ISettingsUtil settingsUtil;

    private ReaderTriggerEvent readerTriggerEvent;

    public BarcodeRFIDScanBaseFragment() {
    }

    public ReaderTriggerEvent getReaderTriggerEvent() {
        return readerTriggerEvent;
    }

    public void setReaderTriggerEvent(ReaderTriggerEvent readerTriggerEvent) {
        this.readerTriggerEvent = readerTriggerEvent;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BarcodeRFIDScanBaseFragment.
     */
    public static BarcodeRFIDScanBaseFragment newInstance() {
        //if( mBarcodeRFIDScanBaseFragment == null )
        mBarcodeRFIDScanBaseFragment = new BarcodeRFIDScanBaseFragment();
        return mBarcodeRFIDScanBaseFragment;
    }

    public void setBarcodeRFIDScanResultListener(BarcodeRFIDScanResultListener barcodeRFIDScanResultListener ) {
        this.barcodeRFIDScanResultListener = barcodeRFIDScanResultListener;
    }

    public static Fragment newInstance(int position) {

        BarcodeRFIDScanBaseFragment fragment = new BarcodeRFIDScanBaseFragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() instanceof ActiveDeviceActivity) {
            ActiveDeviceActivity mainActivity = (ActiveDeviceActivity) getActivity();
            mainActivity.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            onRapidReadSelected();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onRapidReadSelected()
    {

        if (RFIDController.mIsInventoryRunning) {
        } else {
        }
        if (RFIDController.isBatchModeInventoryRunning != null && RFIDController.isBatchModeInventoryRunning) {
        } else{

        }
        if (RFIDController.mRRStartedTime == 0)
            Application.TAG_READ_RATE = 0;
        else
            Application.TAG_READ_RATE = (int) (Application.TOTAL_TAGS / (RFIDController.mRRStartedTime / (float) 1000));
        if(getActivity() instanceof ActiveDeviceActivity) {
            settingsUtil = (ActiveDeviceActivity) getActivity();
            if (Application.TAG_LIST_MATCH_MODE) {
                Log.d("Nikhil1", "onRFIDFragment() RFIDinventoryfragment LoadTagcsv will get call next");
                settingsUtil.LoadTagListCSV();
            }
        }
    }

    public void updateTexts() {
        if(barcodeRFIDScanResultListener != null) {
            barcodeRFIDScanResultListener.scannerRFIDEvent(Application.tagsReadInventory);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * method to reset tags info on the screen before starting inventory operation
     */
    public void resetTagsInfo() {
        updateTexts();
    }

    /**
     * method to start inventory operation on trigger press event received
     */
    public void triggerPressEventRecieved() {
        if (!RFIDController.mIsInventoryRunning && getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(getActivity() instanceof ActiveDeviceActivity) {
                        ActiveDeviceActivity activity = (ActiveDeviceActivity) getActivity();
                        if (activity != null) {
                            if(readerTriggerEvent != null) {
                                readerTriggerEvent.onStartReaderEvent();
                            }
                            activity.inventoryStartOrStop(null);
                        }
                    }
                }
            });
        }
    }

    /**
     * method to stop inventory operation on trigger release event received
     */
    public void triggerReleaseEventRecieved() {
        if ((RFIDController.mIsInventoryRunning == true) && getActivity() != null) {
            //RFIDController.mInventoryStartPending = false;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(getActivity() instanceof ActiveDeviceActivity) {
                    ActiveDeviceActivity activity = (ActiveDeviceActivity) getActivity();
                    if (activity != null) {
                        activity.inventoryStartOrStop(null);
                        if(readerTriggerEvent != null) {
                            readerTriggerEvent.onStopReaderEvent();
                        }
                    }
                    }
                }
            });
        }
    }

    public void handleStatusResponse(final RFIDResults results) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (results.equals(RFIDResults.RFID_BATCHMODE_IN_PROGRESS)) {

                } else if(results.equals(RFIDResults.RFID_OPERATION_IN_PROGRESS )){

                    mIsInventoryRunning = true;

                }else if (!results.equals(RFIDResults.RFID_API_SUCCESS)) {
                    RFIDController.mIsInventoryRunning = false;

                    RFIDController.isBatchModeInventoryRunning = false;
                }
            }
        });
    }


    /**
     * method to update inventory details on the screen on operation end summary received
     */
    public void updateInventoryDetails() {
        updateTexts();
    }

    /**
     * method to reset inventory operation status on the screen
     */
    public void resetInventoryDetail() {
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    {

                    }
                }
            });
    }

    @Override
    public void batchModeEventReceived() {
        batchModeEventReceived = true;
    }

    @Override
    public void handleTagResponse(InventoryListItem inventoryListItem, boolean isAddedToList) {
        updateTexts();
    }

    public void scannerBarcodeEventDetected(byte[] barcodeData, int barcodeType, int scannerID) {
        if(barcodeRFIDScanResultListener != null) {
            barcodeRFIDScanResultListener.scannerBarcodeEvent(barcodeData, barcodeType, scannerID);
        }
    }
}
