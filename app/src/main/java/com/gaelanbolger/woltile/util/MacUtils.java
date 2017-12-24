package com.gaelanbolger.woltile.util;

public class MacUtils {

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
