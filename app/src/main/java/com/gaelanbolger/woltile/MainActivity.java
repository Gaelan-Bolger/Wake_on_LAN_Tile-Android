package com.gaelanbolger.woltile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.gaelanbolger.woltile.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainFragment f = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
        if (f == null) f = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, f, MainFragment.TAG).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_settings:
                SettingsActivity.start(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
