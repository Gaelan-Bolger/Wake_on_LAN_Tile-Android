package com.gaelanbolger.woltile.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

public class ResourceUtils {

    public static Drawable getDrawable(Context context, int resId) {
        return ContextCompat.getDrawable(context, resId);
    }

    public static int getDrawableForName(@NonNull Context context, @Nullable String name) {
        return getResourceId(context, name, "drawable");
    }

    public static int getResourceId(@NonNull Context context, @Nullable String name, String type) {
        if (TextUtils.isEmpty(name)) return 0;
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }
}
