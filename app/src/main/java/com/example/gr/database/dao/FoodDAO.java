package com.example.gr.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.gr.model.Food;

import java.util.List;

@Dao
public interface FoodDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Food> foodList);

    @Insert
    void insertFood(Food food);

    @Query("SELECT * FROM food LIMIT 10")
    List<Food> getAllFood();
    @Query("SELECT * FROM food WHERE name LIKE :searchSeq")
    List<Food> findFoodByName(String searchSeq);
    @Query("SELECT * FROM food WHERE id=:id")
    Food getFoodById(int id);

    @Delete
    void deleteFood(Food food);

    @Update
    void updateFood(Food food);

    @Query("DELETE from food")
    void deleteAllFood();
}
