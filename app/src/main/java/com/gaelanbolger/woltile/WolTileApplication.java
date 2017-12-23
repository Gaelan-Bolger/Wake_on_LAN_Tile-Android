package com.gaelanbolger.woltile;

import android.app.Application;
import android.util.Log;


public class WolTileApplication extends Application {

    private static final String TAG = WolTileApplication.class.getSimpleName();
    public static final String PREF_HOST_NAME = "host_name";
    public static final String PREF_IP_ADDRESS = "ip_address";
    public static final String PREF_MAC_ADDRESS = "mac_address";
    public static final String PREF_PORT = "port";
    public static final String PREF_ICON = "icon";
    public static final int DEFAULT_PORT = 9;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate: ");
    }
}
