package com.gaelanbolger.woltile;

import android.app.Application;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.pddstudio.networkutils.NetworkUtils;

public class WolTileApplication extends Application {

    private static final String TAG = WolTileApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        NetworkUtils.initSingleton(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate: ");
    }
}
