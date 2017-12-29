package com.gaelanbolger.woltile.qs;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.gaelanbolger.woltile.R;
import com.gaelanbolger.woltile.data.AppDatabase;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.edit.EditActivity;
import com.gaelanbolger.woltile.main.TilesActivity;
import com.gaelanbolger.woltile.settings.AppSettings;
import com.gaelanbolger.woltile.util.NetworkUtils;
import com.gaelanbolger.woltile.util.ResourceUtils;
import com.gaelanbolger.woltile.view.OnDoubleClickListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class AbsWolTileService extends TileService {

    private static final String TAG = AbsWolTileService.class.getSimpleName();

    private Host mHost;
    private OnDoubleClickListener mClickListener = new TileClickListener();
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.d(TAG, "onStartListening: " + getTileComponent().getServiceClass());
        AppDatabase database = AppDatabase.getInstance(this);
        mDisposable.add(database.hostDao().getByIdRx(getTileComponent().ordinal())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        host -> {
                            mHost = host;
                            updateTile();
                        },
                        throwable -> Log.e(TAG, "onStartListening: Error retrieving host", throwable)
                ));
    }

    @Override
    public void onStopListening() {
        mDisposable.clear();
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();
        Log.d(TAG, "onClick: " + getTileComponent().getServiceClass());
        mClickListener.onClick(null);
    }

    private void updateTile() {
        String name = mHost != null ? mHost.getName() : null;
        getQsTile().setLabel(!TextUtils.isEmpty(name) ? name : getString(getTileComponent().getTitleResId()));
        int resId = mHost != null ? ResourceUtils.getDrawableForName(this, mHost.getIcon()) : 0;
        getQsTile().setIcon(Icon.createWithResource(this, resId > 0 ? resId : R.drawable.ic_laptop_general));
        boolean wifiConnected = NetworkUtils.isWifiConnected(this);
        int state = wifiConnected ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE;
        getQsTile().setState(state);
        getQsTile().updateTile();
    }

    private void showTileEditActivity() {
        Intent intent = new Intent(AbsWolTileService.this, TilesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EditActivity.EXTRA_TILE_COMPONENT, getTileComponent().name());
        startActivityAndCollapse(intent);
    }

    abstract TileComponent getTileComponent();

    class TileClickListener extends OnDoubleClickListener {

        @Override
        public void onSingleClick(View v) {
            if (mHost == null) return;

            String ipAddress = mHost.getIp();
            String macAddress = mHost.getMac();
            int port = mHost.getPort();
            if (!TextUtils.isEmpty(ipAddress) && !TextUtils.isEmpty(macAddress) && port > 0) {
                int packetCount = AppSettings.getPacketCount(AbsWolTileService.this);
                for (int i = 0; i < packetCount; i++) {
                    new WakeOnLanTask(ipAddress, macAddress, port).execute();
                }
            } else {
                showTileEditActivity();
            }
        }

        @Override
        public void onDoubleClick(View v) {
            showTileEditActivity();
        }
    }
}
