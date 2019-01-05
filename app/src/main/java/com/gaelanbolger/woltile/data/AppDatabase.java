package com.gaelanbolger.woltile.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Host.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "app";

    private static AppDatabase sInstance;
    private static Executor sDiskIO;

    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null)
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DB_NAME).build();
        return sInstance;
    }

    public static synchronized Executor io() {
        if (sDiskIO == null)
            sDiskIO = Executors.newSingleThreadExecutor();
        return sDiskIO;
    }

    public abstract HostDao hostDao();
}
