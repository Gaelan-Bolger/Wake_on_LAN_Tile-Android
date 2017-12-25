package com.gaelanbolger.woltile.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.gaelanbolger.woltile.data.Host;

public class DiscoverActivity extends AppCompatActivity implements DiscoverFragment.Listener {

    public static final String EXTRA_HOST_NAME = "host_name";
    public static final String EXTRA_IP_ADDRESS = "ip_address";
    public static final String EXTRA_MAC_ADDRESS = "mac_address";

    public static void start(Context context) {
        Intent starter = new Intent(context, DiscoverActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        FragmentManager fm = getSupportFragmentManager();
        DiscoverFragment f = (DiscoverFragment) fm.findFragmentByTag(DiscoverFragment.TAG);
        if (f == null) f = new DiscoverFragment();
        fm.beginTransaction().replace(android.R.id.content, f, DiscoverFragment.TAG).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHostSelected(Host host) {
        Intent data = new Intent();
        data.putExtra(EXTRA_HOST_NAME, host.getName());
        data.putExtra(EXTRA_IP_ADDRESS, host.getIp());
        data.putExtra(EXTRA_MAC_ADDRESS, host.getMac());
        setResult(RESULT_OK, data);
        finish();
    }
}
