package com.gaelanbolger.woltile.discover;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gaelanbolger.woltile.data.Host;
import com.gaelanbolger.woltile.settings.AppSettings;
import com.gaelanbolger.woltile.util.NetworkUtils.ArpUtils;
import com.pddstudio.networkutils.NetworkUtils;
import com.pddstudio.networkutils.SubnetScannerService;
import com.pddstudio.networkutils.interfaces.ProcessCallback;
import com.pddstudio.networkutils.model.ScanResult;

import java.util.List;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class DiscoverLiveData extends LiveData<DiscoverResult> {

    private static final int TO_FAST = 50;
    private static final int TO_SLOW = 300;

    private final DiscoverResult result = new DiscoverResult();

    public DiscoverLiveData(Context context) {
        loadHosts(context);
    }

    private void loadHosts(Context context) {
        NetworkUtils networkUtils = NetworkUtils.get(context, false);
        DiscoverCallback discoverCallback = new DiscoverCallback(ArpUtils.getArpTable());
        SubnetScannerService scannerService = networkUtils.getSubNetScannerService(discoverCallback);
        scannerService.setTimeout(AppSettings.isFastDiscover(context) ? TO_FAST : TO_SLOW);
        if (AppSettings.isMtDiscover(context)) scannerService.startMultiThreadScanning();
        else scannerService.startScan();
    }

    class DiscoverCallback implements ProcessCallback {

        private final String TAG = DiscoverCallback.class.getSimpleName();

        private Map<String, String> arpTable;
        private String serviceName;

        DiscoverCallback(Map<String, String> arpTable) {
            this.arpTable = arpTable;
        }

        @Override
        public void onProcessStarted(@NonNull String serviceName) {
            Log.d(TAG, "onProcessStarted: " + serviceName);
            this.serviceName = serviceName;
        }

        @Override
        public void onProcessUpdate(@NonNull Object processUpdate) {
            Log.d(TAG, "onProcessUpdate: ");
            ScanResult scanResult = (ScanResult) processUpdate;
            if (scanResult.isReachable()) {
                String hostName = scanResult.getHostName();
                String ipAddress = scanResult.getIpAddress();
                String macAddress = arpTable.get(ipAddress);
                List<Host> hosts = result.getHosts();
                hosts.add(new Host(hostName, ipAddress, macAddress));
                setValue(result);
            }
            if (scanResult.getIpAddress().endsWith(String.valueOf(255))) {
                onProcessFinished(serviceName, "Discover finished");
            }
        }

        @Override
        public void onProcessFinished(@NonNull String serviceName, @Nullable String endMessage) {
            Log.d(TAG, "onProcessFinished: " + endMessage);
            result.setLoaded(true);
            setValue(result);
        }

        @Override
        public void onProcessFailed(@NonNull String serviceName, @Nullable String errorMessage, int errorCode) {
            Log.e(TAG, "onProcessFailed: " + errorMessage);
        }
    }
}
