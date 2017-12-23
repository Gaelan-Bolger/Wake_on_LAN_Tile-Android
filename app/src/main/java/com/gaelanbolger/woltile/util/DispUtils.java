package com.gaelanbolger.woltile.util;

import android.content.res.Resources;

public class DispUtils {

    public static int dp2px(int dp) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * dp);
    }
}
