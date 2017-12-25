package com.gaelanbolger.woltile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.gaelanbolger.woltile.adapter.HostAdapter;
import com.gaelanbolger.woltile.data.AppDatabase;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.dialog.HostDetailDialog;
import com.gaelanbolger.woltile.discover.DiscoverActivity;
import com.gaelanbolger.woltile.settings.SettingsActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private HostAdapter mHostAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHostAdapter = new HostAdapter(this, (view, position) -> {
            Host host = mHostAdapter.getItem(position);
            HostDetailDialog.newInstance(getString(R.string.edit_host), host, h -> mDb.hostDao().update(h))
                    .show(getSupportFragmentManager(), HostDetailDialog.TAG);
        });
        mDb = AppDatabase.getInstance(this);
        mDb.hostDao().getAllLive().observe(this, hosts -> mHostAdapter.setHosts(hosts));

        setContentView(R.layout.activity_main);

        RecyclerView hostsRecycler = findViewById(R.id.rv_host);
        hostsRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        hostsRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        hostsRecycler.setAdapter(mHostAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_discover:
                DiscoverActivity.start(this);
                return true;
            case R.id.item_settings:
                SettingsActivity.start(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
