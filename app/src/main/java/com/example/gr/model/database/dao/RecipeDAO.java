package com.example.gr.model.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gr.model.Food;
import com.example.gr.model.Recipe;

import java.util.List;

@Dao

public interface RecipeDAO {
    @Insert
    void insertRecipe(Recipe recipe);

    @Query("SELECT * FROM recipe LIMIT 10")
    List<Recipe> getAllRecipe();
    @Query("Select * from recipe where timestamp = :timestamp")
    Recipe getRecipeByTimestamp(long timestamp);
    @Query("SELECT * FROM recipe WHERE name LIKE :searchSeq")
    List<Recipe> findRecipeByName(String searchSeq);
    @Query("SELECT * FROM recipe WHERE id=:id")
    Recipe getRecipeById(int id);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Update
    int  updateRecipe(Recipe recipe);

    @Query("DELETE from recipe")
    void deleteAllRecipe();
}
