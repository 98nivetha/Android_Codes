package com.zebra.demo.zebralib.application;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.media.ToneGenerator;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Handler;

import com.zebra.demo.BuildConfig;
import com.zebra.demo.zebralib.rfidreader.common.MaxLimitArrayList;
import com.zebra.demo.zebralib.rfidreader.common.PreFilters;
import com.zebra.demo.zebralib.rfidreader.home.RFIDBaseActivity;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import com.zebra.demo.zebralib.rfidreader.reader_connection.ScanPair;
import com.zebra.demo.zebralib.rfidreader.settings.ProfileContent;
import com.zebra.demo.zebralib.scanner.helpers.AvailableScanner;
import com.zebra.demo.zebralib.scanner.helpers.Barcode;
import com.zebra.demo.zebralib.scanner.helpers.Foreground;
import com.zebra.demo.zebralib.scanner.helpers.ScannerAppEngine;
import com.zebra.rfid.api3.Antennas;
import com.zebra.rfid.api3.BEEPER_VOLUME;
import com.zebra.rfid.api3.DYNAMIC_POWER_OPTIMIZATION;
import com.zebra.rfid.api3.IEvents.BatteryData;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.RFModeTable;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.RegulatoryConfig;
import com.zebra.rfid.api3.StartTrigger;
import com.zebra.rfid.api3.StopTrigger;
import com.zebra.rfid.api3.TagStorageSettings;
import com.zebra.rfid.api3.UNIQUE_TAG_REPORT_SETTING;
import com.zebra.scannercontrol.DCSScannerInfo;
import com.zebra.scannercontrol.SDKHandler;

import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.Log;

import com.zebra.demo.zebralib.rfidreader.locate_tag.multitag_locate.MultiTagLocateListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Created by qvfr34 on 12/31/2015.
 */

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;



/*public class AssetManagementApp extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
    }
}*/

//@AcraMailSender(mailTo = "sathishs27@live.com")
public class Application extends android.app.Application {
    public static final int DEVICE_STD_MODE = 3;
    public static final int DEVICE_PREMIUM_PLUS_MODE = 4;
    public static final long AUTORECONNECT_DELAY = 3000;
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    public static int RFD_DEVICE_MODE = 3;

    public static RFIDReader mConnectedReader;

    //Variable to keep track of the unique tags seen
    public static volatile int UNIQUE_TAGS = 0;

    //Variable to keep track of the unique tags when matching tags CSV is enabled. (value=UNIQUE_TAGS+missing tags).
    public static volatile int UNIQUE_TAGS_CSV = 0;

    //variable to keep track of the total tags seen
    public static volatile int TOTAL_TAGS = 0;
    //Arraylist to keeptrack of the tagIDs to act as adapter for autocomplete text views
    public static ArrayList<String> tagIDs;
    //variable to store the tag read rate
    public static volatile int TAG_READ_RATE = 0;
    //Boolean to keep track of whether the inventory is running or not
    public static volatile boolean mIsInventoryRunning;
    public static volatile boolean mInventoryStartPending;
    public static int inventoryMode = 0;
    public static Boolean isBatchModeInventoryRunning;
    public static int memoryBankId = -1;
    public static String accessControlTag;
    public static String locateTag;
    public static String PreFilterTag;
    //Variable to maintain the RR started time to maintain the read rate
    public static volatile long mRRStartedTime;
    public static PreFilters[] preFilters = null;
    public static boolean isAccessCriteriaRead = false;
    public static int preFilterIndex = -1;
    //For Notification
    public static volatile int INTENT_ID = 100;
    public static RFIDBaseActivity.EventHandler RFIDBAseEventHandler;
    public static TreeMap<String, Integer> inventoryList = new TreeMap<String, Integer>();
    public static HashMap<String, String> versionInfo = new HashMap<>(5);

    //Arraylist to keeptrack of the tags read for Inventory
    public static ArrayList<InventoryListItem> tagsReadInventory = new MaxLimitArrayList();

    //Arraylist to store the tags from CSV file
    public static ArrayList<InventoryListItem> tagsListCSV = new MaxLimitArrayList();
    public static ArrayList<InventoryListItem> matchingTagsList = new MaxLimitArrayList();
    public static ArrayList<InventoryListItem> missingTagsList = new MaxLimitArrayList();
    public static ArrayList<InventoryListItem> unknownTagsList = new MaxLimitArrayList();
    public static ArrayList<InventoryListItem> tagsReadForSearch = new MaxLimitArrayList();

    public static volatile boolean TAG_LIST_MATCH_MODE = false;
    public static volatile boolean TAG_LIST_FILE_EXISTS = false;
    public static boolean tagListMatchAutoStop = true;
    public static boolean tagListMatchNotice = false;
    public static TreeMap<String, Integer> tagListMap = new TreeMap<String, Integer>();
    public static int missedTags = 0;
    public static int matchingTags = 0;

    public static boolean isGettingTags;
    public static boolean EXPORT_DATA;
    public static ReaderDevice mConnectedDevice;
    public static BluetoothDevice BTDevice;
    public static boolean isLocatingTag;
    public static ScanPair scanPair = null;

    public static String importFileName = "";
    //Variable to multiTagLocate operation
    public static volatile boolean mIsMultiTagLocatingRunning;
    public static boolean multiTagLocateTagListExist = false;
    public static boolean multiTagLocatelastTag = false;
    public static boolean MultiTagInventoryMultiSelect = false;
    public static boolean MultiTagLocateTagListImport = false;
    public static TreeMap<String, MultiTagLocateListItem> multiTagLocateTagListMap = new TreeMap<String, MultiTagLocateListItem>();
    public static ArrayList<MultiTagLocateListItem> multiTagLocateActiveTagItemList = new ArrayList<MultiTagLocateListItem>();
    //Arraylist to keeptrack of the tagIDs to act as adapter for multiTagLocate autocomplete text views
    public static ArrayMap<String, String> multiTagLocateTagMap = new ArrayMap<String, String>();
    public static ArrayList<String> multiTagLocateTagIDs = new ArrayList<String>();
    //
    public static StartTrigger settings_startTrigger;
    public static StopTrigger settings_stopTrigger;
    public static short TagProximityPercent = -1;
    public static TagStorageSettings tagStorageSettings;
    public static int batchMode;
    public static BatteryData BatteryData = null;
    public static DYNAMIC_POWER_OPTIMIZATION dynamicPowerSettings;
    public static boolean is_disconnection_requested;
    public static boolean is_connection_requested;
    //Application Settings
    public static volatile boolean AUTO_DETECT_READERS;
    public static volatile boolean AUTO_RECONNECT_READERS;
    public static volatile boolean NOTIFY_READER_AVAILABLE;
    public static volatile boolean NOTIFY_READER_CONNECTION;
    public static volatile boolean NOTIFY_BATTERY_STATUS;
    //MultiTag Locate Settings
    public static volatile boolean MULTI_TAG_LOCATE_SORT;
    public static int MULTI_TAG_LOCATE_FOUND_PROXI_PERCENT;
    public static String LAST_CONNECTED_READER = "";
    //Beeper
    public static BEEPER_VOLUME beeperVolume = BEEPER_VOLUME.HIGH_BEEP;
    public static BEEPER_VOLUME sledBeeperVolume = BEEPER_VOLUME.HIGH_BEEP;
    // Singulation control
    public static Antennas.SingulationControl singulationControl;
    // regulatory
    public static RegulatoryConfig regulatory;
    public static Boolean regionNotSet = false;
    // antenna
    public static RFModeTable rfModeTable;
    public static Antennas.AntennaRfConfig antennaRfConfig;
    public static int[] antennaPowerLevel;
    public static Readers readers;
    //Variable to keep track of the unique tags seen

    public static volatile boolean TAG_LIST_LOADED = false;

    public static String strBrandID = "AAAA";
    public static int strBrandIDLogo;
    public static int iUpdateLogo = 0;
    public static int iBrandIDLen = 12;
    public static boolean bBrandCheckStarted = false;
    public static BluetoothDevice latestUnPairedBTDevice;
    public static boolean settingsactivityResumed;
    public static ReaderDevice mReaderDisappeared;
    public static ToneGenerator toneGenerator;
    public static Activity contextSettingDetails = null;
    public static String currentFragment = "";   //for MTC export data, when curr frag is rr it should export as previously.
    public static boolean SHOW_CSV_TAG_NAMES = false;
    public static boolean asciiMode = false;
    public static ProfileContent.ProfilesItem ActiveProfile;
    public static String PreFilterTagID;
    public static boolean NON_MATCHING = false;
    public static String RFID_DATAWEDGE_PROFILE_CREATION = "RFID_DATAWEDGE_PROFILE_CREATION";
    public static String RFID_DATAWEDGE_ENABLE_SCANNER = "RFID_DATAWEDGE_ENABLE_SCANNER";
    public static String RFID_DATAWEDGE_DISABLE_SCANNER = "RFID_DATAWEDGE_DISABLE_SCANNER";
    public static UNIQUE_TAG_REPORT_SETTING reportUniquetags = null;
    public static boolean ledState = false;
    public static int beeperspinner_status;
    public static String TAG = "RFIDDEMO";
    public static boolean brandidcheckenabled = false;
    public static String strCurrentImage = "";
    public static boolean bFound = false;
    public static String packageName;
    public static boolean isReaderConnectedThroughBluetooth = false;
    public static AvailableScanner curAvailableScanner = null;
    public static boolean updateReaderConnection = false;
    public static int mSelectedItem = -1;
    public static int keyLayoutType = -1;
    private static boolean activityVisible;
    public static String prevPairData = "";

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    //Instance of SDK Handler
    public static SDKHandler sdkHandler;

    //Handler to handle bluetooth events
    public static Handler globalMsgHandler;

    //Var to access scanner app engine
    public static ScannerAppEngine scannerAppEngine;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public static String cycleCountProfileData = null;
    public static final String CACHE_TAGLIST_MATCH_MODE_FILE = ".cacheMatchModeTagFile.csv";  // cache file for taglist match mode
    public static final String CACHE_LOCATE_TAG_FILE = ".cacheLocateTagFile.csv";  // cache file for locate tag

    //Settings for notifications
    public static int MOT_SETTING_OPMODE;
    public static boolean MOT_SETTING_SCANNER_DETECTION;
    public static boolean MOT_SETTING_EVENT_ACTIVE;
    public static boolean MOT_SETTING_EVENT_AVAILABLE;
    public static boolean MOT_SETTING_EVENT_BARCODE;
    public static boolean MOT_SETTING_EVENT_IMAGE;
    public static boolean MOT_SETTING_EVENT_VIDEO;
    public static boolean MOT_SETTING_EVENT_BINARY_DATA;

    public static boolean MOT_SETTING_NOTIFICATION_ACTIVE;
    public static boolean MOT_SETTING_NOTIFICATION_AVAILABLE;
    public static boolean MOT_SETTING_NOTIFICATION_BARCODE;
    public static boolean MOT_SETTING_NOTIFICATION_IMAGE;
    public static boolean MOT_SETTING_NOTIFICATION_VIDEO;
    public static boolean MOT_SETTING_NOTIFICATION_BINARY_DATA;

    //public static volatile int INTENT_ID = 0xFFFF;

    public static int SCANNER_ID_NONE = -1;
    public static String currentScannerName = "";
    public static String currentScannerAddress = "";
    public static int currentScannerId = SCANNER_ID_NONE;
    public static boolean currentAutoReconnectionState = true;
    public static boolean isAnyScannerConnected = false; //True, if currently connected to any scanner
    public static int currentConnectedScannerID = -1; //Track scannerId of currently connected Scanner
    public static boolean isFirmwareUpdateInProgress = false;
    public static boolean isFirmwareUpdateSuccess = true;
    //Scanners (both available and active)
    public static ArrayList<DCSScannerInfo> mScannerInfoList = new ArrayList<DCSScannerInfo>();
    public static ArrayList<ScannerAppEngine.IScannerAppEngineDevListDelegate> mDevListDelegates = new ArrayList<ScannerAppEngine.IScannerAppEngineDevListDelegate>();
    //Barcode data
    //public static ArrayList<Barcode> barcodeData = new ArrayList<Barcode>();
    public static DCSScannerInfo currentConnectedScanner;
    public static DCSScannerInfo lastConnectedScanner;
    public static boolean showAdvancedOptions = false;
    public static boolean rwAdvancedOptions = false;


    public static int minScreenWidth = 360;

    private static Application sInstance;


    public static Application getAppContext() {
        return sInstance;
    }

    private static synchronized void setInstance(Application app) {
        sInstance = app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Configure ACRA with mail sender
        CoreConfigurationBuilder builder = new CoreConfigurationBuilder()
                .withPluginConfigurations(
                        new MailSenderConfigurationBuilder()
                                .withMailTo("") // Replace with your email //sathishs27@live.com
                                .withReportAsFile(true)
                                .withReportFileName("crash_report.txt")
                                .build()
                );
        ACRA.init(this, builder);




        setInstance(this);
        Foreground.init(this);
        //this keyword referring to Context of the sample application
        sdkHandler = new SDKHandler(this, false);
    }

    /**
     * Update the tagIds from tagsReadInventory
     */
    public static void updateTagIDs() {
        if (tagsReadInventory == null)
            return;

        if (tagsReadInventory.size() == 0)
            return;

        if (tagIDs == null) {
            tagIDs = new ArrayList<>();
            for (InventoryListItem i : tagsReadInventory) {
                tagIDs.add(i.getTagID());
            }
        } else if (tagIDs.size() != tagsReadInventory.size()) {
            tagIDs.clear();
            for (InventoryListItem i : tagsReadInventory) {
                tagIDs.add(i.getTagID());
            }
        }/*else{
            //Do Nothing. Array is up to date
        }*/
    }

    //clear saved data
    public static void reset() {

        UNIQUE_TAGS = 0;
        UNIQUE_TAGS_CSV = 0;
        TOTAL_TAGS = 0;
        TAG_READ_RATE = 0;
        mRRStartedTime = 0;
        missedTags = 0;
        matchingTags = 0;

        if (tagsReadInventory != null)
            tagsReadInventory.clear();
        if (tagIDs != null)
            tagIDs.clear();

        if (Application.TAG_LIST_MATCH_MODE) {
            Application.matchingTagsList.clear();
            Application.missingTagsList.clear();
            Application.unknownTagsList.clear();
            Application.tagsReadForSearch.clear();
        }

        mIsInventoryRunning = false;
        inventoryMode = 0;
        memoryBankId = -1;
        if (inventoryList != null)
            inventoryList.clear();

        mConnectedDevice = null;

        INTENT_ID = 100;
        antennaPowerLevel = null;

        //Triggers
        settings_startTrigger = null;
        settings_startTrigger = null;

        //Beeper
        beeperVolume = BEEPER_VOLUME.HIGH_BEEP;

        accessControlTag = null;
        isAccessCriteriaRead = false;

        // reader settings
        regulatory = null;
        regionNotSet = false;

        preFilters = null;
        preFilterIndex = -1;
        PreFilterTag = "";
        PreFilterTagID = "";

        settings_startTrigger = null;
        settings_stopTrigger = null;

        if (versionInfo != null)
            versionInfo.clear();

        BatteryData = null;

        isLocatingTag = false;
        mIsMultiTagLocatingRunning = false;
        TagProximityPercent = -1;
        locateTag = null;
        is_disconnection_requested = false;
        is_connection_requested = false;
        readers = null;
    }

    public int getTabCount(ReaderDevice readerDevice) {

        String ScannerVersionInfo = Application.versionInfo.get("PL33");
        if (readerDevice.getDeviceCapability(readerDevice.getName()).equals("PREMIUM PLUS(WiFi & SCAN)")) {
            return DEVICE_PREMIUM_PLUS_MODE;
        } else if (readerDevice.getName().startsWith("RFD8500")) {
            if((ScannerVersionInfo == null) || ScannerVersionInfo.equals("")) {
                return DEVICE_STD_MODE;
            }else{
                return DEVICE_PREMIUM_PLUS_MODE;
            }
        }
        return DEVICE_STD_MODE;

    }


    public String processNFCData(Intent inputIntent) {
    String  nfcData = null;
        Log.i(TAG, "processNFCData");
        Parcelable[] rawMessages =
                inputIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (rawMessages != null && rawMessages.length > 0) {

            NdefMessage[] messages = new NdefMessage[rawMessages.length];

            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }

            Log.i(TAG, "message size = " + messages.length);

            //TextView veiw = findViewById(R.id.viewdata);
            ///if ( veiw != null ) {
            // only one message sent during the beam
            NdefMessage msg = (NdefMessage) rawMessages[0];
            // record 0 contains the MIME type, record 1 is the AAR, if present

            String BtAddress;
            byte[] b = msg.getRecords()[0].getPayload();

            BtAddress = getNFCBTAddress(b);
            Log.i(TAG, "NFC btAddress = " + BtAddress);
            if (BtAddress == null) {
                String base = new String(msg.getRecords()[0].getPayload());
                String Type = new String(msg.getRecords()[0].getType());
                String str = String.format(Locale.getDefault(), "Message entries=%d. Base message is %s msgType is %s", rawMessages.length, base, Type);

                String[] basemsg = base.split("_");
                if (basemsg.length > 0)
                    nfcData = basemsg[1];

            } else {
                nfcData = BtAddress;
            }
        }
        return nfcData;
    }

    private String getNFCBTAddress(byte[] b) {
        char[] chars = new char[2 * b.length];
        for (int i = 0; i < b.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(b[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[b[i] & 0x0F];
        }
        String ch = new String(chars);
        String subStr ;

        if(!ch.startsWith("1f00"))  return null;
        subStr = ch.substring(4, 16);

        for (int s = 0 ; s < subStr.length() ; s++) {
            subStr = swap(subStr, s, s + 1);
            s++;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(subStr);
        stringBuilder.reverse();
        subStr = stringBuilder.toString();
        return subStr;
    }

    static String swap(String str, int i, int j)
    {
        char ch[] = str.toCharArray();
        char temp = ch[i];
        ch[i] = ch[j];
        ch[j] = temp;

        return String.copyValueOf(ch);
    }


}
