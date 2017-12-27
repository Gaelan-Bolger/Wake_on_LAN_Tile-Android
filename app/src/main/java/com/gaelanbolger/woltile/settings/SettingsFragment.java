package com.gaelanbolger.woltile.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaelanbolger.woltile.BuildConfig;
import com.gaelanbolger.woltile.R;

import de.psdev.licensesdialog.LicensesDialog;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefs, rootKey);

        /* About Settings */
        findPreference("open_source_software").setOnPreferenceClickListener(preference -> {
            new LicensesDialog.Builder(getActivity())
                    .setNotices(R.raw.notices)
                    .setNoticesCssStyle(R.string.licenses_dialog_css)
                    .setTitle(R.string.open_source_software)
                    .build().showAppCompat();
            return true;
        });
        findPreference("app_version").setSummary(getString(R.string.app_version, BuildConfig.VERSION_NAME));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Remove dividers
        getListView().removeItemDecorationAt(0);

        return view;
    }
}
