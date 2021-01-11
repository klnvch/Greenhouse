package com.klnvch.greenhousecontroller.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface PhoneDataDao {
    @Query("SELECT * FROM phone ORDER BY id DESC LIMIT 1000")
    Flowable<List<PhoneData>> getAll();

    @Insert
    Completable insert(PhoneData data);
}
