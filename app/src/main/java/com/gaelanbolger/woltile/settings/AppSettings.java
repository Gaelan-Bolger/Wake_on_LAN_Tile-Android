package com.gaelanbolger.woltile.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSettings {

    public static final String PACKET_COUNT = "packet_count";
    public static final String FAST_DISCOVER = "fast_discover";
    public static final String MT_DISCOVER = "mt_discover";

    private static SharedPreferences sPreferences;

    /* AppSetting Getters */

    public static int getPacketCount(Context context) {
        return getInt(context, PACKET_COUNT, 1);
    }

    public static boolean isFastDiscover(Context context) {
        return getBoolean(context, FAST_DISCOVER, true);
    }

    public static boolean isMtDiscover(Context context) {
        return getBoolean(context, MT_DISCOVER, true);
    }

    /* Generic Getters */

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSharedPreferences(context).getInt(key, defValue);
    }

    public static String getString(Context context, String key, String defValue) {
        return getSharedPreferences(context).getString(key, defValue);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sPreferences == null) {
            sPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sPreferences;
    }
}
