package com.zebra.demo.view.listener;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import java.util.List;

public interface BarcodeRFIDScanResultListener {

    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID);
    public void scannerRFIDEvent(List<InventoryListItem> rfidDataList);


}
