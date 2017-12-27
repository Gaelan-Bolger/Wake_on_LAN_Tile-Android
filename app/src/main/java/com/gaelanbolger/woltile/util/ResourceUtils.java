package com.gaelanbolger.woltile.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class ResourceUtils {

    public static int getDrawableForName(@NonNull Context context, @Nullable String name) {
        return getResourceId(context, name, "drawable");
    }

    public static int getResourceId(@NonNull Context context, @Nullable String name, String type) {
        if (TextUtils.isEmpty(name)) return 0;
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }
}
