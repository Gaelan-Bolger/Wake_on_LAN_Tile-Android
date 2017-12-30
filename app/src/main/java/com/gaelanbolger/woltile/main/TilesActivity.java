package com.gaelanbolger.woltile.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.IconPopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.gaelanbolger.woltile.R;
import com.gaelanbolger.woltile.adapter.OnItemClickListener;
import com.gaelanbolger.woltile.data.AppDatabase;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.dialog.ConfirmActionDialog;
import com.gaelanbolger.woltile.edit.EditActivity;
import com.gaelanbolger.woltile.qs.TileComponent;
import com.gaelanbolger.woltile.qs.WakeOnLanTask;
import com.gaelanbolger.woltile.settings.AppSettings;
import com.gaelanbolger.woltile.settings.SettingsActivity;

import butterknife.BindView;

import static butterknife.ButterKnife.bind;
import static com.gaelanbolger.woltile.util.PackageUtils.isComponentEnabled;
import static com.gaelanbolger.woltile.util.PackageUtils.setComponentEnabled;

public class TilesActivity extends AppCompatActivity implements OnItemClickListener {

    private static final int REQ_EDIT_TILE = 1014;
    private static final int SPAN_COUNT = 3;

    private TilesAdapter mAdapter;

    @BindView(R.id.rv_tile)
    RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
        setContentView(R.layout.activity_tiles);
        bind(this);

        mAdapter = new TilesAdapter(this, TileComponent.values(), this);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        mRecycler.setAdapter(mAdapter);
        mRecycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mAdapter.setItemHeight(mRecycler.getHeight() / (mAdapter.getItemCount() / SPAN_COUNT));
            }
        });

        HostsViewModel viewModel = ViewModelProviders.of(this).get(HostsViewModel.class);
        viewModel.getHosts().observe(this, hosts -> mAdapter.setHosts(hosts));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.hasExtra(EditActivity.EXTRA_TILE_COMPONENT)) {
            String tileComponentName = intent.getStringExtra(EditActivity.EXTRA_TILE_COMPONENT);
            onEditTile(TileComponent.valueOf(tileComponentName));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT_TILE) {
            if (resultCode == RESULT_OK) {
                String tileComponentName = data.getStringExtra(EditActivity.EXTRA_TILE_COMPONENT);
                TileComponent tileComponent = TileComponent.valueOf(tileComponentName);
                ComponentName cn = new ComponentName(this, tileComponent.getServiceClass());
                if (!isComponentEnabled(getPackageManager(), cn)) {
                    setComponentEnabled(getPackageManager(), cn, true);
                    makeSnackbar(R.string.tile_enabled);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_tiles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_settings:
                SettingsActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        TileComponent tileComponent = mAdapter.getItem(position);
        onEditTile(tileComponent);
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        TileComponent tileComponent = mAdapter.getItem(position);
        Host host = mAdapter.getHost(tileComponent.ordinal());
        if (host != null) {
            IconPopupMenu popupMenu = new IconPopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.popup_tile, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.item_send:
                        int packetCount = AppSettings.getPacketCount(this);
                        for (int i = 0; i < packetCount; i++) {
                            new WakeOnLanTask(host.getIp(), host.getMac(), host.getPort()).execute();
                        }
                        return true;
                    case R.id.item_delete:
                        ConfirmActionDialog.newInstance(getString(R.string.confirm_delete_tile), () -> {
                            AppDatabase.io().execute(() -> {
                                AppDatabase database = AppDatabase.getInstance(this);
                                database.hostDao().delete(host);
                            });
                            ComponentName cn = new ComponentName(this, tileComponent.getServiceClass());
                            if (isComponentEnabled(getPackageManager(), cn)) {
                                setComponentEnabled(getPackageManager(), cn, false);
                                makeSnackbar(R.string.tile_disabled);
                            }
                        }).show(getSupportFragmentManager(), ConfirmActionDialog.TAG);
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            });
            popupMenu.show();
            return true;
        }
        return false;
    }

    private void onEditTile(TileComponent tileComponent) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditActivity.EXTRA_TILE_COMPONENT, tileComponent.name());
        startActivityForResult(intent, REQ_EDIT_TILE);
    }

    private void makeSnackbar(@StringRes int textResId) {
        Snackbar.make(findViewById(android.R.id.content), textResId, Snackbar.LENGTH_SHORT).show();
    }
}
