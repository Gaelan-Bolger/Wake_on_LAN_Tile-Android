package com.gaelanbolger.woltile.discover;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class DiscoverViewModel extends AndroidViewModel {

    private DiscoverLiveData data;

    public DiscoverViewModel(@NonNull Application application) {
        super(application);
        data = new DiscoverLiveData(application);
    }

    public DiscoverLiveData getData() {
        return data;
    }

    @Override
    protected void onCleared() {
        data.destroy();
    }
}
