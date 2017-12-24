package com.gaelanbolger.woltile.discover;

import com.gaelanbolger.woltile.data.Host;

import java.util.ArrayList;
import java.util.List;

public class DiscoverResult {

    private boolean loaded = false;
    private List<Host> hosts = new ArrayList<>();

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }
}
