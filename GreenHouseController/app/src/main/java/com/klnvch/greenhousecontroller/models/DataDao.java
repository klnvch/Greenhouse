package com.klnvch.greenhousecontroller.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataDao {
    @Query("SELECT * FROM data")
    List<Data> getAll();

    @Insert
    void insertAll(Data... data);
}
