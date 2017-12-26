package com.gaelanbolger.woltile.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.preference.PreferenceCategory;

import com.gaelanbolger.woltile.R;

public class SettingsCategory extends PreferenceCategory {

    public SettingsCategory(Context context) {
        this(context, null);
    }

    public SettingsCategory(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.preference_category_layout);
    }

    @Override
    public boolean isSelectable() {
        return false;
    }
}
