package com.zebra.demo;/*

import android.app.Application;

*/
/*JiniProductApp*//*

public class AssetManagementApp extends Application {

    private static AssetManagementApp sInstance;


    public static AssetManagementApp getAppContext() {
        return sInstance;
    }

    private static synchronized void setInstance(AssetManagementApp app) {
        sInstance = app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
    }
}
*/


/*
import android.app.Application;
import org.acra.ACRA;
import org.acra.annotation.AcraMailSender;

@AcraMailSender(mailTo = "sathishs27@live.com")
public class AssetManagementApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
    }
}*/
