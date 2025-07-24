package com.zebra.demo.view.fragment.IBA_FRAGMENTS;

import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.fragment.BarcodeRFIDScanBaseFragment;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;

import java.util.List;

public class InventoryReportFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, RecyclerViewItemClickListener, ApiResponseListener {

    @Override
    public void onSuccess(Object data) {

    }

    @Override
    public void onFailed(Object data) {
        if (data instanceof String) {
            Utility.showToast(String.valueOf(data));
        }
    }

    @Override
    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID) {

    }

    @Override
    public void scannerRFIDEvent(List<InventoryListItem> rfidDataList) {

    }

    @Override
    public void onRecyclerViewItemClickListener(int position, Object object) {

    }
}
