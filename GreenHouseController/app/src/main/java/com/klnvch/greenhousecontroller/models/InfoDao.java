package com.klnvch.greenhousecontroller.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InfoDao {
    @Query("SELECT * FROM info")
    List<Info> getAll();

    @Insert
    void insertAll(Info... info);
}
