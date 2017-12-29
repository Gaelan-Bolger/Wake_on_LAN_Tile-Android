package com.gaelanbolger.woltile.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface HostDao {

    @Query("SELECT * FROM Host")
    LiveData<List<Host>> getAllLive();

    @Query("SELECT * FROM Host")
    Flowable<List<Host>> getAllRx();

    @Query("SELECT * FROM Host WHERE id = :id")
    LiveData<Host> getByIdLive(long id);

    @Query("SELECT * FROM Host WHERE id = :id")
    Flowable<Host> getByIdRx(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Host... hosts);

    @Delete
    void delete(Host host);
}
