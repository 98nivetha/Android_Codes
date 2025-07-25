package com.zebra.demo.zebralib.scanner.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.LoggerFragment;
import com.zebra.scannercontrol.DCSSDKDefs;
import com.zebra.demo.zebralib.scanner.helpers.ScannerAppEngine;
import com.zebra.demo.R;
import com.zebra.demo.zebralib.application.Application;
import com.zebra.demo.zebralib.scanner.helpers.Constants;
import com.zebra.demo.zebralib.scanner.helpers.CustomProgressDialog;
import com.zebra.demo.zebralib.scanner.helpers.Symbology;
import com.zebra.scannercontrol.FirmwareUpdateEvent;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import static com.zebra.scannercontrol.RMDAttributes.*;


import java.io.StringReader;
import java.util.Stack;


public class SymbologiesFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener, ScannerAppEngine.IScannerAppEngineDevConnectionsDelegate, ScannerAppEngine.IScannerAppEngineDevEventsDelegate, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "SymbologiesFragment";
    private boolean settingsFetched;
    private static final String MSG_FETCH = "Fetching settings";
    private ArrayList<Symbology> symbologies;
    private AsyncTask task;
    private Stack<Integer> attStack; //RHBJ36-SSDK-3450-12.24.2015
	private CustomProgressDialog progressDialog;
    private NavigationView navigationView;
    SymbologiesFragment mSymbologiesFragment = null;
    Menu menu;
    MenuItem pairNewScannerMenu;

    private int scannerID;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static SymbologiesFragment newInstance() {
        SymbologiesFragment mSymbologiesFragment = new SymbologiesFragment();
        return mSymbologiesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.content_symbologies, container, false);

        scannerID = Application.currentScannerId;

		/*RHBJ36-SSDK-3450-12.24.2015
		 * Allocate stack to store attributes.
		*/
        attStack = new Stack<Integer>();
        symbologies = new ArrayList<>();
        symbologies.add(new Symbology("UPC-A", RMD_ATTR_SYM_UPC_A));                                //0
        symbologies.add(new Symbology("UPC-E", RMD_ATTR_SYM_UPC_E));                                //1
        symbologies.add(new Symbology("UPC-E1", RMD_ATTR_SYM_UPC_E_1));                             //2
        symbologies.add(new Symbology("EAN-8/JAN8", RMD_ATTR_SYM_EAN_8_JAN_8));                     //3
        symbologies.add(new Symbology("EAN-13/JAN13", RMD_ATTR_SYM_EAN_13_JAN_13));                 //4
        symbologies.add(new Symbology("Bookland EAN", RMD_ATTR_SYM_BOOKLAND_EAN));                  //5
        symbologies.add(new Symbology("Code 128", RMD_ATTR_SYM_CODE_128));                          //6
        symbologies.add(new Symbology("GS1-128", RMD_ATTR_SYM_UCC_EAN_128));                        //7
        symbologies.add(new Symbology("Code 39", RMD_ATTR_SYM_CODE_39));                            //8
        symbologies.add(new Symbology("Code 93", RMD_ATTR_SYM_CODE_93));                            //9
        symbologies.add(new Symbology("Code 11", RMD_ATTR_SYM_CODE_11));                            //10
        symbologies.add(new Symbology("Interleaved 2 of 5", RMD_ATTR_SYM_INTERLEAVED_2_OF_5));      //11
        symbologies.add(new Symbology("Discrete 2 of 5", RMD_ATTR_SYM_DISCRETE_2_OF_5));            //12
        symbologies.add(new Symbology("Chinese 2 of 5", RMD_ATTR_SYM_CHINESE_2_OF_5));              //13
        symbologies.add(new Symbology("Codabar", RMD_ATTR_SYM_CODABAR));                            //14
        symbologies.add(new Symbology("MSI", RMD_ATTR_SYM_MSI));                                    //15
        symbologies.add(new Symbology("Code 32", RMD_ATTR_SYM_CODE_32));                            //16
        symbologies.add(new Symbology("Data Matrix", RMD_ATTR_SYM_DATAMATRIXQR));                   //17
        symbologies.add(new Symbology("PDF417", RMD_ATTR_SYM_PDF));                                 //18
        symbologies.add(new Symbology("ISBN", RMD_ATTR_SYM_ISBN));                                  //19
        symbologies.add(new Symbology("UCC Coupon Extended Code", RMD_ATTR_SYM_UCC_COUPON_EXTENDED));//20
        symbologies.add(new Symbology("ISSN EAN", RMD_ATTR_SYM_ISSN_EAN));                          //21
        symbologies.add(new Symbology("ISBT 128", RMD_ATTR_SYM_ISBT_128));                          //22
        symbologies.add(new Symbology("Trioptic Code 39", RMD_ATTR_SYM_TRIOPTIC_CODE_39));          //23
        symbologies.add(new Symbology("Matrix 2 of 5", RMD_ATTR_SYM_MATRIX_2_OF_5));                //24
        symbologies.add(new Symbology("Korean 3 of 5", RMD_ATTR_SYM_KOREAN_3_OF_5));                //25
        symbologies.add(new Symbology("GS1 DataBar-14", RMD_ATTR_SYM_GS1_DATABAR_14));              //26
        symbologies.add(new Symbology("GS1 DataBar Limited", RMD_ATTR_SYM_GS1_DATABAR_LIMITED));    //27
        symbologies.add(new Symbology("GS1 DataBar Expanded", RMD_ATTR_SYM_GS1_DATABAR_EXPANDED));  //28
        symbologies.add(new Symbology("MicroPDF417", RMD_ATTR_SYM_MICROPDF417));                    //29
        symbologies.add(new Symbology("Maxicode", RMD_ATTR_SYM_MAXICODE));                          //30
        symbologies.add(new Symbology("QR Code", RMD_ATTR_SYM_QR_CODE));                            //31
        symbologies.add(new Symbology("Aztec", RMD_ATTR_SYM_AZTEC));                                //32
        symbologies.add(new Symbology("Han Xin Code", RMD_ATTR_SYM_HAN_XIN_CODE));                  //33
        symbologies.add(new Symbology("Australian Post", RMD_ATTR_SYM_AUSTRALIAN_POST));            //34
        symbologies.add(new Symbology("US PLANET", RMD_ATTR_SYM_US_PLANET));                        //35
        symbologies.add(new Symbology("US POSTNET", RMD_ATTR_SYM_US_POSTNET));                      //36
        symbologies.add(new Symbology("Netherlands KIX", RMD_ATTR_SYM_KIX_CODE));                   //37
        symbologies.add(new Symbology("USPS 4CB", RMD_ATTR_SYM_USPS_4CB));                          //38
        symbologies.add(new Symbology("UK Postal", RMD_ATTR_SYM_UK_POSTAL));                        //39
        symbologies.add(new Symbology("Japan Post", RMD_ATTR_SYM_JAPAN_POST));                      //40
        symbologies.add(new Symbology("UPU FICS", RMD_ATTR_SYM_FICS));                              //41
        symbologies.add(new Symbology("MicroQR", RMD_ATTR_SYM_MICRO_QR_CODE));                      //42
        symbologies.add(new Symbology("Composite C", RMD_ATTR_SYM_COMPOSITE_C));                        //43
        symbologies.add(new Symbology("Composite AB", RMD_ATTR_SYM_COMPOSITE_AB));                       //44
        symbologies.add(new Symbology("TLC39", RMD_ATTR_SYM_COMPOSITE_TLC39));                    //45
        symbologies.add(new Symbology("Dot Code", RMD_ATTR_SYM_DOTCODE));                            //46

        return rootview;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.no_items, menu);

    }

    /*RHBJ36 12.15.2015
	 *Cancel the async task (Fetching symbologies) on destroying the Symbology activity.
	*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(task !=null){
            task.cancel(true);
            task=null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //addDevConnectionsDelegate(this);
        getSymbologiesConfiguration();
    }

    private void registerCheckChangeListner() {
        ((SwitchCompat)getView().findViewById(R.id.upcA)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.upcE)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.upcE1)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.ean8)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.ean13)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.bookland)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.code128)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.ean128)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.code39)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.code93)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.code11)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.interleaved)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.discrete)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.chinese)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.codabar)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.msi)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.code32)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.dataMatrix)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.pdf)).setOnCheckedChangeListener(this);

        ((SwitchCompat)getView().findViewById(R.id.isbn)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.ucc_coupon_extended)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.issn_ean)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.isbt_128)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.triopticCode39)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.matrix2o5)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.korean3o5)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.gs1DataBar14)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.gs1DatabarLimited)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.gs1DatabarExpanded)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.micropdf417)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.maxicode)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.qrCode)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.microQRCode)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.aztec)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.hanXinCode)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.aupost)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.us_planet)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.us_postnet)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.kixCode)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.usps4CB)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.uk_postal)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.japanPostal)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.fics)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.compositec)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.compositeAB)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.tlc39)).setOnCheckedChangeListener(this);
        ((SwitchCompat)getView().findViewById(R.id.dotcode)).setOnCheckedChangeListener(this);
    }


    private void getSymbologiesConfiguration() {
        int scannerID = Application.currentScannerId;
        if (scannerID != -1) {
            String inXML = "<inArgs><scannerID>" + scannerID + "</scannerID></inArgs>";
            String outXML;
            String scannerName = Application.currentScannerName;
            /*RHBJ36 check null 12.15.2015
			 *Check null for not to create multilple Asynctask for fetching symbologies with same task variable to avoid crash.
			*/
            if(task==null)
                task = new MyAsyncTask(scannerID, scannerName,DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GETALL, null).execute(new String[]{inXML});
        } else {
            Toast.makeText(getActivity(), Constants.INVALID_SCANNER_ID_MSG, Toast.LENGTH_SHORT).show();
        }
    }



    private void setSymbologyConfiguration(String param, View view) {
        String scannerName = Application.currentScannerName;

        CheckBox cbPermanant = (CheckBox)getView().findViewById(R.id.checkBoxPermanant);
        if(cbPermanant.isChecked()) {
            new MyAsyncTask(Application.currentScannerId,scannerName, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_STORE, view).execute(new String[]{param});
        }else{
            new MyAsyncTask(Application.currentScannerId,scannerName, DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_SET, view).execute(new String[]{param});

        }
    }
	/*RHBJ36:Issue reported from IOS.SSDK-3471.
	 *Change for fix for crashing when scanning barcode while fetching Symbologies.
	 *Dialog is dismisses after Activity finished. Added check to confirm if the activity is not in Finishing state
	 *before dismissing the dialog.
	*/
	private void dismissDialog(){
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_pair_device) {
            ((ActiveDeviceActivity)getActivity()).disconnect(scannerID);
            //Application.barcodeData.clear();
            Application.currentScannerId = Application.SCANNER_ID_NONE;
            //finish();
            intent = new Intent(getActivity(), ScannerHomeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_devices) {
            intent = new Intent(getActivity(), ScannersActivity.class);

            startActivity(intent);
        }else if (id == R.id.nav_find_cabled_scanner) {
            AlertDialog.Builder dlg = new  AlertDialog.Builder(getActivity());
            dlg.setTitle("This will disconnect your current scanner");
            //dlg.setIcon(android.R.drawable.ic_dialog_alert);
            dlg.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg) {

                    ((ActiveDeviceActivity)getActivity()).disconnect(scannerID);
                    //Application.barcodeData.clear();
                    Application.currentScannerId = Application.SCANNER_ID_NONE;
                    //finish();
                    Intent intent = new Intent(getActivity(), FindCabledScanner.class);
                    startActivity(intent);
                }
            });

            dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg) {

                }
            });
            dlg.show();
        }else if (id == R.id.nav_connection_help) {
            intent = new Intent(getActivity(), ConnectionHelpActivity2.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) getView().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.setSelected(true);
        return true;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (settingsFetched) {
            Symbology buttonSymbology = new Symbology((String) buttonView.getTag(), 0);
            Symbology symbology = symbologies.get(symbologies.indexOf(buttonSymbology));
            String inXML = "<inArgs><scannerID>" + Application.currentScannerId + "</scannerID><cmdArgs><arg-xml><attrib_list><attribute>" +
                    "<id>" + symbology.getRmdAttributeID() + "</id><datatype>F</datatype><value>" + isChecked + "</value>" +
                    "</attribute></attrib_list></arg-xml></cmdArgs></inArgs>";
            setSymbologyConfiguration(inXML, buttonView);
        } else {
            Toast.makeText(getActivity(), "Supported symbologies have not been retrieved. Action will not be performed", Toast.LENGTH_SHORT).show();
        }

        final TextView textView = (TextView)getView().findViewById(getTitleTextID(((SwitchCompat) buttonView).getTag().toString()) );

        if(isChecked){
            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.font_color));
        }else{
            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.inactive_text));
        }


    }

    private int getTitleTextID(String tag) {
        if(tag.equals("UPC-A") )return R.id.upcA_title    ;
        if(tag.equals("UPC-E"))return R.id.upcETitle    ;
        if(tag.equals("UPC-E1"))return R.id.upcE1Title    ;
        if(tag.equals("EAN-8/JAN8"))return R.id.ean8Title    ;
        if(tag.equals("EAN-13/JAN13"))return R.id.ean13Title    ;
        if(tag.equals("Bookland EAN"))return R.id.booklandTitle    ;
        if(tag.equals("Code 128"))return R.id.code128Title    ;
        if(tag.equals("GS1-128"))return R.id.ean128Title    ;
        if(tag.equals("Code 39"))return R.id.code39Title   ;
        if(tag.equals("Code 93"))return R.id.code93Title    ;
        if(tag.equals("Code 11"))return R.id.code11Title    ;
        if(tag.equals("Interleaved 2 of 5"))return R.id.interleavedTitle    ;
        if(tag.equals("Discrete 2 of 5"))return R.id.discreteTitle    ;
        if(tag.equals("Chinese 2 of 5"))return R.id.chineseTitle    ;
        if(tag.equals("Codabar"))return R.id.codabarTitle    ;
        if(tag.equals("MSI"))return R.id.msiTitle    ;
        if(tag.equals("Code 32"))return R.id.code32Title    ;
        if(tag.equals("Data Matrix"))return R.id.dataMatrixTitle    ;
        if(tag.equals("PDF417"))return R.id.pdfTitle    ;
        if(tag.equals("ISBN"))return R.id.isbnTitle    ;
        if(tag.equals("UCC Coupon Extended Code"))return R.id.ucc_coupon_extended_Title    ;
        if(tag.equals("ISSN EAN"))return R.id.issn_ean_Title   ;
        if(tag.equals("ISBT 128"))return R.id.isbt_128Title    ;
        if(tag.equals("Trioptic Code 39"))return R.id.triopticCode39Title    ;
        if(tag.equals("Matrix 2 of 5"))return R.id.matrix2o5Title    ;
        if(tag.equals("Korean 3 of 5"))return R.id.korean3o5Title    ;
        if(tag.equals("GS1 DataBar-14"))return R.id.gs1DataBar14Title    ;
        if(tag.equals("GS1 DataBar Limited"))return R.id.gs1DatabarLimitedTitle   ;
        if(tag.equals("GS1 DataBar Expanded"))return R.id.gs1DatabarExpandedTitle   ;
        if(tag.equals("MicroPDF417"))return R.id.microPDF417Title    ;
        if(tag.equals("Maxicode"))return R.id.maxicodeTitle    ;
        if(tag.equals("QR Code"))return R.id.qrCodeTitle    ;
        if(tag.equals("Aztec"))return R.id.aztecTitle    ;
        if(tag.equals("Han Xin Code"))return R.id.hanXinCodeTitle    ;
        if(tag.equals("Australian Post"))return R.id.aupostTitle   ;
        if(tag.equals("US PLANET"))return R.id.us_planetTitle    ;
        if(tag.equals("US POSTNET"))return R.id.us_postnetTitle    ;
        if(tag.equals("Netherlands KIX"))return R.id.kixCodeTitle    ;
        if(tag.equals("USPS 4CB"))return R.id.usps4CBTitle    ;
        if(tag.equals("UK Postal"))return R.id.uk_postalTitle    ;
        if(tag.equals("Japan Post"))return R.id.japanPostalTitle    ;
        if(tag.equals("UPU FICS"))return R.id.ficsTitle    ;
        if(tag.equals("MicroQR"))return R.id.microQrCodeTitle    ;
        if(tag.equals("Composite C"))return R.id.composteCTitle    ;
        if(tag.equals("Composite AB"))return R.id.compositeABTitle    ;
        if(tag.equals("TLC39"))return R.id.tlc39Title    ;
        if(tag.equals("Dot Code"))return R.id.dotCodeTitle    ;

        return 0;
    }

    @Override
    public boolean scannerHasAppeared(int scannerID) {
        return false;
    }

    @Override
    public boolean scannerHasDisappeared(int scannerID) {
        return false;
    }

    @Override
    public boolean scannerHasConnected(int scannerID) {
        pairNewScannerMenu.setTitle(R.string.menu_item_device_disconnect);
        return false;
    }

    @Override
    public boolean scannerHasDisconnected(int scannerID) {
        //Application.barcodeData.clear();
        pairNewScannerMenu.setTitle(R.string.menu_item_device_pair);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        //removeDevConnectiosDelegate(this);
    }

    @Override
    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID) {

    }

    @Override
    public void scannerFirmwareUpdateEvent(FirmwareUpdateEvent firmwareUpdateEvent) {

    }

    @Override
    public void scannerImageEvent(byte[] imageData) {

    }

    @Override
    public void scannerVideoEvent(byte[] videoData) {

    }

    private class MyAsyncTask extends AsyncTask<String,Integer,Boolean> {
        int scannerId;
        DCSSDKDefs.DCSSDK_COMMAND_OPCODE opcode;
        View view;
        String outXML;
        String scannerName;
        

        public MyAsyncTask(int scannerId, String scannerName,DCSSDKDefs.DCSSDK_COMMAND_OPCODE opcode, View _view) {
            this.scannerId=scannerId;
            this.opcode=opcode;
            this.view = _view;
            this.scannerName = scannerName;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new CustomProgressDialog(getActivity(), "Please wait...");
			/*RHBJ36 12.15.2015
			 * Added Dialog box with cancel option while fetching symbologies. So that user can cancel the task and directly navigate to the Settings
			 * cancelling the current symbologies fetching task.
			 */
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismissDialog();
                    //RHBJ36 : finish Activity on cancle async task 12.15.2015
                    if(task != null) {
                        task.cancel(true);
                        task = null;
                        //finish();
                    }
                }
            });
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            boolean result=false;
            /*RHBJ36 : Add Try catch 12.15.2015
			 * Handle executeCommand in try catch to avoid application crash if executeCommand fails.
			 * Dismissed the dialog if showing any.
			 */
            try {
                result = ((ActiveDeviceActivity)getActivity()).executeCommand(opcode, strings[0], sb, scannerId);
            }catch (Exception e){
                dismissDialog();
            }
            if (opcode == DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GETALL) {
                if (result) {
                    try {
                        Log.i(TAG, sb.toString());
                        int i = 0;
                        String[] attrs = new String[Short.MAX_VALUE];
                        XmlPullParser parser = Xml.newPullParser();

                        parser.setInput(new StringReader(sb.toString()));
                        int event = parser.getEventType();
                        String text = null;
                        while (event != XmlPullParser.END_DOCUMENT) {
                            String name = parser.getName();
                            switch (event) {
                                case XmlPullParser.START_TAG:
                                    break;
                                case XmlPullParser.TEXT:
                                    text = parser.getText();
                                    break;

                                case XmlPullParser.END_TAG:
                                    if (name.equals("attribute")) {
                                        attrs[i++] = text;
                                    }
                                    break;
                            }
                            event = parser.next();
                        }

                        if (attrs.length > 0) {
                            for (Symbology symbology : symbologies) {
                                int sym_attr_id = symbology.getRmdAttributeID();

                                for (String str : attrs) {
                                    if (str != null && sym_attr_id == Integer.parseInt(str)) {
                                        symbology.setSupported(true);
                                        Log.i(TAG, symbology.getSymbologyName() + " Supported");
                                        break;
                                    }
                                }
                            }

                            /*RHBJ36-SSDK-3450-12.24.2015
							 *With CS4070, Symbologies fetching takes longer time.
							 *when experimented, it observed that CS4070 supports 14-15 Symbologies fetch with best optimized time.
							 *Following change is to fetch 14 symbologies (can be changed) in single fetch.
							 */
                            for(Symbology sym:symbologies){
                                if(sym.isSupported()){
                                    attStack.push(sym.getRmdAttributeID());
                                }
                            }

                            /*RHBJ36-SSDK-3450-12.24.2015
							 *With CS4070, Symbologies fetching takes longer time.
							 *when experimented, it observed that CS4070 supports 14-15 Symbologies fetch with best optimized time.
							 *Following change is to fetch 14 symbologies (can be changed) in single fetch.
							 */
                            if (scannerName.startsWith("CS4070")) {
                                while(attStack.size()>0){
                                    StringBuilder in_xml = new StringBuilder("<inArgs><scannerID>" + scannerId + " </scannerID><cmdArgs><arg-xml><attrib_list>");
                                    for(int val=0;val<14; val++){
                                        if(attStack.size()!=0)
                                            in_xml.append(attStack.pop()).append(",");
                                    }
                                    in_xml.append("</attrib_list></arg-xml></cmdArgs></inArgs>");
                                    StringBuilder sbGet = new StringBuilder();
                                    boolean res = ((ActiveDeviceActivity)getActivity()).executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GET, in_xml.toString(), sbGet, scannerId);
                                    Log.i(TAG, sbGet.toString());
                                    outXML = sbGet.toString();

                                    int iAttributeID = -1;
                                    //String[] attrs = new String[Short.MAX_VALUE];
                                    parser = Xml.newPullParser();

                                    parser.setInput(new StringReader(outXML.trim()));
                                    event = parser.getEventType();
                                    text = null;
                                    while (event != XmlPullParser.END_DOCUMENT) {
                                        String name = parser.getName();
                                        switch (event) {
                                            case XmlPullParser.START_TAG:
                                                break;
                                            case XmlPullParser.TEXT:
                                                text = parser.getText();
                                                break;
                                            case XmlPullParser.END_TAG:
                                                if (name.equals("id")) {
                                                    iAttributeID = Integer.parseInt(text);
                                                } else if (name.equals("value")) {
                                                    if(text!=null) {
                                                        for (Symbology symbology : symbologies) {
                                                            if (symbology.getRmdAttributeID() == iAttributeID) {
                                                                if (text.equalsIgnoreCase("true")) {
                                                                    symbology.setEnabled(true);
                                                                } else {
                                                                    symbology.setEnabled(false);
                                                                }
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                break;
                                        }
                                        event = parser.next();
                                    }
                                    settingsFetched = true;
                                }
                            } else {
                                //* request values of all supported symbologies *//*
                                StringBuilder in_xml = new StringBuilder("<inArgs><scannerID>" + scannerId + " </scannerID><cmdArgs><arg-xml><attrib_list>");
                                for (Symbology symbology : symbologies) {
                                    if (symbology.isSupported()) {
                                        in_xml.append(symbology.getRmdAttributeID()).append(",");
                                    }
                                }
                                in_xml = new StringBuilder(in_xml.substring(0, in_xml.length() - 1));
                                in_xml.append("</attrib_list></arg-xml></cmdArgs></inArgs>");
                                StringBuilder sbGet = new StringBuilder();
                                boolean res = ((ActiveDeviceActivity)getActivity()).executeCommand(DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GET, in_xml.toString(), sbGet, scannerId);
                                Log.i(TAG, sbGet.toString());
                                outXML = sbGet.toString();

                                int iAttributeID = -1;
                                //String[] attrs = new String[Short.MAX_VALUE];
                                parser = Xml.newPullParser();

                                parser.setInput(new StringReader(outXML.trim()));
                                event = parser.getEventType();
                                text = null;
                                while (event != XmlPullParser.END_DOCUMENT) {
                                    String name = parser.getName();
                                    switch (event) {
                                        case XmlPullParser.START_TAG:
                                            break;
                                        case XmlPullParser.TEXT:
                                            text = parser.getText();
                                            break;

                                        case XmlPullParser.END_TAG:
                                            if (name.equals("id")) {
                                                iAttributeID = Integer.parseInt(text);
                                            } else if (name.equals("value")) {
                                                if(text!=null) {
                                                    for (Symbology symbology : symbologies) {
                                                        if (symbology.getRmdAttributeID() == iAttributeID) {
                                                            if (text.equalsIgnoreCase("true")) {
                                                                symbology.setEnabled(true);
                                                            } else {
                                                                symbology.setEnabled(false);
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                    }
                                    event = parser.next();
                                }

                                settingsFetched = true;
                            }

                        }

                    } catch (Exception e) {

                    } finally {

                    }
                }
            }
            return result;

        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            dismissDialog();
            if (!b) {
                if (opcode == DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_SET || opcode == DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_STORE) {
                    Toast.makeText(((ActiveDeviceActivity)getActivity()), "Symbology configuration failed", Toast.LENGTH_SHORT).show();
                    /* failed, return to previous value */
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((SwitchCompat) view).setChecked(((SwitchCompat) view).isChecked());
                        }
                    });
                } else if (opcode == DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GETALL) {
                    Toast.makeText(getActivity(), "Unable to retrieve supported  symbologies", Toast.LENGTH_SHORT).show();
					 /*RHBJ36 12.15.2015
					  *Failed to retrive supported symbologies. Finish Activity.
					 */
                    //finish();
                }
            } else {
                if (opcode == DCSSDKDefs.DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GETALL) {
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SwitchCompat switchCompat = (SwitchCompat) getView().findViewById(R.id.upcA);
                                switchCompat.setChecked(symbologies.get(0).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(0));


                                switchCompat = (SwitchCompat) getView().findViewById(R.id.upcE);
                                switchCompat.setChecked(symbologies.get(1).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(1));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.upcE1);
                                switchCompat.setChecked(symbologies.get(2).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(2));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.ean8);
                                switchCompat.setChecked(symbologies.get(3).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(3));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.ean13);
                                switchCompat.setChecked(symbologies.get(4).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(4));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.bookland);
                                switchCompat.setChecked(symbologies.get(5).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(5));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.code128);
                                switchCompat.setChecked(symbologies.get(6).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(6));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.ean128);
                                switchCompat.setChecked(symbologies.get(7).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(7));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.code39);
                                switchCompat.setChecked(symbologies.get(8).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(8));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.code93);
                                switchCompat.setChecked(symbologies.get(9).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(9));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.code11);
                                switchCompat.setChecked(symbologies.get(10).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(10));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.interleaved);
                                switchCompat.setChecked(symbologies.get(11).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(11));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.discrete);
                                switchCompat.setChecked(symbologies.get(12).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(12));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.chinese);
                                switchCompat.setChecked(symbologies.get(13).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(13));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.codabar);
                                switchCompat.setChecked(symbologies.get(14).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(14));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.msi);
                                switchCompat.setChecked(symbologies.get(15).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(15));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.code32);
                                switchCompat.setChecked(symbologies.get(16).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(16));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.dataMatrix);
                                switchCompat.setChecked(symbologies.get(17).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(17));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.pdf);
                                switchCompat.setChecked(symbologies.get(18).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(18));


                                switchCompat = (SwitchCompat) getView().findViewById(R.id.isbn);
                                switchCompat.setChecked(symbologies.get(19).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(19));

                                switchCompat = (SwitchCompat)  getView().findViewById(R.id.ucc_coupon_extended);
                                switchCompat.setChecked(symbologies.get(20).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(20));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.issn_ean);
                                switchCompat.setChecked(symbologies.get(21).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(21));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.isbt_128);
                                switchCompat.setChecked(symbologies.get(22).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(22));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.triopticCode39);
                                switchCompat.setChecked(symbologies.get(23).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(23));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.matrix2o5);
                                switchCompat.setChecked(symbologies.get(24).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(24));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.korean3o5);
                                switchCompat.setChecked(symbologies.get(25).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(25));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.gs1DataBar14);
                                switchCompat.setChecked(symbologies.get(26).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(26));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.gs1DatabarLimited);
                                switchCompat.setChecked(symbologies.get(27).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(27));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.gs1DatabarExpanded);
                                switchCompat.setChecked(symbologies.get(28).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(28));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.micropdf417);
                                switchCompat.setChecked(symbologies.get(29).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(29));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.maxicode);
                                switchCompat.setChecked(symbologies.get(30).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(30));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.qrCode);
                                switchCompat.setChecked(symbologies.get(31).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(31));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.aztec);
                                switchCompat.setChecked(symbologies.get(32).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(32));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.hanXinCode);
                                switchCompat.setChecked(symbologies.get(33).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(33));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.aupost);
                                switchCompat.setChecked(symbologies.get(34).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(34));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.us_planet);
                                switchCompat.setChecked(symbologies.get(35).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(35));

                                switchCompat = (SwitchCompat)  getView().findViewById(R.id.us_postnet);
                                switchCompat.setChecked(symbologies.get(36).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(36));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.kixCode);
                                switchCompat.setChecked(symbologies.get(37).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(37));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.usps4CB);
                                switchCompat.setChecked(symbologies.get(38).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(38));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.uk_postal);
                                switchCompat.setChecked(symbologies.get(39).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(39));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.japanPostal);
                                switchCompat.setChecked(symbologies.get(40).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(40));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.fics);
                                switchCompat.setChecked(symbologies.get(41).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(41));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.microQRCode);
                                switchCompat.setChecked(symbologies.get(42).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(42));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.compositec);
                                switchCompat.setChecked(symbologies.get(43).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(43));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.compositeAB);
                                switchCompat.setChecked(symbologies.get(44).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(44));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.tlc39);
                                switchCompat.setChecked(symbologies.get(45).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(45));

                                switchCompat = (SwitchCompat) getView().findViewById(R.id.dotcode);
                                switchCompat.setChecked(symbologies.get(46).isEnabled());
                                makeRowEnableDisable(switchCompat, symbologies.get(46));

                                registerCheckChangeListner();

                            }
                        });

                    }

                   settingsFetched = true;

                }
            }
        }

        private void makeRowEnableDisable(SwitchCompat switchCompat, Symbology symbology) {
            switchCompat.setEnabled(symbology.isSupported());

            final TextView txtTitle = (TextView)getView().findViewById(getTitleTextID(symbology.getSymbologyName()) );

            if(symbology.isSupported() && symbology.isEnabled()){
                txtTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.font_color));
            }else{
                txtTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.inactive_text));
            }
        }
    }
}
