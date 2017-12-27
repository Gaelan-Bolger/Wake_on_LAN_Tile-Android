package com.gaelanbolger.woltile.qs;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.text.TextUtils;
import android.util.Log;

import com.gaelanbolger.woltile.MainActivity;
import com.gaelanbolger.woltile.R;
import com.gaelanbolger.woltile.data.AppDatabase;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.settings.AppSettings;
import com.gaelanbolger.woltile.util.NetworkUtils;
import com.gaelanbolger.woltile.util.ResourceUtils;

public abstract class AbsWolTileService extends TileService {

    private static final String TAG = AbsWolTileService.class.getSimpleName();

    private Host mHost;

    abstract TileComponent getTileComponent();

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Log.d(TAG, "onTileAdded: ");
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        Log.d(TAG, "onTileRemoved: ");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.d(TAG, "onStartListening: ");
        AppDatabase db = AppDatabase.getInstance(this);
        mHost = db.hostDao().getById(getTileComponent().ordinal());
        if (mHost == null) mHost = new Host(getTileComponent().ordinal());

        String name = mHost.getName();
        getQsTile().setLabel(!TextUtils.isEmpty(name) ? name : getString(getTileComponent().getTitleResId()));
        int resId = ResourceUtils.getDrawableForName(this, mHost.getIcon());
        getQsTile().setIcon(Icon.createWithResource(this, resId > 0 ? resId : R.drawable.ic_laptop_general));
        getQsTile().setState(Tile.STATE_UNAVAILABLE);
        getQsTile().updateTile();

        int state = Tile.STATE_INACTIVE;
        boolean wifiConnected = NetworkUtils.isWifiConnected(this);
        if (wifiConnected && NetworkUtils.IpUtils.canPing(mHost.getIp())) {
            state = Tile.STATE_ACTIVE;
        }
        getQsTile().setState(state);
        getQsTile().updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.d(TAG, "onStopListening: ");
    }

    @Override
    public void onClick() {
        super.onClick();
        Log.d(TAG, "onClick: ");
        String ipAddress = mHost.getIp();
        String macAddress = mHost.getMac();
        int port = mHost.getPort();
        if (!TextUtils.isEmpty(ipAddress) && !TextUtils.isEmpty(macAddress)) {
            int packetCount = AppSettings.getPacketCount(this);
            for (int i = 0; i < packetCount; i++) {
                new WakeOnLanTask(ipAddress, macAddress, port).execute();
            }
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityAndCollapse(intent);
        }
    }
}
