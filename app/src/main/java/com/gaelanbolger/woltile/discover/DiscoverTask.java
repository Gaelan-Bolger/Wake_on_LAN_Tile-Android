package com.gaelanbolger.woltile.discover;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.util.MacUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class DiscoverTask extends AsyncTask<Void, Host, ArrayList<Host>> {

    interface Callback {
        void onNewHostDiscovered(Host host);

        void onAllHostsDiscovered();
    }

    private final String TAG = DiscoverTask.class.getSimpleName();

    private WeakReference<Context> contextRef;
    private final WeakReference<Callback> callbackRef;

    public DiscoverTask(Context context, Callback callback) {
        contextRef = new WeakReference<>(context);
        callbackRef = new WeakReference<>(callback);
    }

    @Override
    protected ArrayList<Host> doInBackground(Void... voids) {
        ArrayList<Host> hosts = new ArrayList<>();
        Context context = contextRef.get();
        if (context != null) {
            WifiManager wifiManager = context.getSystemService(WifiManager.class);
            if (wifiManager == null) return hosts;

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ipString = Formatter.formatIpAddress(ipAddress);
            String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
            for (int i = 0; i < 255 && !isCancelled(); i++) {
                String testIp = prefix + String.valueOf(i);
                if (!TextUtils.equals(ipString, testIp)) {
                    try {
                        InetAddress inetAddress = InetAddress.getByName(testIp);
                        if (inetAddress.isReachable(100)) {
                            NetworkInterface network = NetworkInterface.getByInetAddress(inetAddress);
                            String mac = network != null ? MacUtils.getMacString(network.getHardwareAddress()) : "";
                            Host host = new Host(inetAddress.getHostName(), inetAddress.getHostAddress(), mac);
                            publishProgress(host);
                            hosts.add(host);
                            Log.d(TAG, "doInBackground: Host added: " + host);
                        }
                    } catch (UnknownHostException e) {
                        Log.e(TAG, "doInBackground: Unknown host address: IP = [" + testIp + "]", e);
                    } catch (IOException e) {
                        Log.e(TAG, "doInBackground: Unable to reach host: IP = [" + testIp + "]", e);
                    }
                }
            }
        }
        return hosts;
    }

    @Override
    protected void onProgressUpdate(Host... hosts) {
        Callback callback = callbackRef.get();
        if (callback != null) callback.onNewHostDiscovered(hosts[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Host> hosts) {
        Callback callback = callbackRef.get();
        if (callback != null) callback.onAllHostsDiscovered();
    }
}
