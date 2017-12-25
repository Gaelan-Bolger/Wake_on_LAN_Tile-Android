package com.gaelanbolger.woltile.discover;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.gaelanbolger.woltile.data.Host;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class DiscoverTask extends AsyncTask<Void, Host, Void> {

    public interface Callback {
        void onNewHostDiscovered(Host host);

        void onAllHostsDiscovered(ArrayList<Host> hosts);
    }

    private final String TAG = DiscoverTask.class.getSimpleName();

    private final WeakReference<Context> mContextRef;
    private final WeakReference<Callback> mCallbackRef;
    private final ArrayList<Host> mHosts = new ArrayList<>();

    DiscoverTask(Context context, Callback callback) {
        mContextRef = new WeakReference<>(context);
        mCallbackRef = new WeakReference<>(callback);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Void doInBackground(Void... voids) {
        Context context = mContextRef.get();
        if (context != null) {
            WifiManager wifiManager = context.getSystemService(WifiManager.class);
            if (wifiManager != null) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                String ipString = Formatter.formatIpAddress(ipAddress);
                String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                for (int i = 0; i < 255 && !isCancelled(); i++) {
                    String testIp = prefix + String.valueOf(i);
                    if (!TextUtils.equals(ipString, testIp)) {
                        try {
                            InetAddress inetAddress = InetAddress.getByName(testIp);
                            if (inetAddress.isReachable(90)) {
                                Host host = new Host();
                                host.setName(inetAddress.getHostName());
                                host.setIp(inetAddress.getHostAddress());
                                publishProgress(host);
                                mHosts.add(host);
                            }
                        } catch (UnknownHostException e) {
                            Log.e(TAG, "doInBackground: Unknown host address: IP = [" + testIp + "]", e);
                        } catch (IOException e) {
                            Log.e(TAG, "doInBackground: Unable to reach host: IP = [" + testIp + "]", e);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Host... hosts) {
        Callback callback = mCallbackRef.get();
        if (callback != null) callback.onNewHostDiscovered(hosts[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Callback callback = mCallbackRef.get();
        if (callback != null) callback.onAllHostsDiscovered(mHosts);
    }
}
