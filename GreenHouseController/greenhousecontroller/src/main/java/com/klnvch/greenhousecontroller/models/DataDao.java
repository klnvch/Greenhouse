package com.klnvch.greenhousecontroller.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface DataDao {
    @Query("SELECT * FROM data")
    List<Data> getAll();

    @Insert
    Completable insertAll(Data... data);
}
