package com.gaelanbolger.woltile.discover;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.gaelanbolger.woltile.data.Host;

import java.util.ArrayList;


public class DiscoverLiveData extends LiveData<DiscoverResult> implements DiscoverTask.Callback {

    private final Context context;
    private final DiscoverResult result;

    DiscoverLiveData(Context context, DiscoverResult result) {
        this.context = context;
        this.result = result;
        loadHosts();
    }

    private void loadHosts() {
        new DiscoverTask(context, this).execute();
    }

    @Override
    public void onNewHostDiscovered(Host host) {
        result.getHosts().add(host);
        setValue(result);
    }

    @Override
    public void onAllHostsDiscovered(ArrayList<Host> hosts) {
        result.setHosts(hosts);
        result.setLoaded(true);
        setValue(result);
    }
}
