package com.gaelanbolger.woltile.qs;

import android.os.AsyncTask;
import android.util.Log;

import com.gaelanbolger.woltile.util.NetworkUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// https://stackoverflow.com/a/13655016/1670446
class WakeOnLanTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = WakeOnLanTask.class.getSimpleName();

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
