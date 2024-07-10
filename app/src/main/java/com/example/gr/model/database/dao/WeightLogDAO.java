package com.example.gr.model.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gr.model.WeightLog;

import java.util.List;
@Dao
public interface WeightLogDAO {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        long insertWeightLog(WeightLog weightLog);

        @Query("SELECT * FROM weightlog ORDER BY timeStamp ASC")
        List<WeightLog> getAllWeightLog();
        @Query("SELECT * FROM weightlog WHERE timestamp LIKE :timestamp")
        WeightLog findWeightLogByTimeStamp(long timestamp);

        @Query("Select * from weightlog where timeStamp < :timestamp LIMIT 1" )
        WeightLog findLastWeightLogByTimeStamp(long timestamp);
        @Delete
        void deleteWeightLog(WeightLog weightlog);

        @Update
        void updateWeightLog(WeightLog weightlog);

        @Query("DELETE from weightlog")
        void deleteAllWeightLog();
}
