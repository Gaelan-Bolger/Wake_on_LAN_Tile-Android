package com.gaelanbolger.woltile.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface HostDao {

    @Query("SELECT * FROM Host")
    List<Host> getAll();

    @Query("SELECT * FROM Host")
    LiveData<List<Host>> getAllLive();

    @Query("SELECT * FROM Host WHERE id = :id")
    Host getById(int id);

    @Query("SELECT * FROM Host WHERE id = :id")
    LiveData<Host> getByIdLive(int id);

    @Query("SELECT COUNT(*) FROM Host")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Host... hosts);

    @Update
    void update(Host host);

    @Delete
    void delete(Host host);
}
