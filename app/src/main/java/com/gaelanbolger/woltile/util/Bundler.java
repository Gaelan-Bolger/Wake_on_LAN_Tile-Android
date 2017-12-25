package com.gaelanbolger.woltile.util;

import android.os.Bundle;

public class Bundler {

    private Bundle bundle;

    public Bundler with(String key, String value) {
        getBundle().putString(key, value);
        return this;
    }

    public Bundler with(String key, String[] value) {
        getBundle().putStringArray(key, value);
        return this;
    }

    public Bundler with(String key, int value) {
        getBundle().putInt(key, value);
        return this;
    }

    public Bundler with(String key, int[] value) {
        getBundle().putIntArray(key, value);
        return this;
    }

    public Bundler with(String key, long value) {
        getBundle().putLong(key, value);
        return this;
    }

    public Bundler with(String key, long[] value) {
        getBundle().putLongArray(key, value);
        return this;
    }

    public Bundler with(String key, boolean value) {
        getBundle().putBoolean(key, value);
        return this;
    }

    public Bundle bundle() {
        return bundle;
    }

    private Bundle getBundle() {
        if (bundle == null) bundle = new Bundle();
        return bundle;
    }
}
