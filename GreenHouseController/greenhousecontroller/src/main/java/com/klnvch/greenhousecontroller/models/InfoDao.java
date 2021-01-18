package com.klnvch.greenhousecontroller.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface InfoDao {
    @Query("SELECT * FROM info ORDER BY id DESC")
    Flowable<List<Info>> getAll();

    @Insert
    Completable insertAll(Info... info);

    @Insert
    Completable insertAll(List<Info> info);
}