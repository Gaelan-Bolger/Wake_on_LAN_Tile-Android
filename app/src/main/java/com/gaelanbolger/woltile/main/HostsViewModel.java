package com.gaelanbolger.woltile.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.gaelanbolger.woltile.data.AppDatabase;
import com.gaelanbolger.woltile.data.Host;

import java.util.List;

public class HostsViewModel extends AndroidViewModel {

    private final LiveData<List<Host>> hosts;

    public HostsViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        hosts = database.hostDao().getAllLive();
    }

    public LiveData<List<Host>> getHosts() {
        return hosts;
    }
}
