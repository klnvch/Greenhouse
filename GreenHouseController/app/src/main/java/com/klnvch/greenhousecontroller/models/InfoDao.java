package com.klnvch.greenhousecontroller.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface InfoDao {
    @Query("SELECT * FROM info")
    List<Info> getAll();

    @Insert
    Completable insertAll(Info... info);
}
