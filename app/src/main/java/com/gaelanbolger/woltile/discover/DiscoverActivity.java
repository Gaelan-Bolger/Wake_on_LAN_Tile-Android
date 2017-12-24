package com.gaelanbolger.woltile.discover;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.gaelanbolger.woltile.data.Host;

public class DiscoverActivity extends AppCompatActivity implements DiscoverFragment.Listener {

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
        Toast.makeText(this, host.toString(), Toast.LENGTH_SHORT).show();
    }
}
