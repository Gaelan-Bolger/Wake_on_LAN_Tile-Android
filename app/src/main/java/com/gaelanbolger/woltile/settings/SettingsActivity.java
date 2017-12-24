package com.gaelanbolger.woltile.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.gaelanbolger.woltile.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        SettingsFragment settingsFragment = (SettingsFragment) fm.findFragmentByTag(SettingsFragment.TAG);
        if (settingsFragment == null) settingsFragment = SettingsFragment.newInstance();
        fm.beginTransaction().replace(android.R.id.content, settingsFragment, SettingsFragment.TAG).commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        public static final String TAG = SettingsFragment.class.getSimpleName();

        public static SettingsFragment newInstance() {
            return new SettingsFragment();
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.prefs, rootKey);
        }
    }
}
