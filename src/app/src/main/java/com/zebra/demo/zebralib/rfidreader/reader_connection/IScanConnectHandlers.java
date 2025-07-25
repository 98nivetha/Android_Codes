package com.zebra.demo.zebralib.rfidreader.reader_connection;

import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.scannercontrol.DCSSDKDefs;

public interface IScanConnectHandlers {
    void disconnect(int scannerId);
    DCSSDKDefs.DCSSDK_RESULT connect(int scannerId);
    void reInit();
    void scanTaskDone(ReaderDevice connectingDevice);
    void showScanProgressDialog(ReaderDevice connectingDevice);
    void cancelScanProgressDialog();


}
