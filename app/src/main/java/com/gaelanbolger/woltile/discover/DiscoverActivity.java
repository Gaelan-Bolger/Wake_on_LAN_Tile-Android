package com.gaelanbolger.woltile.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.gaelanbolger.woltile.R;
import com.gaelanbolger.woltile.data.AppDatabase;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.dialog.HostDetailDialog;

public class DiscoverActivity extends AppCompatActivity implements DiscoverFragment.Listener {

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
        String title = getString(R.string.new_host);
        HostDetailDialog dialog = HostDetailDialog.newInstance(title, host, h -> {
            AppDatabase db = AppDatabase.getInstance(DiscoverActivity.this);
            db.hostDao().insertAll(h);
            finish();
        });
        dialog.show(getSupportFragmentManager(), HostDetailDialog.TAG);
    }
}
