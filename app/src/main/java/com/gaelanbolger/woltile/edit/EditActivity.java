package com.gaelanbolger.woltile.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.gaelanbolger.woltile.R;
import com.gaelanbolger.woltile.data.AppDatabase;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.discover.DiscoverActivity;
import com.gaelanbolger.woltile.qs.TileComponent;
import com.gaelanbolger.woltile.util.NetworkUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static butterknife.ButterKnife.bind;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = EditActivity.class.getSimpleName();
    private static final int REQ_DISC_HOST = 1028;
    public static final String EXTRA_TILE_COMPONENT = "tile_component";

    private TileComponent mTileComponent;
    private Host mHost;
    private AppDatabase mDatabase;
    private TileIconAdapter mIconAdapter;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @BindView(R.id.rv_tile_icon)
    RecyclerView mIconRecycler;
    @BindView(R.id.et_host_name)
    EditText mHostNameText;
    @BindView(R.id.et_ip_address)
    EditText mIpAddressText;
    @BindView(R.id.et_mac_address)
    EditText mMacAddressText;
    @BindView(R.id.rg_port)
    RadioGroup mPortGroup;
    @BindView(R.id.et_port_number)
    EditText mPortNumberText;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("icon_name", mIconAdapter.getSelectedItem());
        outState.putString("host_name", mHostNameText.getText().toString());
        outState.putString("ip_address", mIpAddressText.getText().toString());
        outState.putString("mac_address", mMacAddressText.getText().toString());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_TILE_COMPONENT))
            throw new IllegalArgumentException("Must provide a TileComponent name with Intent");
        String tileComponentName = intent.getStringExtra(EXTRA_TILE_COMPONENT);
        mTileComponent = TileComponent.valueOf(tileComponentName);
        mHost = new Host(mTileComponent.ordinal());

        mDatabase = AppDatabase.getInstance(this);
        if (savedInstanceState == null) {
            mDisposable.add(mDatabase.hostDao().getByIdRx(mHost.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            host -> {
                                mHost = host;
                                updateViews();
                            },
                            throwable -> Log.e(TAG, "onStart: Error retrieving host", throwable)
                    ));
        } else {
            mHost.setIcon(savedInstanceState.getString("icon_name"));
            mHost.setName(savedInstanceState.getString("host_name"));
            mHost.setIp(savedInstanceState.getString("ip_address"));
            mHost.setMac(savedInstanceState.getString("mac_address"));
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(mTileComponent.getTitleResId());
        }

        setContentView(R.layout.activity_edit);
        bind(this);
        updateViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_DISC_HOST) {
            if (resultCode == RESULT_OK) {
                mHostNameText.setText(data.getStringExtra(DiscoverActivity.EXTRA_HOST_NAME));
                mIpAddressText.setText(data.getStringExtra(DiscoverActivity.EXTRA_IP_ADDRESS));
                mMacAddressText.setText(data.getStringExtra(DiscoverActivity.EXTRA_MAC_ADDRESS));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_tile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                onBackPressed();
                return true;
            case R.id.item_save:
                onSaveHost();
                return true;
            case R.id.item_discover:
                onDiscoverHosts(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateViews() {
        String hostIcon = mHost.getIcon();
        String[] iconItems = getResources().getStringArray(R.array.tile_icon_option_names);
        String selectedItem = !TextUtils.isEmpty(hostIcon) ? hostIcon : iconItems[0];
        mIconAdapter = new TileIconAdapter(this, iconItems, selectedItem, null);
        mIconRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mIconRecycler.setAdapter(mIconAdapter);
        mHostNameText.setText(mHost.getName());
        mIpAddressText.setText(mHost.getIp());
        mMacAddressText.setText(mHost.getMac());
        switch (mHost.getPort()) {
            case 7:
                mPortGroup.check(R.id.rb_port_7);
                break;
            case 9:
                mPortGroup.check(R.id.rb_port_9);
                break;
            default:
                mPortGroup.check(R.id.rb_port_user);
                mPortNumberText.setText(String.valueOf(mHost.getPort()));
                mPortNumberText.clearFocus();
                break;
        }
    }

    @OnCheckedChanged({R.id.rb_port_7, R.id.rb_port_9, R.id.rb_port_user})
    public void onPortChanged(CompoundButton button, boolean checked) {
        if (!checked) return;
        switch (button.getId()) {
            case R.id.rb_port_user:
                mPortNumberText.setEnabled(true);
                mPortNumberText.setSelectAllOnFocus(true);
                mPortNumberText.requestFocus();
                break;
            default:
                mPortNumberText.setEnabled(false);
                mPortNumberText.clearFocus();
                break;
        }
    }

    public void onDiscoverHosts(View view) {
        Intent intent = new Intent(this, DiscoverActivity.class);
        startActivityForResult(intent, REQ_DISC_HOST);
    }

    private void onSaveHost() {
        String hostName = mHostNameText.getText().toString().trim();
        String ipAddress = mIpAddressText.getText().toString().trim();
        String macAddress = mMacAddressText.getText().toString().trim();
        int port = getSelectedPort(mPortGroup.getCheckedRadioButtonId());
        if (isValidHost(hostName, ipAddress, macAddress, port)) {
            mHost.setName(hostName);
            mHost.setIp(ipAddress);
            mHost.setMac(macAddress);
            mHost.setPort(port);
            mHost.setIcon(mIconAdapter.getSelectedItem());
            AppDatabase.io().execute(() -> mDatabase.hostDao().insertAll(mHost));

            Intent data = new Intent();
            data.putExtra(EXTRA_TILE_COMPONENT, mTileComponent.name());
            setResult(RESULT_OK, data);
            finish();
        }
    }

    private boolean isValidHost(String name, String ip, String mac, int port) {
        boolean valid = true;
        if (TextUtils.isEmpty(name)) {
            mHostNameText.setError("Host Name is required");
            valid = false;
        }
        if (TextUtils.isEmpty(ip)) {
            mIpAddressText.setError("IP Address is required");
            valid = false;
        } else if (!NetworkUtils.IpUtils.isValid(ip)) {
            mIpAddressText.setError("Invalid IP Address");
            valid = false;
        }
        if (TextUtils.isEmpty(mac)) {
            mMacAddressText.setError("MAC Address is required");
            valid = false;
        } else if (!NetworkUtils.MacUtils.isValid(mac)) {
            mMacAddressText.setError("Invalid MAC Address");
            valid = false;
        }
        if (port < 1 || port > 65535) {
            mPortNumberText.setError("1-65535");
            valid = false;
        }
        return valid;
    }

    private int getSelectedPort(int checkedId) {
        int port = 0;
        switch (checkedId) {
            case R.id.rb_port_7:
                port = 7;
                break;
            case R.id.rb_port_9:
                port = 9;
                break;
            case R.id.rb_port_user:
                String s = mPortNumberText.getText().toString().trim();
                try {
                    port = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "getSelectedPort: Un-parsable port number: " + s);
                    mPortNumberText.setError("1-65535");
                }
                break;
        }
        return port;
    }
}
