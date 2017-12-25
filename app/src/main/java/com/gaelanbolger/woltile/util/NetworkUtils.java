package com.gaelanbolger.woltile.util;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;

import java.util.regex.Pattern;

public class NetworkUtils {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = context.getSystemService(ConnectivityManager.class);
        if (cm == null) return false;

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
                && activeNetwork.isConnectedOrConnecting();

    }

    public static class IpUtils {

        public static final String REGEX_IPV4 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        public static boolean isValid(String ip) {
            Pattern pattern = Pattern.compile(REGEX_IPV4);
            return pattern.matcher(ip).matches();
        }
    }

    public static class MacUtils {

        public static final String REGEX_MAC = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";

        public static boolean isValid(String mac) {
            Pattern pattern = Pattern.compile(REGEX_MAC);
            return pattern.matcher(mac).matches();
        }

        public static String getMacString(byte[] macBytes) throws IllegalArgumentException {
            if (macBytes.length != 6) {
                throw new IllegalArgumentException("Invalid MAC address");
            }
            StringBuilder macBuilder = new StringBuilder();
            for (int b = 0; b < macBytes.length; b++) {
                macBuilder.append(String.format("%02X", macBytes[b]));
                if (b < macBytes.length - 1) macBuilder.append("-");
            }
            return macBuilder.toString();
        }

        public static byte[] getMacBytes(String macString) throws IllegalArgumentException {
            byte[] bytes = new byte[6];
            String[] hex = macString.split("([:-])");
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
