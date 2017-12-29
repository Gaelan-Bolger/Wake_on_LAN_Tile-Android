package com.gaelanbolger.woltile.util;

import android.graphics.Color;

import java.util.Random;

public class ColorUtils {

    public static int getRandomColor() {
        Random r = new Random();
        int red = r.nextInt(255);
        int green = r.nextInt(255);
        int blue = r.nextInt(255);
        return Color.rgb(red, green, blue);
    }
}
