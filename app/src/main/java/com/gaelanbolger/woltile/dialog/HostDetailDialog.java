package com.gaelanbolger.woltile.dialog;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.gaelanbolger.woltile.R;
import com.gaelanbolger.woltile.adapter.TileIconAdapter;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.util.Bundler;
import com.gaelanbolger.woltile.util.NetUtils;

public class HostDetailDialog extends DialogFragment {

    public interface OnSaveListener {
        void onSave(Host host);
    }

    public static final String TAG = HostDetailDialog.class.getSimpleName();
    public static final String ARG_TITLE = "title";

    private Host mHost;
    private OnSaveListener mSavedListener;
    private TileIconAdapter mIconAdapter;
    private EditText mNameText;
    private EditText mIpAddressText;
    private EditText mMacAddressText;
    private RadioGroup mPortRadioGroup;

    public static HostDetailDialog newInstance(String title, Host host, OnSaveListener savedListener) {
        HostDetailDialog fragment = new HostDetailDialog();
        fragment.setArguments(new Bundler().with(ARG_TITLE, title).bundle());
        return fragment.setHost(host).setOnHostSavedListener(savedListener);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.dialog_host_detail, null);

        String icon = mHost.getIcon();
        String[] items = getTileIconOptions();
        String selectedItem = !TextUtils.isEmpty(icon) ? icon : items[0];
        mIconAdapter = new TileIconAdapter(view.getContext(), items, selectedItem);
        RecyclerView iconRecycler = view.findViewById(R.id.rv_tile_icon);
        iconRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        iconRecycler.setAdapter(mIconAdapter);

        mNameText = view.findViewById(R.id.et_host_name);
        mNameText.setText(mHost.getName());

        mIpAddressText = view.findViewById(R.id.et_ip_address);
        mIpAddressText.setText(mHost.getIp());

        mMacAddressText = view.findViewById(R.id.et_mac_address);
        mMacAddressText.setText(mHost.getMac());

        mPortRadioGroup = view.findViewById(R.id.rg_port);
        mPortRadioGroup.check(mHost.getPort() == Host.DEFAULT_PORT ? R.id.rb_port_9 : R.id.rb_port_7);

        Bundle args = getArguments();
        String title = args != null ? args.getString(ARG_TITLE) : null;
        AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                .setView(view)
                .setTitle(!TextUtils.isEmpty(title) ? title : getString(R.string.host))
                .setNegativeButton(android.R.string.cancel, (d, w) -> d.dismiss())
                .setPositiveButton(android.R.string.ok, (d, w) -> { /* overridden in onShow */ })
                .create();
        dialog.setOnShowListener(d -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(b -> {
                String name = mNameText.getText().toString().trim();
                String ip = mIpAddressText.getText().toString().trim();
                String mac = mMacAddressText.getText().toString().trim();
                if (isValidHost(name, ip, mac)) {
                    mHost.setName(name);
                    mHost.setIp(ip);
                    mHost.setMac(mac);
                    mHost.setPort(mPortRadioGroup.getCheckedRadioButtonId() == R.id.rb_port_7 ? 7 : 9);
                    mHost.setIcon(mIconAdapter.getSelectedItem());
                    if (mSavedListener != null) {
                        mSavedListener.onSave(mHost);
                    }
                    dismiss();
                }
            });
        });
        return dialog;
    }

    public HostDetailDialog setHost(Host host) {
        mHost = host;
        return this;
    }

    public HostDetailDialog setOnHostSavedListener(OnSaveListener savedListener) {
        mSavedListener = savedListener;
        return this;
    }

    private String[] getTileIconOptions() {
        TypedArray ta = getResources().obtainTypedArray(R.array.tile_icon_option_names);
        int length = ta.length();
        String[] items = new String[length];
        for (int i = 0; i < length; i++)
            items[i] = ta.getString(i);
        ta.recycle();
        return items;
    }

    private boolean isValidHost(String name, String ip, String mac) {
        boolean valid = true;
        if (TextUtils.isEmpty(name)) {
            mNameText.setError("Host Name is required");
            valid = false;
        }
        if (TextUtils.isEmpty(ip)) {
            mIpAddressText.setError("IP Address is required");
            valid = false;
        } else if (!NetUtils.IpUtils.isValid(ip)) {
            mIpAddressText.setError("Invalid IP Address");
            valid = false;
        }
        if (TextUtils.isEmpty(mac)) {
            mMacAddressText.setError("MAC Address is required");
            valid = false;
        } else if (!NetUtils.MacUtils.isValid(mac)) {
            mMacAddressText.setError("Invalid MAC Address");
            valid = false;
        }
        return valid;
    }
}
