package com.example.gr.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;

import java.util.List;

@Dao

public interface FoodLogDAO {
    @Insert
    void insertFoodLog(FoodLog foodlog);

    @Query("SELECT * FROM foodlog LIMIT 10")
    List<FoodLog> getAllFoodLog();
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