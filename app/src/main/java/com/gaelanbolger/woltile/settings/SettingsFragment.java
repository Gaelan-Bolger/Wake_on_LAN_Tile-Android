package com.gaelanbolger.woltile.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.gaelanbolger.woltile.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefs, rootKey);
    }
}
