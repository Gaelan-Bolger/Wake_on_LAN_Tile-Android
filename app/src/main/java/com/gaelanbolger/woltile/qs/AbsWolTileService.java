package com.gaelanbolger.woltile.qs;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.text.TextUtils;
import android.util.Log;

import com.gaelanbolger.woltile.MainActivity;
import com.gaelanbolger.woltile.R;
import com.gaelanbolger.woltile.data.AppDatabase;
import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.util.NetworkUtils;
import com.gaelanbolger.woltile.util.ResourceUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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

        boolean wifiConnected = NetworkUtils.isWifiConnected(this);
        getQsTile().setState(wifiConnected ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);

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
        if (TextUtils.isEmpty(ipAddress) || TextUtils.isEmpty(macAddress)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivityAndCollapse(intent);
        } else {
            new WakeOnLanTask(ipAddress, macAddress, mHost.getPort()).execute();
        }
    }

    // https://stackoverflow.com/a/13655016/1670446
    private static class WakeOnLanTask extends AsyncTask<Void, Void, Void> {

        private String ipAddress;
        private String macAddress;
        private int port;

        WakeOnLanTask(String ipAddress, String macAddress, int port) {
            this.ipAddress = ipAddress;
            this.macAddress = macAddress;
            this.port = port;
        }

        @Override
        protected Void doInBackground(Void... param) {
            try {
                byte[] macBytes = NetworkUtils.MacUtils.getMacBytes(macAddress);
                byte[] bytes = new byte[6 + 16 * macBytes.length];
                for (int i = 0; i < 6; i++) {
                    bytes[i] = (byte) 0xFF;
                }
                for (int i = 6; i < bytes.length; i += macBytes.length) {
                    System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
                }
                InetAddress address = InetAddress.getByName(ipAddress);
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
                DatagramSocket socket = new DatagramSocket();
                socket.send(packet);
                socket.close();
                Log.d(TAG, "doInBackground: Wake-on-LAN packet sent");
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Failed to send Wake-on-LAN packet", e);
            }
            return null;
        }
    }
}
