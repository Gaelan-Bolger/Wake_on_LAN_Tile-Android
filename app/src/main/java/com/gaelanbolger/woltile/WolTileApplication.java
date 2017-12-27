package com.gaelanbolger.woltile;

import android.app.Application;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pddstudio.networkutils.NetworkUtils;

public class WolTileApplication extends Application {

    private static final String TAG = WolTileApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        NetworkUtils.initSingleton(this);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate: ");
    }
}
