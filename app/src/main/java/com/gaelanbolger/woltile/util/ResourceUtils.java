package com.gaelanbolger.woltile.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

public class ResourceUtils {

    public static int getDrawableForName(Context context, String name) {
        return getResourceId(context, name, "drawable");
    }

    public static int getResourceId(Context context, String name, String type) {
        if (TextUtils.isEmpty(name)) return 0;
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }
}
