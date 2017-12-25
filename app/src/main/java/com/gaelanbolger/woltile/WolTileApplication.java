package com.gaelanbolger.woltile;

import android.app.Application;
import android.util.Log;

public class WolTileApplication extends Application {

    private static final String TAG = WolTileApplication.class.getSimpleName();

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
