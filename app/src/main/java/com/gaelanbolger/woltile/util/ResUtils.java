package com.gaelanbolger.woltile.util;

import android.content.Context;

public class ResUtils {

    public static int getResourceId(Context context, String name, String type) {
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }
}
