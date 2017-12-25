package com.gaelanbolger.woltile.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Host.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract HostDao hostDao();

    private static AppDatabase sInstance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app")
                    .allowMainThreadQueries() // TODO remove in prod
                    .build();
        }
        return sInstance;
    }
}
