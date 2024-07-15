package com.example.gr.model.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;

import java.util.List;

@Dao

public interface FoodLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFoodLog(FoodLog foodlog);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FoodLog> foodLogList);
    @Query("SELECT * FROM foodlog LIMIT 10")
    List<FoodLog> getAllFoodLog();

    @Query("Select * from foodlog where diaryId = :diaryId AND meal = 0")
    List<FoodLog> getBreakfastFoodLogs(int diaryId);
    @Query("Select * from foodlog where diaryId = :diaryId AND meal = 1")
    List<FoodLog> getLunchFoodLogs(int diaryId);
    @Query("Select * from foodlog where diaryId = :diaryId AND meal = 2")
    List<FoodLog> getDinnerFoodLogs(int diaryId);
    @Query("Select * from foodlog where diaryId = :diaryId AND meal = 3")
    List<FoodLog> getSnackFoodLogs(int diaryId);

//    @Query("SELECT * FROM foodlog WHERE name LIKE :searchSeq")
//    List<FoodLog> findFoodLogByName(String searchSeq);
    @Query("SELECT * FROM foodlog WHERE id=:id")
    FoodLog getFoodLogById(int id);

    @Delete
    void deleteFoodLog(FoodLog foodlog);

    @Update
    void updateFoodLog(FoodLog foodlog);

    @Query("DELETE from foodlog")
    void deleteAllFoodLog();
}
