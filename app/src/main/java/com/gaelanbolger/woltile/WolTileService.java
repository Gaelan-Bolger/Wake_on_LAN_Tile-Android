package com.gaelanbolger.woltile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.text.TextUtils;
import android.util.Log;

import com.gaelanbolger.woltile.util.NetworkUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WolTileService extends TileService {

    private static final String TAG = WolTileService.class.getSimpleName();

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
        SharedPreferences preferences = getSharedPreferences();
        String hostName = preferences.getString(WolTileApplication.PREF_HOST_NAME, null);
        getQsTile().setLabel(!TextUtils.isEmpty(hostName) ? hostName : getString(R.string.tile_label));
        int resId = preferences.getInt(WolTileApplication.PREF_ICON, 0);
        getQsTile().setIcon(Icon.createWithResource(this, resId > 0 ? resId : R.drawable.ic_laptop_black_24dp));
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
        SharedPreferences preferences = getSharedPreferences();
        String ipAddress = preferences.getString(WolTileApplication.PREF_IP_ADDRESS, null);
        String macAddress = preferences.getString(WolTileApplication.PREF_MAC_ADDRESS, null);
        int port = preferences.getInt(WolTileApplication.PREF_PORT, WolTileApplication.DEFAULT_PORT);
        if (!TextUtils.isEmpty(ipAddress) && !TextUtils.isEmpty(macAddress)) {
            new WakeOnLanTask(ipAddress, macAddress, port).execute();
        } else {
            startActivityAndCollapse(new Intent(this, MainActivity.class));
        }
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
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
                byte[] macBytes = getMacBytes(macAddress);
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

        private byte[] getMacBytes(String macStr) throws IllegalArgumentException {
            byte[] bytes = new byte[6];
            String[] hex = macStr.split("([:-])");
            if (hex.length != 6) {
                throw new IllegalArgumentException("Invalid MAC address");
            }
            try {
                for (int i = 0; i < 6; i++) {
                    bytes[i] = (byte) Integer.parseInt(hex[i], 16);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid hex digit in MAC address");
            }
            return bytes;
        }
    }
}
