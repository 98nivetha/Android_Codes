package com.zebra.demo.utility;
import android.app.Activity;
import android.widget.Toast;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.application.Application;
import com.zebra.demo.zebralib.rfidreader.rfid.RFIDController;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static String getCommonValue(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    public static void showToast(String message) {
        Toast.makeText(Application.getAppContext(), message, Toast.LENGTH_LONG).show();
    }

    public static String getAppDateFromServerDate(String serverDate) {
        String fromDateFormat = "yyyy-MM-dd\'T\'HH:mm:ss";
        String toDateFormat = "dd/MMM/yyyy";
        SimpleDateFormat sdf1 = new SimpleDateFormat(fromDateFormat);
        SimpleDateFormat sdf2 = new SimpleDateFormat(toDateFormat);
        try {
            return sdf2.format(sdf1.parse(serverDate));

        } catch (ParseException | NullPointerException e) {
            return "";
        }
    }

    public static String getAppDateFromServerDateOnly(String serverDate) {
        String fromDateFormat = "yyyy-MM-dd";
        String toDateFormat = "dd/MMM/yyyy";
        SimpleDateFormat sdf1 = new SimpleDateFormat(fromDateFormat);
        SimpleDateFormat sdf2 = new SimpleDateFormat(toDateFormat);
        try {
            return sdf2.format(sdf1.parse(serverDate));

        } catch (ParseException | NullPointerException e) {
            return "";
        }
    }

    public static String getCreateQuotationDateTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 10);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
        return sdf1.format(c.getTime()) + ".000Z";
    }

    public static String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        //c.add(Calendar.DATE, 10);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
        return sdf1.format(c.getTime()) + ".000Z";
    }

    public static String convertToFormat(float value) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(value);
    }

    public static String convertToFormat(double value) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(value);
    }

    public static int getInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    /**
     * method to start on trigger press event received
     */
    public static void triggerPressStartEventReceived(Activity activity) {
        try {
            if (activity != null && activity instanceof ActiveDeviceActivity && !RFIDController.mIsInventoryRunning) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ActiveDeviceActivity activeDeviceActivity = (ActiveDeviceActivity) activity;
                        if (activeDeviceActivity != null) {
                            activeDeviceActivity.inventoryStartOrStop(null);
                        }
                    }
                });
            }
        } catch (Exception e) {
        }
    }

    /**
     * method to stop on trigger release event received
     */
    public static void triggerReleaseStopEventReceived(Activity activity) {
        try {
            if (activity != null && activity instanceof ActiveDeviceActivity && (RFIDController.mIsInventoryRunning == true)) {
                //RFIDController.mInventoryStartPending = false;

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ActiveDeviceActivity activeDeviceActivity = (ActiveDeviceActivity) activity;
                        if (activeDeviceActivity != null) {
                            activeDeviceActivity.inventoryStartOrStop(null);
                        }
                    }
                });
            }
        } catch (Exception e) {
//            Utility.showToast("Exception2 : " + e);
        }
    }


    /**
     * method to start or stop
     */
    public static void startOrStopEventReceived(Activity activity) {
        try {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (activity != null && activity instanceof ActiveDeviceActivity) {
                        ActiveDeviceActivity activeDeviceActivity = (ActiveDeviceActivity) activity;
                        if (activeDeviceActivity != null) {
                            activeDeviceActivity.inventoryStartOrStop(null);
                        }
                    }
                }
            });
        } catch (Exception e) {
        }
    }


    public static boolean isValidIP(String ip) {
        final String IP_REGEX =
                "^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}" +
                        "(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])$";

        final Pattern IP_PATTERN = Pattern.compile(IP_REGEX);

        return IP_PATTERN.matcher(ip).matches();
    }

    public static boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDomain(String domain) {
        // Regular Expression for domain validation
        String domainRegex = "^(?!-)[A-Za-z0-9-]+(\\.[A-Za-z]{2,})+$";

        // Compile pattern
        Pattern pattern = Pattern.compile(domainRegex);
        Matcher matcher = pattern.matcher(domain);

        return matcher.matches();
    }

}
