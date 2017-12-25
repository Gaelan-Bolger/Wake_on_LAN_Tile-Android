package com.gaelanbolger.woltile;

import android.app.Application;
import android.util.Log;

import com.gaelanbolger.woltile.data.AppDatabase;
import com.gaelanbolger.woltile.data.Host;


public class WolTileApplication extends Application {

    private static final String TAG = WolTileApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        initializeDatabase();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate: ");
    }

    private void initializeDatabase() {
        Log.d(TAG, "initializeDatabase: ");
        AppDatabase db = AppDatabase.getInstance(this);
        int count = db.hostDao().count();
        for (int i = 0; i < 12 - count; i++) {
            db.hostDao().insertAll(new Host());
        }
    }
}
