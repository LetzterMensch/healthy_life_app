package com.example.gr.controller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gr.model.Diary;
import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;
import com.example.gr.utils.DateTimeUtils;
import com.example.gr.utils.constant.Constant;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.databinding.ActivityRecipeDetailBinding;
import com.example.gr.model.Recipe;
import com.example.gr.utils.GlideUtils;

import java.util.Calendar;

public class RecipeDetailActivity extends BaseActivity {
    private ActivityRecipeDetailBinding mActivityRecipeDetailBinding;
    private Recipe mRecipe;
    private Diary mDiary;
    private int mMeal;
    private FoodLog mFoodLog;
    private Food mFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityRecipeDetailBinding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityRecipeDetailBinding.getRoot());

        getDataIntent();
        initToolbar();
        setDataRecipeDetail();
    }

    private void initToolbar() {
        mActivityRecipeDetailBinding.imgBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        mActivityRecipeDetailBinding.imgAdd.setOnClickListener(v->logRecipe());
    }
    private void logRecipe(){
        Food mFood = LocalDatabase.getInstance(this).foodDAO().getFoodById(mRecipe.getFoodID());
        mRecipe.setFood(mFood);
        mFoodLog = new FoodLog(mRecipe.getFood(), mMeal,mRecipe.getFood().getNumberOfServings(),mDiary.getId());
        mDiary.logFood(mFoodLog);
        Toast.makeText(this, "Đã lưu vào nhật ký", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.get(Constant.KEY_INTENT_RECIPE_OBJECT) != null) {
                mRecipe = (Recipe) bundle.get(Constant.KEY_INTENT_RECIPE_OBJECT);
                mMeal = bundle.getInt("key_meal");
                mDiary = (Diary) bundle.getSerializable("key_diary");
                System.out.println("recipe ID : " + mRecipe.getFoodID());
            }else if(bundle.get(Constant.KEY_INTENT_CREATE_RECIPE_OBJECT) != null) {
                mRecipe = (Recipe) bundle.get(Constant.KEY_INTENT_CREATE_RECIPE_OBJECT);
                mActivityRecipeDetailBinding.imgAdd.setVisibility(View.GONE);
                mActivityRecipeDetailBinding.btnSaveRecipe.setVisibility(View.VISIBLE);
                mActivityRecipeDetailBinding.btnSaveRecipe.setOnClickListener(v -> {
                    saveRecipe();
                });
            }
        }
    }

    private void saveRecipe() {
        long foodID = LocalDatabase.getInstance(this).foodDAO().insertFood(mRecipe.getFood());
        mRecipe.setFoodID((int) foodID);
        mRecipe.saveRecipe();
        finish();
    }

    private void setDataRecipeDetail() {
        if (mRecipe == null) {
            return;
        }
        GlideUtils.loadUrl(mRecipe.getImage(), mActivityRecipeDetailBinding.recipeImg);
        mActivityRecipeDetailBinding.tvFoodName.setText(mRecipe.getName());
        mActivityRecipeDetailBinding.tvFoodDescription.setText(mRecipe.getDescription());
        mActivityRecipeDetailBinding.tvRecipeIngredients.setText(mRecipe.getIngredients());
        mActivityRecipeDetailBinding.tvCalories.setText(String.valueOf(mRecipe.getCalories()));
        float progress;
        // Set indicator
        progress = mRecipe.getCarbs() * 100 / mRecipe.getCalories();
        System.out.println(progress);
        mActivityRecipeDetailBinding.carbIndicator.setProgress((int) progress);
        mActivityRecipeDetailBinding.foodCarb.setText(String.valueOf((int) mRecipe.getCarbs()));

        progress = mRecipe.getProtein() * 100 / mRecipe.getCalories();
        mActivityRecipeDetailBinding.proteinIndicator.setProgress((int) progress);
        mActivityRecipeDetailBinding.foodProtein.setText(String.valueOf((int) mRecipe.getProtein()));

        progress = mRecipe.getFat() * 100 / mRecipe.getCalories();
        mActivityRecipeDetailBinding.fatIndicator.setProgress((int) progress);
        mActivityRecipeDetailBinding.foodFat.setText(String.valueOf((int) mRecipe.getFat()));
    }
}
